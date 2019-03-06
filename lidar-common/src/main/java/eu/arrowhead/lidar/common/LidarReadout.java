package eu.arrowhead.lidar.common;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Lidar Readout
 */
public class LidarReadout {

    /**
     * The name of the LiDAR Readout.
     */
    public String name;

    /**
     * The time of reading the LiDAR data.
     */
    public double time;

    /**
     * The unit of the angle data.
     * Default is in radians.
     */
    public String unitAngle;

    /**
     * The unit of the distance data.
     * Default is in meters.
     */
    public String unitDist;

    /**
     * The list of LiDAR points.
     */
    public List<LidarPoint> data;

    /**
     * Constructor.
     * Creates an empty LiDAR readout without any specified information.
     */
    public LidarReadout() {
        data = new ArrayList<LidarPoint>();
    }

    /**
     * Constructor.
     * Creates a LiDAR readout with a specific name.
     * @param name the name of the readout
     */
    public LidarReadout(String name) {
        this(name, Instant.now().getEpochSecond(), "radians", "meters", new ArrayList<LidarPoint>());
    }

    /**
     * Constructor.
     * Creates a LiDAR readout with a specific name, and units.
     * @param name the name of the readout
     * @param unitAngle the unit of the angle
     * @param unitDist the unit of the distance
     */
    public LidarReadout(String name, String unitAngle, String unitDist) {
        this(name, Instant.now().getEpochSecond(), unitAngle, unitDist, new ArrayList<LidarPoint>());
    }

    /**
     * Constructor.
     * Creates a LiDAR readout with full specifications.
     * @param name the name of the readout
     * @param time the current time when reading
     * @param unitAngle the unit of the angle
     * @param unitDist the unit of the distance
     * @param data list of lidar data
     */
    public LidarReadout(String name, double time, String unitAngle, String unitDist, List<LidarPoint> data) {
        this.name = name;
        this.time = time;
        this.unitAngle = unitAngle;
        this.unitDist = unitDist;
        this.data = data;
    }

    /**
     * Add LiDAR data to the readout.
     * @param point the lidar point to add
     */
    public void add(LidarPoint point) {
        data.add(point);
    }

    /**
     * Remove LiDAR data from the readout.
     * @param point the lidar point to remove
     * @return true if successfull, otherwise false
     */
    public boolean remove(LidarPoint point) {
        return data.remove(point);
    }

    @Override
    public String toString() {
        return "Lidar readout[name=" + name + ", time=" + time + ", unitAngle=" + unitAngle +
                ", unitDist=" + unitDist + "] has " + data.size() + " lidar points.";
    }
}
