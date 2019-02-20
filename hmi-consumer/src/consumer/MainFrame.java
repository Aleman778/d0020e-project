package consumer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JRootPane root;
    private LidarView view;

    public MainFrame() {
        setup();
    }

    private void setup() {
        setTitle("LiDAR HMI");
        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        root = new JRootPane();
        root.setLayout(new BorderLayout());
        setRootPane(root);

        view = new LidarView();
        root.add(view);
    }
}
