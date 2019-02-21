package consumer;

import javafx.beans.Observable;

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
        int w = (int) (viewport.w / viewport.scale);
        int h = (int) (viewport.h / viewport.scale);
        for (int x = -w; x < w; x++) {
            for (int y = -h - x % 2; y < h; y += 2) {
                g.fillRect(viewport.relX(x), viewport.relY(y), viewport.getScale(), viewport.getScale());
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
            int d = viewport.relSize(0.01);
            g.fillOval(viewport.relX(x), viewport.relY(y), d, d);
        }
    }
}
