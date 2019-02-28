package eu.arrowhead.detector.provider;

import java.util.ArrayList;

public class ObjectDetectionMain {

    private static int numPoints;
    private static double margin;
    private static boolean isDetected;
    private static ArrayList<LidarPoint> calibration;

    //Setup for variables.
    private static void setup() {
        numPoints = 10;
        margin = 1.0;
        isDetected = false;
        calibration = LidarGenerator.generate(numPoints);
    }


    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    private static void detection(ArrayList<LidarPoint> data){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).distance < ((calibration.get(i).distance)-margin)){
                isDetected = true;
                return;
                }
            }
        isDetected = false;
        }
    }

    public static boolean detected(){ return isDetected;}

    public static void main(String args) {
        setup();
        while(true) {
            detection(LidarGenerator.generate(numPoints));
        }
    }

}