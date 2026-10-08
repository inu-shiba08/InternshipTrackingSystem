package dao;

import model.Evaluation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC CRUD operations for mentor Evaluation records. */
public class EvaluationDAO {

    public void create(Evaluation evaluation) throws SQLException {
        String sql = """
                INSERT INTO evaluations
                (evaluation_id, report_id, mentor_id, score, remarks,
                 evaluation_date)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, evaluation);
            statement.executeUpdate();
        }
    }

    public Evaluation findById(String evaluationId) throws SQLException {
        String sql = """
                SELECT evaluation_id, report_id, mentor_id, score,
                       remarks, evaluation_date
                FROM evaluations
                WHERE evaluation_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, evaluationId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public Evaluation findByReportId(String reportId) throws SQLException {
        String sql = """
                SELECT evaluation_id, report_id, mentor_id, score,
                       remarks, evaluation_date
                FROM evaluations
                WHERE report_id = ?
                ORDER BY evaluation_date DESC
                LIMIT 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reportId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Evaluation> findByMentorId(String mentorId)
            throws SQLException {
        String sql = """
                SELECT evaluation_id, report_id, mentor_id, score,
                       remarks, evaluation_date
                FROM evaluations
                WHERE mentor_id = ?
                ORDER BY evaluation_date DESC
                """;

        List<Evaluation> evaluations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mentorId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    evaluations.add(map(rs));
                }
            }
        }

        return evaluations;
    }

    public List<Evaluation> findAll() throws SQLException {
        String sql = """
                SELECT evaluation_id, report_id, mentor_id, score,
                       remarks, evaluation_date
                FROM evaluations
                ORDER BY evaluation_date DESC
                """;

        List<Evaluation> evaluations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                evaluations.add(map(rs));
            }
        }

        return evaluations;
    }

    public boolean update(Evaluation evaluation) throws SQLException {
        String sql = """
                UPDATE evaluations
                SET report_id = ?, mentor_id = ?, score = ?,
                    remarks = ?, evaluation_date = ?
                WHERE evaluation_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, evaluation.getReportId());
            statement.setString(2, evaluation.getMentorId());
            statement.setDouble(3, evaluation.getScore());
            statement.setString(4, evaluation.getRemarks());
            statement.setDate(5, Date.valueOf(evaluation.getEvaluationDate()));
            statement.setString(6, evaluation.getEvaluationId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(String evaluationId) throws SQLException {
        String sql = "DELETE FROM evaluations WHERE evaluation_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, evaluationId);
            return statement.executeUpdate() > 0;
        }
    }

    private void setParameters(PreparedStatement statement,
                                Evaluation evaluation) throws SQLException {
        statement.setString(1, evaluation.getEvaluationId());
        statement.setString(2, evaluation.getReportId());
        statement.setString(3, evaluation.getMentorId());
        statement.setDouble(4, evaluation.getScore());
        statement.setString(5, evaluation.getRemarks());
        statement.setDate(6, Date.valueOf(evaluation.getEvaluationDate()));
    }

    private Evaluation map(ResultSet rs) throws SQLException {
        return new Evaluation(
                rs.getString("evaluation_id"),
                rs.getString("report_id"),
                rs.getString("mentor_id"),
                rs.getDouble("score"),
                rs.getString("remarks"),
                rs.getDate("evaluation_date").toLocalDate());
    }
}
