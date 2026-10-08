package service;

import exceptions.InvalidSubException;
import model.Evaluation;
import model.ProgressReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Handles mentor evaluation of submitted progress reports.
 *
 * The service performs validation and keeps evaluations in memory until
 * EvaluationDAO is implemented and connected by the database owner.
 */
public class EvaluationService {

    private final List<Evaluation> evaluations = new ArrayList<>();
    private final ProgressService progressService;

    public EvaluationService(ProgressService progressService) {
        if (progressService == null) {
            throw new IllegalArgumentException("ProgressService cannot be null.");
        }
        this.progressService = progressService;
    }

    /**
     * Creates an evaluation for a submitted progress report.
     *
     * @param reportId report being evaluated
     * @param mentorId mentor performing the evaluation
     * @param score score from 0 to 10
     * @param remarks mentor's remarks
     * @return created Evaluation
     * @throws InvalidSubException if the report or evaluation data is invalid
     */
    public Evaluation evaluateProgress(String reportId, String mentorId,
                                       double score, String remarks)
            throws InvalidSubException {

        validateEvaluation(reportId, mentorId, score, remarks);

        Evaluation evaluation = new Evaluation(
                generateId("EVAL"),
                reportId.trim(),
                mentorId.trim(),
                score,
                remarks.trim()
        );

        evaluations.add(evaluation);

        ProgressReport report = progressService.findReport(reportId.trim());
        report.setStatus("EVALUATED");

        return evaluation;
    }

    /**
     * Validates evaluation input without saving an evaluation.
     */
    public void validateEvaluation(String reportId, String mentorId,
                                   double score, String remarks)
            throws InvalidSubException {

        if (isBlank(reportId)) {
            throw new InvalidSubException("Report ID cannot be empty.");
        }

        if (isBlank(mentorId)) {
            throw new InvalidSubException("Mentor ID cannot be empty.");
        }

        if (Double.isNaN(score) || Double.isInfinite(score)
                || score < Evaluation.MIN_SCORE
                || score > Evaluation.MAX_SCORE) {
            throw new InvalidSubException(
                    "Score must be between " + Evaluation.MIN_SCORE
                            + " and " + Evaluation.MAX_SCORE + ".");
        }

        if (isBlank(remarks)) {
            throw new InvalidSubException("Evaluation remarks cannot be empty.");
        }

        ProgressReport report = progressService.findReport(reportId.trim());
        if (report == null) {
            throw new InvalidSubException(
                    "No progress report found for ID: " + reportId.trim());
        }

        if (findEvaluationForReport(reportId.trim()) != null) {
            throw new InvalidSubException(
                    "This progress report has already been evaluated.");
        }
    }

    /**
     * Returns all evaluations currently held by this service.
     */
    public List<Evaluation> getAllEvaluations() {
        return Collections.unmodifiableList(evaluations);
    }

    /**
     * Finds an evaluation for a particular progress report.
     */
    public Evaluation findEvaluationForReport(String reportId) {
        if (isBlank(reportId)) {
            return null;
        }

        for (Evaluation evaluation : evaluations) {
            if (evaluation.getReportId().equalsIgnoreCase(reportId.trim())) {
                return evaluation;
            }
        }
        return null;
    }

    private static String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
