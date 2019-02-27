package provider;

import java.awt.*;

public class LidarPoint {

    public double distance;
    public double angle;

    public LidarPoint(double distance, double angle) {
        this.distance = distance;
        this.angle = angle;
    }

    public LidarPoint(LidarPoint copy) {
        this.distance = copy.distance;
        this.angle = copy.angle;
    }

    public Rectangle getBounds(Viewport viewport) {
        Rectangle bounds = new Rectangle();
        int diameter = viewport.relSize(0.1);
        bounds.x = viewport.relX(distance * Math.cos(angle)) - (int) diameter/2;
        bounds.y = viewport.relY(distance * Math.sin(angle)) - (int) diameter/2;
        bounds.width = (int) diameter; bounds.height = (int) diameter;
        return bounds;
    }
}
