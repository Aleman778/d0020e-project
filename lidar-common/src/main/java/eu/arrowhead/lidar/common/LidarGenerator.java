package eu.arrowhead.lidar.common;

import java.util.ArrayList;

/**
 * Generate Fake LiDAR data using the {@link LidarGenerator#generate(int)} function.
 * To update existing data use the {@link LidarGenerator#update(ArrayList)} function.
 */
public class LidarGenerator {

    /**
     * Generates new Fake LiDAR points.
     * @param numPoints the number of LiDAR points to generate
     * @return a new ArrayList containing all the generated LiDAR points
     */
    public static ArrayList<LidarPoint> generate(int numPoints){
        ArrayList<LidarPoint> data = new ArrayList<>();

        double angle = 0;
        double distance = Math.random() * 3.0 + 1.0;
        double increment = (2.0 * Math.PI) / numPoints;

        for(int i = 0; i < numPoints; i++){
            data.add(new LidarPoint(distance, angle));
            distance += Math.random() * 0.5 - 0.25;
            if (distance < 0.5)
                distance = 0.0;
            angle += increment;
        }
        return data;
    }

    /**
     * Update existing LiDAR points to give a somewhat realistic but random behaviour.
     * @param data the ArrayList of LiDAR points to update
     */
    public static void update(ArrayList<LidarPoint> data) {
        for (LidarPoint p : data) {
            p.distance += Math.random() * 0.04 - 0.02;
            if (p.distance < 0.5)
                p.distance = 0.5;
        }
    }
}
