package consumer;

import javax.swing.*;
import java.awt.*;

public class LidarView extends JPanel {

    public LidarView() {
        setName("LidarView");
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(64, 64,64,64);
    }
}
