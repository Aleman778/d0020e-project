package consumer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Viewport {

    public double x, y;
    public double scale;
    public double panSpeed = 0.01;

    public Viewport() {
        x = -HMI.frame.getWidth() / 128.0;
        y = -HMI.frame.getHeight() / 128.0;
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
        return HMI.frame.getWidth();
    }

    public int getHeight() {
        return HMI.frame.getHeight();
    }

    public int getScale() {
        return (int) scale;
    }

    public MouseAdapter mouseAdapter(JPanel observer) {
        return new MouseAdapter() {

            private int prevX = 0;
            private int prevY = 0;

            @Override
            public void mouseMoved(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                x -= panSpeed * (e.getX() - prevX);
                y -= panSpeed * (e.getY() - prevY);
                prevX = e.getX();
                prevY = e.getY();
                observer.repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double prevScale = scale;

                scale -= e.getWheelRotation() * e.getScrollAmount() * Math.exp(scale/1000.0);
                if (scale < 8)
                    scale = 8;
                if (scale > 200)
                    scale = 200;

                double displacementX = getWidth() - scale * (double) getWidth() / prevScale;
                double displacementY = getHeight() - scale * (double) getHeight() / prevScale;
                x -= (prevX / (double) getWidth()) * (displacementX / prevScale);
                y -= (prevY / (double) getHeight()) * (displacementY / prevScale);
                System.out.println(displacementX);
                System.out.println((prevX / (double) getWidth()) * (displacementX / prevScale));
                observer.repaint();
            }
        };
    }
}
