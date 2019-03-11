package eu.arrowhead.lidar.common.hmi;

import eu.arrowhead.lidar.common.LidarPoint;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HMIWindow extends JFrame {

    private JRootPane root;

    private LidarView view;

    private JPanel statusPanel;
    private JLabel statusLabel;
    private String statusMessage;

    public HMIWindow() {
        SwingUtilities.invokeLater(() -> setup());
    }

    private void setup() {
        setTitle("LiDAR Viewer");
        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        HMITheme theme = HMIThemeManager.getTheme();

        root = new JRootPane();
        root.setLayout(new BorderLayout());
        setRootPane(root);

        statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);
        statusPanel.setBackground(theme.bgPrimaryColor);
        statusLabel.setForeground(theme.fgPrimaryColor);
        root.add(statusPanel, BorderLayout.SOUTH);
        statusMessage = "Ready";

        view = new LidarView();
        root.add(view, BorderLayout.CENTER);
    }

    public void callback(ArrayList<LidarPoint> data) {
        view.updatePoints(data);
    }

    /**
     * Set the window status message.
     * Displays at the bottom of window.
     * @param message the message to display
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Store the current status message in memory.
     */
    public void storeStatus() {
        statusMessage = statusLabel.getText();
    }

    /**
     * Display the currently stored message from memory.
     * The default is the message "Ready".
     */
    public void restoreStatus() {
        setStatus(statusMessage);
        statusMessage = "Ready";
    }
}
