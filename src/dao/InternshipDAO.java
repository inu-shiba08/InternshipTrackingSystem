package dao;

import model.Internship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC CRUD operations for Internship records. */
public class InternshipDAO {

    public void create(Internship internship) throws SQLException {
        String sql = """
                INSERT INTO internships
                (internship_id, student_id, company_name, role,
                 duration_weeks, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, internship);
            statement.executeUpdate();
        }
    }

    public Internship findById(String internshipId) throws SQLException {
        String sql = """
                SELECT internship_id, student_id, company_name, role,
                       duration_weeks, status
                FROM internships
                WHERE internship_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, internshipId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Internship> findByStudentId(String studentId)
            throws SQLException {
        String sql = """
                SELECT internship_id, student_id, company_name, role,
                       duration_weeks, status
                FROM internships
                WHERE student_id = ?
                ORDER BY internship_id
                """;

        List<Internship> internships = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    internships.add(map(rs));
                }
            }
        }

        return internships;
    }

    public List<Internship> findAll() throws SQLException {
        String sql = """
                SELECT internship_id, student_id, company_name, role,
                       duration_weeks, status
                FROM internships
                ORDER BY internship_id
                """;

        List<Internship> internships = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                internships.add(map(rs));
            }
        }

        return internships;
    }

    public boolean update(Internship internship) throws SQLException {
        String sql = """
                UPDATE internships
                SET student_id = ?, company_name = ?, role = ?,
                    duration_weeks = ?, status = ?
                WHERE internship_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, internship.getStudentId());
            statement.setString(2, internship.getCompanyName());
            statement.setString(3, internship.getRole());
            statement.setInt(4, internship.getDurationWeeks());
            statement.setString(5, internship.getStatus());
            statement.setString(6, internship.getInternshipId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(String internshipId) throws SQLException {
        String sql = "DELETE FROM internships WHERE internship_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, internshipId);
            return statement.executeUpdate() > 0;
        }
    }

    private void setParameters(PreparedStatement statement,
                                Internship internship) throws SQLException {
        statement.setString(1, internship.getInternshipId());
        statement.setString(2, internship.getStudentId());
        statement.setString(3, internship.getCompanyName());
        statement.setString(4, internship.getRole());
        statement.setInt(5, internship.getDurationWeeks());
        statement.setString(6, internship.getStatus());
    }

    private Internship map(ResultSet rs) throws SQLException {
        return new Internship(
                rs.getString("internship_id"),
                rs.getString("student_id"),
                rs.getString("company_name"),
                rs.getString("role"),
                rs.getInt("duration_weeks"),
                rs.getString("status"));
    }
}
