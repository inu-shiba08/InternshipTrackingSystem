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
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/**
 * Mentor dashboard: View Students, View Progress Reports, Evaluate Student
 * and Logout. UI and navigation only; uses demo data.
 */
public class MentorDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARD_HOME = "HOME";
    private static final String CARD_STUDENTS = "STUDENTS";
    private static final String CARD_REPORTS = "REPORTS";
    private static final String CARD_EVALUATE = "EVALUATE";

    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 10;

    // Demo students: {id, name, email, company, role}
    private static final String[][] STUDENTS = {
        {"S001", "Aditi Sharma", "aditi@example.com", "TechNova Solutions", "Software Intern"},
        {"S002", "Rohan Verma", "rohan@example.com", "DataBridge Labs", "Data Analyst Intern"},
        {"S003", "Meera Nair", "meera@example.com", "CloudPeak Systems", "QA Intern"}
    };

    // Demo progress reports: {student, week, description, status}
    private static final String[][] REPORTS = {
        {"Aditi Sharma", "Week 1", "Built login page and studied project code", "Completed"},
        {"Aditi Sharma", "Week 2", "Worked on dashboard layout", "In Progress"},
        {"Rohan Verma", "Week 1", "Cleaned the sample dataset and wrote summary queries", "Completed"},
        {"Meera Nair", "Week 1", "Wrote test cases for the registration module", "Completed"}
    };

    private final String mentorName;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // View Progress Reports
    private JTable reportTable;
    private JTextArea txtReportDetails;

    // Evaluate Student
    private JComboBox<String> cmbStudent;
    private JLabel lblStudentInfo;
    private JTextField txtScore;
    private JTextArea txtRemarks;

    public MentorDashboard(String mentorName) {
        this.mentorName = mentorName;
        initComponents();
    }

    private void initComponents() {
        setTitle("Mentor Dashboard");
        setSize(820, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        contentPanel.add(createHomePanel(), CARD_HOME);
        contentPanel.add(createScreen("Assigned Students", createStudentsView()), CARD_STUDENTS);
        contentPanel.add(createScreen("Progress Reports", createReportsView()), CARD_REPORTS);
        contentPanel.add(createScreen("Evaluate Student", createEvaluateForm()), CARD_EVALUATE);
        add(contentPanel, BorderLayout.CENTER);

        showCard(CARD_HOME);
    }

    // ---------- Common layout ----------

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblHeading = new JLabel("Mentor Dashboard");
        lblHeading.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel lblWelcome = new JLabel("Welcome, " + mentorName);

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

        JButton btnStudents = new JButton("View Students");
        JButton btnReports = new JButton("View Progress Reports");
        JButton btnEvaluate = new JButton("Evaluate Student");
        btnStudents.addActionListener(e -> showCard(CARD_STUDENTS));
        btnReports.addActionListener(e -> showCard(CARD_REPORTS));
        btnEvaluate.addActionListener(e -> showCard(CARD_EVALUATE));
        menu.add(btnStudents);
        menu.add(btnReports);
        menu.add(btnEvaluate);

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

    // ---------- View Students ----------

    private JScrollPane createStudentsView() {
        DefaultTableModel model = createReadOnlyModel(
                new String[]{"Student ID", "Name", "Email", "Company", "Role"});
        for (String[] student : STUDENTS) {
            model.addRow(student);
        }
        return new JScrollPane(new JTable(model));
    }

    // ---------- View Progress Reports ----------

    private JSplitPane createReportsView() {
        DefaultTableModel model = createReadOnlyModel(
                new String[]{"Student", "Week", "Description", "Status"});
        for (String[] report : REPORTS) {
            model.addRow(report);
        }
        reportTable = new JTable(model);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedReportDetails();
            }
        });

        txtReportDetails = new JTextArea("Select a report above to see its details.");
        txtReportDetails.setEditable(false);
        txtReportDetails.setLineWrap(true);
        txtReportDetails.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(txtReportDetails);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Report Details"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(reportTable), detailsScroll);
        splitPane.setResizeWeight(0.65);
        return splitPane;
    }

    private void showSelectedReportDetails() {
        int row = reportTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtReportDetails.setText(
                "Student: " + reportTable.getValueAt(row, 0)
                + "\nWeek: " + reportTable.getValueAt(row, 1)
                + "\nStatus: " + reportTable.getValueAt(row, 3)
                + "\n\nWork done:\n" + reportTable.getValueAt(row, 2));
    }

    // ---------- Evaluate Student ----------

    private JPanel createEvaluateForm() {
        String[] studentNames = new String[STUDENTS.length];
        for (int i = 0; i < STUDENTS.length; i++) {
            studentNames[i] = STUDENTS[i][1] + " (" + STUDENTS[i][0] + ")";
        }
        cmbStudent = new JComboBox<>(studentNames);
        cmbStudent.addActionListener(e -> updateStudentInfo());
        lblStudentInfo = new JLabel();
        txtScore = new JTextField();

        JPanel fields = new JPanel(new GridLayout(3, 2, 10, 12));
        fields.add(new JLabel("Student:"));
        fields.add(cmbStudent);
        fields.add(new JLabel("Internship:"));
        fields.add(lblStudentInfo);
        fields.add(new JLabel("Score (" + MIN_SCORE + "-" + MAX_SCORE + "):"));
        fields.add(txtScore);

        txtRemarks = new JTextArea(6, 30);
        txtRemarks.setLineWrap(true);
        txtRemarks.setWrapStyleWord(true);
        JPanel remarksPanel = new JPanel(new BorderLayout(5, 5));
        remarksPanel.add(new JLabel("Remarks:"), BorderLayout.NORTH);
        remarksPanel.add(new JScrollPane(txtRemarks), BorderLayout.CENTER);

        JButton btnSubmit = new JButton("Submit Evaluation");
        btnSubmit.addActionListener(e -> handleSubmitEvaluation());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(btnSubmit);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(fields, BorderLayout.NORTH);
        panel.add(remarksPanel, BorderLayout.CENTER);
        panel.add(buttonRow, BorderLayout.SOUTH);

        updateStudentInfo();
        return panel;
    }

    private void updateStudentInfo() {
        int index = cmbStudent.getSelectedIndex();
        if (index >= 0) {
            lblStudentInfo.setText(STUDENTS[index][3] + " - " + STUDENTS[index][4]);
        }
    }

    private void handleSubmitEvaluation() {
        String scoreText = txtScore.getText().trim();
        String remarks = txtRemarks.getText().trim();

        if (scoreText.isEmpty()) {
            showError("Please enter a score.");
            return;
        }
        int score;
        try {
            score = Integer.parseInt(scoreText);
        } catch (NumberFormatException ex) {
            showError("Score must be a whole number between "
                    + MIN_SCORE + " and " + MAX_SCORE + ".");
            return;
        }
        if (score < MIN_SCORE || score > MAX_SCORE) {
            showError("Score must be between " + MIN_SCORE + " and " + MAX_SCORE + ".");
            return;
        }
        if (remarks.isEmpty()) {
            showError("Please enter remarks.");
            return;
        }

        // Demo only: nothing is saved yet.
        JOptionPane.showMessageDialog(this,
                "Evaluation submitted (demo).\nStudent: " + cmbStudent.getSelectedItem()
                + "\nScore: " + score + "/" + MAX_SCORE,
                "Success", JOptionPane.INFORMATION_MESSAGE);
        txtScore.setText("");
        txtRemarks.setText("");
        showCard(CARD_HOME);
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
