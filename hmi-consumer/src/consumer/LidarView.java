package consumer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class LidarView extends JPanel {

    private Viewport viewport;
    private ArrayList<LidarPoint> points;

    public LidarView() {
        setName("LidarView");

        viewport = new Viewport();
        MouseAdapter adapter = viewport.mouseAdapter(this);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);

        points = LidarGenerator.generatePosition(40);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,HMI.frame.getWidth(), HMI.frame.getHeight());
        drawGrid(g);
        drawSensor(g);
        drawData(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        int s = viewport.getScale();
        int w = (int) viewport.getWidth() / s;
        int h = (int) viewport.getHeight() / s;
        for (int x = -1; x < w + 1; x++) {
            for (int y = -1; y < h + 1; y++) {
                g.drawRect(viewport.relX(0) % s + x * s, viewport.relY(0) % s + y * s, s, s);
            }
        }
    }

    private void drawSensor(Graphics g) {
        int d = viewport.relSize(0.5);
        g.setColor(Color.BLUE);
        g.fillOval(viewport.relX(0) - d/2, viewport.relY(0) - d/2, d, d);
    }

    private void drawData(Graphics g) {
        g.setColor(Color.RED);
        for (LidarPoint p : points) {
            double x = p.distance * Math.cos(p.angle);
            double y = p.distance * Math.sin(p.angle);
            int d = viewport.relSize(0.1);
            g.fillOval(viewport.relX(x), viewport.relY(y), d, d);
        }
    }
}
