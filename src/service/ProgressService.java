package service;

import exceptions.InvalidSubException;
import model.Internship;
import model.ProgressReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Handles student progress-report submission and validation.
 *
 * This Stage 2 implementation keeps submitted reports in memory until the
 * ProgressReportDAO is implemented by the database owner. The public methods
 * are intentionally simple so the service can later be connected to the DAO.
 */
public class ProgressService {

    private final List<ProgressReport> reports = new ArrayList<>();
    private final List<Internship> internships = new ArrayList<>();

    /**
     * Registers an internship with this service so progress submissions can
     * be checked against a real internship.
     */
    public void registerInternship(Internship internship) throws InvalidSubException {
        if (internship == null) {
            throw new InvalidSubException("Internship cannot be null.");
        }
        if (isBlank(internship.getInternshipId())) {
            throw new InvalidSubException("Internship ID cannot be empty.");
        }
        if (isBlank(internship.getStudentId())) {
            throw new InvalidSubException("Student ID cannot be empty.");
        }

        for (Internship existing : internships) {
            if (existing.getInternshipId().equalsIgnoreCase(internship.getInternshipId())) {
                throw new InvalidSubException("Internship already exists: "
                        + internship.getInternshipId());
            }
        }

        internships.add(internship);
    }

    /**
     * Submits a new weekly progress report.
     *
     * @param internshipId internship to which the report belongs
     * @param weekNumber positive week number
     * @param workDescription work completed during the week
     * @return the created ProgressReport
     * @throws InvalidSubException when required data is missing or invalid
     */
    public ProgressReport submitProgress(String internshipId, int weekNumber,
                                         String workDescription)
            throws InvalidSubException {

        validateSubmission(internshipId, weekNumber, workDescription);

        ProgressReport report = new ProgressReport(
                generateId("REP"),
                internshipId.trim(),
                weekNumber,
                workDescription.trim()
        );

        reports.add(report);
        return report;
    }

    /**
     * Validates a progress submission without saving it.
     */
    public void validateSubmission(String internshipId, int weekNumber,
                                   String workDescription)
            throws InvalidSubException {

        if (isBlank(internshipId)) {
            throw new InvalidSubException("Internship ID cannot be empty.");
        }

        if (weekNumber <= 0) {
            throw new InvalidSubException("Week number must be greater than 0.");
        }

        if (isBlank(workDescription)) {
            throw new InvalidSubException("Work description cannot be empty.");
        }

        if (!internships.isEmpty() && findInternship(internshipId.trim()) == null) {
            throw new InvalidSubException(
                    "No internship found for ID: " + internshipId.trim());
        }

        if (hasReportForWeek(internshipId.trim(), weekNumber)) {
            throw new InvalidSubException(
                    "Progress for week " + weekNumber + " has already been submitted.");
        }
    }

    /**
     * Returns all reports currently held by this service.
     */
    public List<ProgressReport> getAllReports() {
        return Collections.unmodifiableList(reports);
    }

    /**
     * Returns all reports belonging to one internship.
     */
    public List<ProgressReport> getReportsForInternship(String internshipId)
            throws InvalidSubException {

        if (isBlank(internshipId)) {
            throw new InvalidSubException("Internship ID cannot be empty.");
        }

        List<ProgressReport> result = new ArrayList<>();
        for (ProgressReport report : reports) {
            if (report.getInternshipId().equalsIgnoreCase(internshipId.trim())) {
                result.add(report);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Finds a submitted report by its ID.
     */
    public ProgressReport findReport(String reportId) {
        if (isBlank(reportId)) {
            return null;
        }

        for (ProgressReport report : reports) {
            if (report.getReportId().equalsIgnoreCase(reportId.trim())) {
                return report;
            }
        }
        return null;
    }

    /**
     * Exposes the in-memory internship lookup for integration/testing.
     */
    public Internship findInternship(String internshipId) {
        if (isBlank(internshipId)) {
            return null;
        }

        for (Internship internship : internships) {
            if (internship.getInternshipId().equalsIgnoreCase(internshipId.trim())) {
                return internship;
            }
        }
        return null;
    }

    private boolean hasReportForWeek(String internshipId, int weekNumber) {
        for (ProgressReport report : reports) {
            if (report.getInternshipId().equalsIgnoreCase(internshipId)
                    && report.getWeekNumber() == weekNumber) {
                return true;
            }
        }
        return false;
    }

    private static String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
