package eu.arrowhead.lidar.common;

import eu.arrowhead.lidar.common.LidarPoint;
import java.util.ArrayList;


public class ObjectDetection {

    private static int numPoints = 40;
    private static int index = 1;
    private static double margin = 1.0;
    private static ArrayList<LidarPoint> calibration = LidarGenerator.generate(numPoints);
    private int id;
    private ArrayList<LidarPoint> data;


    public ObjectDetection(){
        this.data = LidarGenerator.generate(numPoints);
        this.id = index;
        ++index;
    }

    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    boolean detection() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).distance < ((calibration.get(i).distance) - margin)) {
                return true;
            }
        }
        return false;
    }

    int getId(){
        return id;
    }
}
