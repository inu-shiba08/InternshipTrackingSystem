package dao;

import model.ProgressReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC CRUD operations for weekly ProgressReport records. */
public class ProgressReportDAO {

    public void create(ProgressReport report) throws SQLException {
        String sql = """
                INSERT INTO progress_reports
                (report_id, internship_id, week_number, work_description,
                 submission_date, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, report);
            statement.executeUpdate();
        }
    }

    public ProgressReport findById(String reportId) throws SQLException {
        String sql = """
                SELECT report_id, internship_id, week_number,
                       work_description, submission_date, status
                FROM progress_reports
                WHERE report_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reportId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<ProgressReport> findByInternshipId(String internshipId)
            throws SQLException {
        String sql = """
                SELECT report_id, internship_id, week_number,
                       work_description, submission_date, status
                FROM progress_reports
                WHERE internship_id = ?
                ORDER BY week_number
                """;

        List<ProgressReport> reports = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, internshipId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    reports.add(map(rs));
                }
            }
        }

        return reports;
    }

    public List<ProgressReport> findAll() throws SQLException {
        String sql = """
                SELECT report_id, internship_id, week_number,
                       work_description, submission_date, status
                FROM progress_reports
                ORDER BY internship_id, week_number
                """;

        List<ProgressReport> reports = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                reports.add(map(rs));
            }
        }

        return reports;
    }

    public boolean update(ProgressReport report) throws SQLException {
        String sql = """
                UPDATE progress_reports
                SET internship_id = ?, week_number = ?, work_description = ?,
                    submission_date = ?, status = ?
                WHERE report_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, report.getInternshipId());
            statement.setInt(2, report.getWeekNumber());
            statement.setString(3, report.getWorkDescription());
            statement.setDate(4, Date.valueOf(report.getSubmissionDate()));
            statement.setString(5, report.getStatus());
            statement.setString(6, report.getReportId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(String reportId) throws SQLException {
        String sql = "DELETE FROM progress_reports WHERE report_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reportId);
            return statement.executeUpdate() > 0;
        }
    }

    private void setParameters(PreparedStatement statement,
                                ProgressReport report) throws SQLException {
        statement.setString(1, report.getReportId());
        statement.setString(2, report.getInternshipId());
        statement.setInt(3, report.getWeekNumber());
        statement.setString(4, report.getWorkDescription());
        statement.setDate(5, Date.valueOf(report.getSubmissionDate()));
        statement.setString(6, report.getStatus());
    }

    private ProgressReport map(ResultSet rs) throws SQLException {
        return new ProgressReport(
                rs.getString("report_id"),
                rs.getString("internship_id"),
                rs.getInt("week_number"),
                rs.getString("work_description"),
                rs.getDate("submission_date").toLocalDate(),
                rs.getString("status"));
    }
}
