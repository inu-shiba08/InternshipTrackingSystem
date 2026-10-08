package model;

import java.time.LocalDate;

/** A mentor's evaluation of a student's progress report. */
public class Evaluation {
    public static final double MIN_SCORE = 0.0;
    public static final double MAX_SCORE = 10.0;

    private String evaluationId;
    private String reportId;
    private String mentorId;
    private double score; // 0.0 - 10.0
    private String remarks;
    private LocalDate evaluationDate;

    /** Constructor for NEW evaluations: date = today. */
    public Evaluation(String evaluationId, String reportId, String mentorId,
                      double score, String remarks) {
        this(evaluationId, reportId, mentorId, score, remarks, LocalDate.now());
    }

    /** Constructor for RESTORING saved evaluations with their original date. */
    public Evaluation(String evaluationId, String reportId, String mentorId,
                      double score, String remarks, LocalDate evaluationDate) {
        this.evaluationId = evaluationId;
        this.reportId = reportId;
        this.mentorId = mentorId;
        this.score = requireValidScore(score);
        this.remarks = remarks;
        this.evaluationDate = (evaluationDate != null) ? evaluationDate : LocalDate.now();
    }

    private static double requireValidScore(double score) {
        if (Double.isNaN(score) || score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException(
                    "Score must be between " + MIN_SCORE + " and " + MAX_SCORE + ".");
        }
        return score;
    }

    public String getEvaluationId() { return evaluationId; }

    public void setEvaluationId(String evaluationId) { this.evaluationId = evaluationId; }

    public String getReportId() { return reportId; }

    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getMentorId() { return mentorId; }

    public void setMentorId(String mentorId) { this.mentorId = mentorId; }

    public double getScore() { return score; }

    public void setScore(double score) { this.score = requireValidScore(score); }

    public String getRemarks() { return remarks; }

    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDate getEvaluationDate() { return evaluationDate; }

    public void setEvaluationDate(LocalDate evaluationDate) {
        if (evaluationDate == null) {
            throw new IllegalArgumentException("Evaluation date cannot be null.");
        }
        this.evaluationDate = evaluationDate;
    }

    @Override
    public String toString() {
        return "Evaluation{evaluationId='" + evaluationId + "', reportId='" + reportId
                + "', mentorId='" + mentorId + "', score=" + score + "/10, remarks='"
                + remarks + "', evaluationDate=" + evaluationDate + "}";
    }
}
