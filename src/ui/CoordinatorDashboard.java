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
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

/**
 * Coordinator dashboard: View Internships/Students, View Status,
 * Generate/View Report and Logout. UI and navigation only; uses demo data.
 */
public class CoordinatorDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARD_HOME = "HOME";
    private static final String CARD_INTERNSHIPS = "INTERNSHIPS";
    private static final String CARD_STATUS = "STATUS";
    private static final String CARD_REPORT = "REPORT";

    private static final String FILTER_ALL = "All";
    private static final int STATUS_COLUMN = 2;

    // Demo internships: {student, email, company, role, mentor, duration}
    private static final String[][] INTERNSHIPS = {
        {"Aditi Sharma", "aditi@example.com", "TechNova Solutions", "Software Intern", "Dr. Mehta", "8 weeks"},
        {"Rohan Verma", "rohan@example.com", "DataBridge Labs", "Data Analyst Intern", "Prof. Rao", "6 weeks"},
        {"Meera Nair", "meera@example.com", "CloudPeak Systems", "QA Intern", "Ms. Iyer", "8 weeks"},
        {"Kabir Singh", "kabir@example.com", "GreenGrid Energy", "IT Support Intern", "Dr. Mehta", "4 weeks"},
        {"Sana Khan", "sana@example.com", "PixelCraft Studio", "UI Design Intern", "Prof. Rao", "12 weeks"}
    };

    // Demo status data: {student, company, status, last updated}
    private static final String[][] STATUS_ROWS = {
        {"Aditi Sharma", "TechNova Solutions", "Ongoing", "2026-10-05"},
        {"Rohan Verma", "DataBridge Labs", "Ongoing", "2026-10-03"},
        {"Meera Nair", "CloudPeak Systems", "Completed", "2026-09-28"},
        {"Kabir Singh", "GreenGrid Energy", "Pending", "2026-10-06"},
        {"Sana Khan", "PixelCraft Studio", "Pending", "2026-10-07"}
    };

    // Sample report text. Replace with real report output when it is available.
    private static final String SAMPLE_REPORT =
            "INTERNSHIP SUMMARY REPORT (sample data)\n"
            + "=======================================\n"
            + "Total students registered : 5\n"
            + "Ongoing internships       : 2\n"
            + "Completed internships     : 1\n"
            + "Pending approval          : 2\n"
            + "\n"
            + "Evaluations submitted     : 2\n"
            + "Average score             : 8.5 / 10\n"
            + "\n"
            + "Student results\n"
            + "---------------\n"
            + "Aditi Sharma  - TechNova Solutions - Ongoing   - Score 8/10\n"
            + "Rohan Verma   - DataBridge Labs    - Ongoing   - Not yet evaluated\n"
            + "Meera Nair    - CloudPeak Systems  - Completed - Score 9/10\n"
            + "Kabir Singh   - GreenGrid Energy   - Pending   - Not yet evaluated\n"
            + "Sana Khan     - PixelCraft Studio  - Pending   - Not yet evaluated\n";

    private final String coordinatorName;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private TableRowSorter<DefaultTableModel> statusSorter;
    private JComboBox<String> cmbStatusFilter;
    private JTextArea txtReport;

    public CoordinatorDashboard(String coordinatorName) {
        this.coordinatorName = coordinatorName;
        initComponents();
    }

    private void initComponents() {
        setTitle("Coordinator Dashboard");
        setSize(820, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        contentPanel.add(createHomePanel(), CARD_HOME);
        contentPanel.add(createScreen("Internships / Students", createInternshipsView()),
                CARD_INTERNSHIPS);
        contentPanel.add(createScreen("Internship Status", createStatusView()), CARD_STATUS);
        contentPanel.add(createScreen("Internship Report", createReportView()), CARD_REPORT);
        add(contentPanel, BorderLayout.CENTER);

        showCard(CARD_HOME);
    }

    // ---------- Common layout ----------

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblHeading = new JLabel("Coordinator Dashboard");
        lblHeading.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel lblWelcome = new JLabel("Welcome, " + coordinatorName);

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

        JButton btnInternships = new JButton("View Internships / Students");
        JButton btnStatus = new JButton("View Status");
        JButton btnReport = new JButton("Generate / View Report");
        btnInternships.addActionListener(e -> showCard(CARD_INTERNSHIPS));
        btnStatus.addActionListener(e -> showCard(CARD_STATUS));
        btnReport.addActionListener(e -> showCard(CARD_REPORT));
        menu.add(btnInternships);
        menu.add(btnStatus);
        menu.add(btnReport);

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

    // ---------- View Internships / Students ----------

    private JScrollPane createInternshipsView() {
        DefaultTableModel model = createReadOnlyModel(new String[]{
            "Student", "Email", "Company", "Role", "Mentor", "Duration"});
        for (String[] internship : INTERNSHIPS) {
            model.addRow(internship);
        }
        return new JScrollPane(new JTable(model));
    }

    // ---------- View Status ----------

    private JPanel createStatusView() {
        DefaultTableModel model = createReadOnlyModel(new String[]{
            "Student", "Company", "Status", "Last Updated"});
        for (String[] row : STATUS_ROWS) {
            model.addRow(row);
        }
        JTable statusTable = new JTable(model);
        statusSorter = new TableRowSorter<>(model);
        statusTable.setRowSorter(statusSorter);

        cmbStatusFilter = new JComboBox<>(
                new String[]{FILTER_ALL, "Pending", "Ongoing", "Completed"});
        cmbStatusFilter.addActionListener(e -> applyStatusFilter());

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterRow.add(new JLabel("Filter by status:"));
        filterRow.add(cmbStatusFilter);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(filterRow, BorderLayout.NORTH);
        panel.add(new JScrollPane(statusTable), BorderLayout.CENTER);
        return panel;
    }

    private void applyStatusFilter() {
        String selected = (String) cmbStatusFilter.getSelectedItem();
        if (selected == null || FILTER_ALL.equals(selected)) {
            statusSorter.setRowFilter(null);
        } else {
            statusSorter.setRowFilter(RowFilter.regexFilter("^" + selected + "$", STATUS_COLUMN));
        }
    }

    // ---------- Generate / View Report ----------

    private JPanel createReportView() {
        txtReport = new JTextArea("Click \"Generate Report\" to view the sample report.");
        txtReport.setEditable(false);
        txtReport.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JButton btnGenerate = new JButton("Generate Report");
        btnGenerate.addActionListener(e -> txtReport.setText(SAMPLE_REPORT));
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(btnGenerate);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JScrollPane(txtReport), BorderLayout.CENTER);
        panel.add(buttonRow, BorderLayout.SOUTH);
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
}
