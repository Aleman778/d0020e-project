package eu.arrowhead.lidar.consumer;

import eu.arrowhead.lidar.common.LidarPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class LidarView extends JPanel {

    private Viewport viewport;
    private Thread provider;
    private LidarPoint selected;
    private ArrayList<LidarPoint> points;

    public LidarView() {
        setName("LidarView");

        viewport = new Viewport();
        MouseAdapter adapter = viewport.mouseAdapter(this);
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);
    }

    public void selectPoint(int x, int y) {
        selected = null;
        HMIConsumer.hmi.restoreStatus();
        for (LidarPoint p : points) {
            if (getBounds().contains(x, y)) {
                String info = "Selected data point - distance: " + p.distance + " meters, angle: " + Math.toDegrees(p.angle) + " degrees";
                HMIConsumer.hmi.storeStatus();
                HMIConsumer.hmi.setStatus(info);
                selected = new LidarPoint(p);
            }
        }
    }

    public Rectangle getBounds(LidarPoint p) {
        Rectangle bounds = new Rectangle();
        int diameter = viewport.relSize(0.1);
        bounds.x = viewport.relX(p.distance * Math.cos(p.angle)) - (int) diameter/2;
        bounds.y = viewport.relY(p.distance * Math.sin(p.angle)) - (int) diameter/2;
        bounds.width = (int) diameter; bounds.height = (int) diameter;
        return bounds;
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0, HMIConsumer.hmi.getWidth(), HMIConsumer.hmi.getHeight());
        drawGrid(g);
        if (points != null && !points.isEmpty())
            drawData(g);
        drawSensor(g);
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
        Graphics2D g2 = (Graphics2D) g;
        LidarPoint last = points.get(points.size() - 1);
        int prevX = viewport.relX(last.distance * Math.cos(last.angle));
        int prevY = viewport.relY(last.distance * Math.sin(last.angle));

        g.setColor(Color.RED);
        if (selected != null) {
            g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.1f));
        }
        for (LidarPoint p : points) {
            int x = viewport.relX(p.distance * Math.cos(p.angle));
            int y = viewport.relY(p.distance * Math.sin(p.angle));
            int d = viewport.relSize(0.1);

            g.fillOval(x - d / 2, y - d / 2, d, d);
            g2.setStroke(new BasicStroke(viewport.relSize(0.01)));
            g.drawLine(prevX, prevY, x, y);
            g2.setStroke(new BasicStroke(1));
            prevX = x; prevY = y;
        }

        if (selected != null) {
            g.setColor(Color.RED);
            int x = viewport.relX(selected.distance * Math.cos(selected.angle));
            int y = viewport.relY(selected.distance * Math.sin(selected.angle));
            int d = viewport.relSize(0.1);
            g.fillOval(x - d / 2, y - d / 2, d, d);
            g2.setStroke(new BasicStroke(viewport.relSize(0.01)));
            g.drawLine(viewport.relX(0), viewport.relY(0), x, y);
            g2.setStroke(new BasicStroke(1));
        }
    }
}
