package eu.arrowhead.detector.provider;

import java.util.ArrayList;

public class ObjectDetectionMain {

    private static int numPoints;
    private static double margin;
    private static ArrayList<LidarPoint> calibration;

    //Setup for variables.
    private static void setup() {
        numPoints = 10;
        margin = 1.0;
        calibration = LidarGenerator.generate(numPoints);
    }


    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    private static void detection(ArrayList<LidarPoint> data) {
        String lidarData = "{\n\t\"LidarData\": {\n";
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).distance < ((calibration.get(i).distance) - margin)) {
                lidarData = lidarData + "\t\t\"Datapoint " + data.get(i).angle + "\": \"TRUE\"";
            } else {
                lidarData = lidarData + "\t\t\"Datapoint " + data.get(i).angle + "\": \"FALSE\"";
            }
            if((i+1) < data.size()){
                lidarData = lidarData + ",\n";
            }
        }
        lidarData = lidarData + "\t}\n}";
        System.out.println(lidarData);
    }

    public static void main(String args) {
        setup();
        while(true) {
            detection(LidarGenerator.generate(numPoints));
        }
    }

}