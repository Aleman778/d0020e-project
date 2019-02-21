package consumer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class LidarView extends JPanel {

    private Viewport viewport;
    private Thread provider;
    private ArrayList<LidarPoint> points;

    public LidarView() {
        setName("LidarView");

        viewport = new Viewport();
        MouseAdapter adapter = viewport.mouseAdapter(this);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);

        points = LidarGenerator.generate(40);
        provider = LidarGenerator.update(points, this);
        provider.start();
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
        int w = viewport.getWidth() / s;
        int h = viewport.getHeight() / s;
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
        LidarPoint last = points.get(points.size() - 1);
        int prevX = viewport.relX(last.distance * Math.cos(last.angle));
        int prevY = viewport.relY(last.distance * Math.sin(last.angle));

        g.setColor(Color.RED);
        for (LidarPoint p : points) {
            int x = viewport.relX(p.distance * Math.cos(p.angle));
            int y = viewport.relY(p.distance * Math.sin(p.angle));
            int d = viewport.relSize(0.1);
            g.fillOval(x - d / 2, y - d / 2, d, d);
            g.drawLine(prevX, prevY, x, y);
            prevX = x; prevY = y;
        }
    }
}
