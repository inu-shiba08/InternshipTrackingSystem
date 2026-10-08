package dao;

import model.Coordinator;
import model.Mentor;
import model.Student;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for users and role-specific user details.
 *
 * The users table stores common User fields. Student/Mentor/Coordinator
 * detail tables store the fields specific to each concrete model class.
 */
public class UserDAO {

    public void create(User user) throws SQLException {
        String userSql = """
                INSERT INTO users (user_id, name, email, password, role)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement =
                             connection.prepareStatement(userSql)) {
                    setUserParameters(statement, user);
                    statement.executeUpdate();
                }

                insertRoleDetails(connection, user);
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(oldAutoCommit);
            }
        }
    }

    public User findById(String userId) throws SQLException {
        String sql = """
                SELECT user_id, name, email, password, role,
                       roll_number, department, employee_id,
                       designation, office_location
                FROM user_details
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapUser(rs) : null;
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = """
                SELECT user_id, name, email, password, role,
                       roll_number, department, employee_id,
                       designation, office_location
                FROM user_details
                WHERE email = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapUser(rs) : null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = """
                SELECT user_id, name, email, password, role,
                       roll_number, department, employee_id,
                       designation, office_location
                FROM user_details
                ORDER BY name
                """;

        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }

        return users;
    }

    public boolean update(User user) throws SQLException {
        String sql = """
                UPDATE users
                SET name = ?, email = ?, password = ?, role = ?
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                int affected;
                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {
                    statement.setString(1, user.getName());
                    statement.setString(2, user.getEmail());
                    statement.setString(3, user.getPassword());
                    statement.setString(4, user.getRole());
                    statement.setString(5, user.getUserId());
                    affected = statement.executeUpdate();
                }

                if (affected > 0) {
                    upsertRoleDetails(connection, user);
                }

                connection.commit();
                return affected > 0;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(oldAutoCommit);
            }
        }
    }

    public boolean delete(String userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            return statement.executeUpdate() > 0;
        }
    }

    private void setUserParameters(PreparedStatement statement, User user)
            throws SQLException {
        statement.setString(1, user.getUserId());
        statement.setString(2, user.getName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setString(5, user.getRole());
    }

    private void insertRoleDetails(Connection connection, User user)
            throws SQLException {
        if (user instanceof Student student) {
            String sql = """
                    INSERT INTO student_details
                    (user_id, roll_number, department, current_internship_id)
                    VALUES (?, ?, ?, ?)
                    """;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, student.getUserId());
                statement.setString(2, student.getRollNumber());
                statement.setString(3, student.getDepartment());
                statement.setString(4, student.getCurrentInternshipId());
                statement.executeUpdate();
            }
        } else if (user instanceof Mentor mentor) {
            String sql = """
                    INSERT INTO mentor_details
                    (user_id, employee_id, department, designation)
                    VALUES (?, ?, ?, ?)
                    """;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, mentor.getUserId());
                statement.setString(2, mentor.getEmployeeId());
                statement.setString(3, mentor.getDepartment());
                statement.setString(4, mentor.getDesignation());
                statement.executeUpdate();
            }
        } else if (user instanceof Coordinator coordinator) {
            String sql = """
                    INSERT INTO coordinator_details
                    (user_id, employee_id, office_location)
                    VALUES (?, ?, ?)
                    """;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, coordinator.getUserId());
                statement.setString(2, coordinator.getEmployeeId());
                statement.setString(3, coordinator.getOfficeLocation());
                statement.executeUpdate();
            }
        }
    }

    private void upsertRoleDetails(Connection connection, User user)
            throws SQLException {
        // Reusing a delete + insert keeps this compatible with MySQL versions
        // without requiring database-specific UPSERT syntax.
        if (user instanceof Student) {
            try (PreparedStatement delete =
                         connection.prepareStatement(
                                 "DELETE FROM student_details WHERE user_id = ?")) {
                delete.setString(1, user.getUserId());
                delete.executeUpdate();
            }
        } else if (user instanceof Mentor) {
            try (PreparedStatement delete =
                         connection.prepareStatement(
                                 "DELETE FROM mentor_details WHERE user_id = ?")) {
                delete.setString(1, user.getUserId());
                delete.executeUpdate();
            }
        } else if (user instanceof Coordinator) {
            try (PreparedStatement delete =
                         connection.prepareStatement(
                                 "DELETE FROM coordinator_details WHERE user_id = ?")) {
                delete.setString(1, user.getUserId());
                delete.executeUpdate();
            }
        }

        insertRoleDetails(connection, user);
    }

    private User mapUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");

        return switch (role.toUpperCase()) {
            case "STUDENT" -> {
                Student student = new Student(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("roll_number"),
                        rs.getString("department"));
                student.setCurrentInternshipId(
                        rs.getString("current_internship_id"));
                yield student;
            }
            case "MENTOR" -> new Mentor(
                    rs.getString("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("employee_id"),
                    rs.getString("department"),
                    rs.getString("designation"));
            case "COORDINATOR" -> new Coordinator(
                    rs.getString("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("employee_id"),
                    rs.getString("office_location"));
            default -> throw new SQLException("Unknown user role: " + role);
        };
    }
}
