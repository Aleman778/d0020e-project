package eu.arrowhead.lidar.common.hmi;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Viewport {

    public double x, y;
    public double scale;
    public double panSpeed = 0.005;

    public Viewport() {
        x = -HMIApplication.window.getWidth() / 128.0;
        y = -HMIApplication.window.getHeight() / 128.0;
        scale = 64.0;
    }

    public int relX(double x) {
        return (int) ((x - this.x) * scale);
    }

    public int relY(double y) {
        return (int) ((y - this.y) * scale);
    }

    public int relSize(double s) {
        return (int) (s * scale);
    }

    public int getWidth() {
        return HMIApplication.window.getWidth();
    }

    public int getHeight() {
        return HMIApplication.window.getHeight();
    }

    public int getScale() {
        return (int) scale;
    }

    public MouseAdapter mouseAdapter(LidarView observer) {
        return new MouseAdapter() {

            private int prevX = 0;
            private int prevY = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                observer.selectPoint(e.getX(), e.getY());
                observer.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                x -= (e.getX() - prevX) / scale;
                y -= (e.getY() - prevY) / scale;
                prevX = e.getX();
                prevY = e.getY();
                observer.repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double prevScale = scale;

                scale -= e.getWheelRotation() * e.getScrollAmount() * Math.exp(scale/500.0);
                if (scale < 16)
                    scale = 16;
                if (scale > 100)
                    scale = 100;

                double dx = getWidth() - scale * (double) getWidth() / prevScale;
                double dy = getHeight() - scale * (double) getHeight() / prevScale;
                x -= (prevX / (double) getWidth()) * (dx / scale);
                y -= (prevY / (double) getHeight()) * (dy / scale);
                observer.repaint();
            }
        };
    }
}
