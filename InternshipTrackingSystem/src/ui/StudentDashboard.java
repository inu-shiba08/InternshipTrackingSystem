package ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Student dashboard: Register Internship, Submit Progress,
 * View Evaluation/Status and Logout. UI and navigation only; uses demo data.
 */
public class StudentDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARD_HOME = "HOME";
    private static final String CARD_REGISTER = "REGISTER";
    private static final String CARD_PROGRESS = "PROGRESS";
    private static final String CARD_STATUS = "STATUS";

    private static final String[] MENTORS = {"Dr. Mehta", "Prof. Rao", "Ms. Iyer"};
    private static final String[] DURATIONS = {"4 weeks", "6 weeks", "8 weeks", "12 weeks"};
    private static final String[] WEEKS = {"Week 1", "Week 2", "Week 3", "Week 4",
        "Week 5", "Week 6", "Week 7", "Week 8"};
    private static final String[] PROGRESS_STATUSES = {"Completed", "In Progress", "Delayed"};

    private final String studentName;
    private final String studentEmail;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // Register Internship form
    private JTextField txtCompany;
    private JTextField txtRole;
    private JTextField txtStartDate;
    private JComboBox<String> cmbDuration;
    private JComboBox<String> cmbMentor;

    // Submit Progress form
    private JComboBox<String> cmbWeek;
    private JComboBox<String> cmbProgressStatus;
    private JTextArea txtProgress;

    // View Evaluation / Status
    private JLabel lblCompanyValue;
    private JLabel lblRoleValue;
    private JLabel lblDurationValue;
    private JLabel lblMentorValue;
    private JLabel lblStatusValue;
    private DefaultTableModel progressTableModel;

    public StudentDashboard(String studentName, String studentEmail) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        initComponents();
    }

    private void initComponents() {
        setTitle("Student Dashboard");
        setSize(820, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        contentPanel.add(createHomePanel(), CARD_HOME);
        contentPanel.add(createScreen("Register Internship", createRegisterForm()), CARD_REGISTER);
        contentPanel.add(createScreen("Submit Progress", createProgressForm()), CARD_PROGRESS);
        contentPanel.add(createScreen("Evaluation / Status", createStatusView()), CARD_STATUS);
        add(contentPanel, BorderLayout.CENTER);

        showCard(CARD_HOME);
    }

    // ---------- Common layout ----------

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblHeading = new JLabel("Student Dashboard");
        lblHeading.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel lblWelcome = new JLabel("Welcome, " + studentName + " (" + studentEmail + ")");

        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.add(lblHeading);
        titles.add(lblWelcome);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> handleLogout());

        header.add(titles, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        return header;
    }

    private JPanel createHomePanel() {
        JPanel menu = new JPanel(new GridLayout(3, 1, 10, 15));
        menu.setPreferredSize(new Dimension(320, 170));

        JButton btnRegister = new JButton("Register Internship");
        JButton btnProgress = new JButton("Submit Progress");
        JButton btnStatus = new JButton("View Evaluation / Status");
        btnRegister.addActionListener(e -> showCard(CARD_REGISTER));
        btnProgress.addActionListener(e -> showCard(CARD_PROGRESS));
        btnStatus.addActionListener(e -> showCard(CARD_STATUS));
        menu.add(btnRegister);
        menu.add(btnProgress);
        menu.add(btnStatus);

        JPanel centered = new JPanel(new GridBagLayout());
        centered.add(menu);
        return centered;
    }

    /** Wraps a screen body with a "Back to Dashboard" button and a title. */
    private JPanel createScreen(String title, JComponent body) {
        JPanel screen = new JPanel(new BorderLayout(10, 10));
        screen.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton btnBack = new JButton("< Back to Dashboard");
        btnBack.addActionListener(e -> showCard(CARD_HOME));
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel top = new JPanel(new BorderLayout());
        top.add(btnBack, BorderLayout.WEST);
        top.add(lblTitle, BorderLayout.CENTER);
        top.add(new JLabel("                     "), BorderLayout.EAST); // balances the back button

        screen.add(top, BorderLayout.NORTH);
        screen.add(body, BorderLayout.CENTER);
        return screen;
    }

    private DefaultTableModel createReadOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // ---------- Register Internship ----------

    private JPanel createRegisterForm() {
        txtCompany = new JTextField();
        txtRole = new JTextField();
        txtStartDate = new JTextField();
        cmbDuration = new JComboBox<>(DURATIONS);
        cmbMentor = new JComboBox<>(MENTORS);

        JPanel fields = new JPanel(new GridLayout(5, 2, 10, 12));
        fields.add(new JLabel("Company:"));
        fields.add(txtCompany);
        fields.add(new JLabel("Internship Role / Title:"));
        fields.add(txtRole);
        fields.add(new JLabel("Duration:"));
        fields.add(cmbDuration);
        fields.add(new JLabel("Start Date (YYYY-MM-DD):"));
        fields.add(txtStartDate);
        fields.add(new JLabel("Mentor:"));
        fields.add(cmbMentor);

        JButton btnRegister = new JButton("Register Internship");
        btnRegister.addActionListener(e -> handleRegisterInternship());

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(btnRegister);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(fields, BorderLayout.NORTH);
        panel.add(buttonRow, BorderLayout.CENTER);
        return panel;
    }

    private void handleRegisterInternship() {
        String company = txtCompany.getText().trim();
        String role = txtRole.getText().trim();
        String startDate = txtStartDate.getText().trim();

        if (company.isEmpty() || role.isEmpty() || startDate.isEmpty()) {
            showError("Please fill in Company, Role and Start Date.");
            return;
        }
        try {
            LocalDate.parse(startDate);
        } catch (DateTimeParseException ex) {
            showError("Start date must be in YYYY-MM-DD format (e.g. 2026-11-03).");
            return;
        }

        // Demo only: show the registered details on the status screen.
        lblCompanyValue.setText(company);
        lblRoleValue.setText(role);
        lblDurationValue.setText((String) cmbDuration.getSelectedItem());
        lblMentorValue.setText((String) cmbMentor.getSelectedItem());
        lblStatusValue.setText("Pending Approval");

        JOptionPane.showMessageDialog(this,
                "Internship registered (demo).\nStatus: Pending Approval",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        txtCompany.setText("");
        txtRole.setText("");
        txtStartDate.setText("");
        showCard(CARD_STATUS);
    }

    // ---------- Submit Progress ----------

    private JPanel createProgressForm() {
        cmbWeek = new JComboBox<>(WEEKS);
        cmbProgressStatus = new JComboBox<>(PROGRESS_STATUSES);
        txtProgress = new JTextArea(8, 30);
        txtProgress.setLineWrap(true);
        txtProgress.setWrapStyleWord(true);

        JPanel fields = new JPanel(new GridLayout(2, 2, 10, 12));
        fields.add(new JLabel("Week:"));
        fields.add(cmbWeek);
        fields.add(new JLabel("Status:"));
        fields.add(cmbProgressStatus);

        JPanel descriptionPanel = new JPanel(new BorderLayout(5, 5));
        descriptionPanel.add(new JLabel("Work / Progress Description:"), BorderLayout.NORTH);
        descriptionPanel.add(new JScrollPane(txtProgress), BorderLayout.CENTER);

        JButton btnSubmit = new JButton("Submit Progress");
        btnSubmit.addActionListener(e -> handleSubmitProgress());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(btnSubmit);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(fields, BorderLayout.NORTH);
        panel.add(descriptionPanel, BorderLayout.CENTER);
        panel.add(buttonRow, BorderLayout.SOUTH);
        return panel;
    }

    private void handleSubmitProgress() {
        String description = txtProgress.getText().trim();
        if (description.isEmpty()) {
            showError("Please enter a work/progress description.");
            return;
        }

        // Demo only: add the entry to the table on the status screen.
        progressTableModel.addRow(new Object[]{
            cmbWeek.getSelectedItem(), description, cmbProgressStatus.getSelectedItem()});

        JOptionPane.showMessageDialog(this, "Progress submitted (demo).",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        txtProgress.setText("");
        showCard(CARD_STATUS);
    }

    // ---------- View Evaluation / Status ----------

    private JPanel createStatusView() {
        lblCompanyValue = new JLabel("TechNova Solutions");
        lblRoleValue = new JLabel("Software Intern");
        lblDurationValue = new JLabel("8 weeks");
        lblMentorValue = new JLabel("Dr. Mehta");
        lblStatusValue = new JLabel("Ongoing");

        JPanel info = new JPanel(new GridLayout(5, 2, 10, 5));
        info.setBorder(BorderFactory.createTitledBorder("Internship Details"));
        info.add(new JLabel("Company:"));
        info.add(lblCompanyValue);
        info.add(new JLabel("Role:"));
        info.add(lblRoleValue);
        info.add(new JLabel("Duration:"));
        info.add(lblDurationValue);
        info.add(new JLabel("Mentor:"));
        info.add(lblMentorValue);
        info.add(new JLabel("Status:"));
        info.add(lblStatusValue);

        progressTableModel = createReadOnlyModel(new String[]{"Week", "Description", "Status"});
        progressTableModel.addRow(new Object[]{
            "Week 1", "Built login page and studied project code", "Completed"});
        progressTableModel.addRow(new Object[]{
            "Week 2", "Worked on dashboard layout", "In Progress"});
        JScrollPane progressScroll = new JScrollPane(new JTable(progressTableModel));
        progressScroll.setBorder(BorderFactory.createTitledBorder("Progress Submitted"));

        JTextArea txtEvaluation = new JTextArea(
                "Score: 8/10\nRemarks: Good progress; improve documentation");
        txtEvaluation.setEditable(false);
        txtEvaluation.setRows(3);
        JScrollPane evaluationScroll = new JScrollPane(txtEvaluation);
        evaluationScroll.setBorder(BorderFactory.createTitledBorder("Mentor Evaluation"));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(info, BorderLayout.NORTH);
        panel.add(progressScroll, BorderLayout.CENTER);
        panel.add(evaluationScroll, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Navigation ----------

    private void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to log out?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LoginUI().setVisible(true);
            dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
