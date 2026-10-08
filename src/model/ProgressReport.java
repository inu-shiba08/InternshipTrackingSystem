package model;

import java.time.LocalDate;

/** A periodic progress report submitted by a student. */
public class ProgressReport {
    public static final String DEFAULT_STATUS = "SUBMITTED";

    private String reportId;
    private String internshipId;
    private int weekNumber;
    private String workDescription;
    private LocalDate submissionDate;
    private String status; // SUBMITTED, EVALUATED, PENDING

    /** Primary constructor for NEW reports: date = today, status = SUBMITTED. */
    public ProgressReport(String reportId, String internshipId, int weekNumber,
                          String workDescription) {
        this.reportId = reportId;
        this.internshipId = internshipId;
        this.weekNumber = requirePositiveWeek(weekNumber);
        this.workDescription = requireDescription(workDescription);
        this.submissionDate = LocalDate.now();
        this.status = DEFAULT_STATUS;
    }

    /** Overloaded constructor for RESTORING saved records (e.g. from a database). */
    public ProgressReport(String reportId, String internshipId, int weekNumber,
                          String workDescription, LocalDate submissionDate, String status) {
        this.reportId = reportId;
        this.internshipId = internshipId;
        this.weekNumber = requirePositiveWeek(weekNumber);
        this.workDescription = requireDescription(workDescription);
        this.submissionDate = (submissionDate != null) ? submissionDate : LocalDate.now();
        this.status = normalizeStatus(status);
    }

    private static int requirePositiveWeek(int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("Week number must be greater than 0.");
        }
        return week;
    }

    private static String requireDescription(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Work description cannot be empty.");
        }
        return text;
    }

    private static String normalizeStatus(String status) {
        return (status == null || status.trim().isEmpty())
                ? DEFAULT_STATUS : status.trim().toUpperCase();
    }

    public String getReportId() { return reportId; }

    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getInternshipId() { return internshipId; }

    public void setInternshipId(String internshipId) { this.internshipId = internshipId; }

    public int getWeekNumber() { return weekNumber; }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = requirePositiveWeek(weekNumber);
    }

    public String getWorkDescription() { return workDescription; }

    public void setWorkDescription(String workDescription) {
        this.workDescription = requireDescription(workDescription);
    }

    public LocalDate getSubmissionDate() { return submissionDate; }

    public void setSubmissionDate(LocalDate submissionDate) {
        if (submissionDate == null) {
            throw new IllegalArgumentException("Submission date cannot be null.");
        }
        this.submissionDate = submissionDate;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = normalizeStatus(status); }

    @Override
    public String toString() {
        return "ProgressReport{reportId='" + reportId + "', internshipId='" + internshipId
                + "', weekNumber=" + weekNumber + ", status='" + status
                + "', submissionDate=" + submissionDate + "}";
    }
}
