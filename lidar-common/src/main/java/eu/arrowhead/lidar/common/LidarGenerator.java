package eu.arrowhead.lidar.common;

import java.util.ArrayList;

public class LidarGenerator {

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
}
