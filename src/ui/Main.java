package ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point. Starts the program at the login screen.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to the default look and feel
        }
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
