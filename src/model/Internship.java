package model;

/** The internship record of a student. */
public class Internship {
    public static final String DEFAULT_STATUS = "APPLIED";

    private String internshipId;
    private String studentId;
    private String companyName;
    private String role;
    private int durationWeeks;
    private String status; // APPLIED, APPROVED, ONGOING, COMPLETED

    /** Convenience constructor: status defaults to "APPLIED". */
    public Internship(String internshipId, String studentId, String companyName,
                      String role, int durationWeeks) {
        this(internshipId, studentId, companyName, role, durationWeeks, DEFAULT_STATUS);
    }

    public Internship(String internshipId, String studentId, String companyName,
                      String role, int durationWeeks, String status) {
        this.internshipId = internshipId;
        this.studentId = studentId;
        this.companyName = companyName;
        this.role = role;
        this.durationWeeks = requirePositiveDuration(durationWeeks);
        this.status = normalizeStatus(status);
    }

    private static int requirePositiveDuration(int weeks) {
        if (weeks <= 0) {
            throw new IllegalArgumentException("Duration must be at least 1 week.");
        }
        return weeks;
    }

    private static String normalizeStatus(String status) {
        return (status == null || status.trim().isEmpty())
                ? DEFAULT_STATUS : status.trim().toUpperCase();
    }

    public String getInternshipId() { return internshipId; }

    public void setInternshipId(String internshipId) { this.internshipId = internshipId; }

    public String getStudentId() { return studentId; }

    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public int getDurationWeeks() { return durationWeeks; }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = requirePositiveDuration(durationWeeks);
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = normalizeStatus(status); }

    @Override
    public String toString() {
        return "Internship{internshipId='" + internshipId + "', studentId='" + studentId
                + "', companyName='" + companyName + "', role='" + role
                + "', durationWeeks=" + durationWeeks + ", status='" + status + "'}";
    }
}
