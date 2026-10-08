package ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Login screen. UI prototype only: login uses fictional hard-coded demo
 * accounts. Replace {@link #isValidDemoLogin} with a real authentication
 * call when the backend is ready.
 */
public class LoginUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String ROLE_STUDENT = "Student";
    private static final String ROLE_MENTOR = "Mentor";
    private static final String ROLE_COORDINATOR = "Coordinator";

    // Demo accounts: {role, email, password, display name}
    private static final String[][] DEMO_ACCOUNTS = {
        {ROLE_STUDENT, "aditi@example.com", "student123", "Aditi Sharma"},
        {ROLE_MENTOR, "mehta@example.com", "mentor123", "Dr. Mehta"},
        {ROLE_COORDINATOR, "coordinator@example.com", "coord123", "Prof. Kapoor"}
    };

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JLabel lblMessage;

    public LoginUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Internship Tracking and Evaluation System - Login");
        setSize(440, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel lblTitle = new JLabel("Internship Tracking and Evaluation System",
                SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel lblSubtitle = new JLabel("Please log in to continue", SwingConstants.CENTER);
        panel.add(lblTitle);
        panel.add(lblSubtitle);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cmbRole = new JComboBox<>(new String[]{ROLE_STUDENT, ROLE_MENTOR, ROLE_COORDINATOR});

        panel.add(new JLabel("Username / Email:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Login as:"));
        panel.add(cmbRole);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> handleLogin());
        getRootPane().setDefaultButton(btnLogin); // Enter key = Login

        lblMessage = new JLabel(" ", SwingConstants.CENTER);
        lblMessage.setForeground(Color.RED);

        panel.add(btnLogin);
        panel.add(lblMessage);
        return panel;
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();

        if (username.isEmpty() && password.isEmpty()) {
            showError("Please enter your username/email and password.");
            return;
        }
        if (username.isEmpty()) {
            showError("Username/email cannot be empty.");
            return;
        }
        if (password.isEmpty()) {
            showError("Password cannot be empty.");
            return;
        }

        String displayName = isValidDemoLogin(role, username, password);
        if (displayName == null) {
            showError("Invalid credentials for the selected role.");
            return;
        }
        openDashboard(role, displayName, username);
    }

    /** Returns the display name if the demo login is valid, otherwise null. */
    private String isValidDemoLogin(String role, String username, String password) {
        for (String[] account : DEMO_ACCOUNTS) {
            boolean roleMatches = account[0].equals(role);
            boolean userMatches = account[1].equalsIgnoreCase(username);
            boolean passwordMatches = account[2].equals(password);
            if (roleMatches && userMatches && passwordMatches) {
                return account[3];
            }
        }
        return null;
    }

    private void showError(String message) {
        lblMessage.setText(message);
    }

    private void openDashboard(String role, String displayName, String email) {
        JFrame dashboard;
        if (ROLE_STUDENT.equals(role)) {
            dashboard = new StudentDashboard(displayName, email);
        } else if (ROLE_MENTOR.equals(role)) {
            dashboard = new MentorDashboard(displayName);
        } else {
            dashboard = new CoordinatorDashboard(displayName);
        }
        dashboard.setVisible(true);
        dispose();
    }
}
