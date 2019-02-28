package eu.arrowhead.detector.provider;

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
}
