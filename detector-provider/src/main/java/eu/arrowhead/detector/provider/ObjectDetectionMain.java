package provider;

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
        calibration = generate(numPoints);
    }


    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    private static void detection(ArrayList<LidarPoint> data){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).distance < (calibration.distance-margin)){
                isDetected = true;
                break;
                }
            }
        isDetected = false;
        }
    }

    public static boolean detected(){ return isdetected;}

    public static void main(String args) {
        setup();
        while(true) {
            detection(generate(numPoints));
        }
    }

}