package consumer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class LidarView extends JPanel {

    private int xOffset = 0;
    private int yOffset = 0;
    private int scale = 64;

    private ArrayList<LidarPoint> points;

    public LidarView() {
        setName("LidarView");
        setupEvents();

        points = LidarGenerator.generatePosition(30);
        points.add(new LidarPoint(100.0, -Math.PI/4.0+Math.PI/2.0));
    }

    private void setupEvents() {
        MouseAdapter adapter = new MouseAdapter() {

            private int xPrev = 0;
            private int yPrev = 0;

            @Override
            public void mouseMoved(MouseEvent e) {
                xPrev = e.getX();
                yPrev = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                xOffset += e.getX() - xPrev;
                yOffset += e.getY() - yPrev;
                xPrev = e.getX();
                yPrev = e.getY();
                repaint();
                System.out.println("hej " + xOffset);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scale += e.getWheelRotation() * e.getScrollAmount();
                repaint();
                System.out.println("hej " + scale);
            }
        };
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,HMI.frame.getWidth(), HMI.frame.getHeight());
        g.setColor(Color.GRAY);
        for (int x = -scale; x < HMI.frame.getWidth() / scale + 1; x++) {
            for (int y = -scale; y < HMI.frame.getHeight() / scale + 1; y++) {
                g.drawRect(xOffset % scale + x * scale,yOffset % scale + y * scale, scale, scale);
            }
        }

        g.setColor(Color.RED);
        for (LidarPoint p : points) {
            g.fillOval((int) (p.distance *scale* Math.cos(p.angle))+HMI.frame.getWidth()/2, (int) (p.distance *scale* Math.sin(p.angle))+HMI.frame.getHeight()/2, 6, 6);
        }
    }
}
