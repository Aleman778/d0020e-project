package eu.arrowhead.lidar.common;

/**
 * A LiDAR point is one distance measurement including its corresponding angle retrieved
 * from a LiDAR sensor. Combining multiple LiDAR points together forms an area.
 * @see LidarReadout
 */
public class LidarPoint {

    /**
     * The distance measured in meters.
     */
    public double distance;

    /**
     * The angle in radians.
     */
    public double angle;

    /**
     * Default constructor.
     */
    public LidarPoint() {

    }

    /**
     * Constructor.
     * Create a new LidarPoint with a distance and angle.
     * @param distance the distance in meters
     * @param angle the angle in radians
     */
    public LidarPoint(double distance, double angle) {
        this.distance = distance;
        this.angle = angle;
    }

    /**
     * Copy constructor.
     * Create a copy of an existing lidar point.
     * @param copy the lidar point to copy from
     */
    public LidarPoint(LidarPoint copy) {
        this.distance = copy.distance;
        this.angle = copy.angle;
    }
}
