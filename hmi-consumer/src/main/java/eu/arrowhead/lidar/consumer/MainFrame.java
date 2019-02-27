package eu.arrowhead.lidar.consumer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JRootPane root;
    private LidarView view;

    public MainFrame() {
        SwingUtilities.invokeLater(() -> setup());
    }

    private void setup() {
        setTitle("LiDAR Viewer");
        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        root = new JRootPane();
        root.setLayout(new BorderLayout());
        setRootPane(root);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("");
        panel.add(label);
        root.add(panel, BorderLayout.SOUTH);

        view = new LidarView(label);
        root.add(view, BorderLayout.CENTER);

    }
}
