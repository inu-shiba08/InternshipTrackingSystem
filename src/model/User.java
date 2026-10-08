package model;

/**
 * Abstract base class representing a generic user of the system.
 * Demonstrates Abstraction, Encapsulation and (via viewDashboard) Polymorphism.
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String role; // "STUDENT", "MENTOR", "COORDINATOR"

    protected User(String userId, String name, String email, String password, String role) {
        this.userId = requireNotBlank(userId, "User ID");
        this.name = name;
        this.email = requireValidEmail(email);
        this.password = password;
        this.role = requireNotBlank(role, "Role");
    }

    /** Each concrete user type decides what its dashboard looks like. */
    public abstract void viewDashboard();

    private static String requireNotBlank(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be null or blank.");
        }
        return value.trim();
    }

    private static String requireValidEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format: must contain '@'.");
        }
        return email.trim();
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) {
        this.userId = requireNotBlank(userId, "User ID");
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = requireValidEmail(email);
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) {
        this.role = requireNotBlank(role, "Role");
    }

    /** Password is intentionally excluded from the output. */
    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name
                + "', email='" + email + "', role='" + role + "'}";
    }
}
