package provider;

import java.util.ArrayList;
public class ObjectDetectionMain {

    private int numPoints;
    private double margin;
    private boolean objectDetected;
    private ArrayList<LidarPoint> calibration;

    //Setup for variables.
    private static void setup() {
        numPoints = 10;
        margin = 1.0;
        objectDetected = false;
        calibration = generate(numPoints);
    }


    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    private static void detection(ArrayList<LidarPoint> data){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i) < (calibration-margin) && !objectDetected){
                objectDetected = true;
                break;
            } else {
                if(data.get(i) > (calibration-margin) && objectDetected)
                objectDetected = false;
                }
            }
        }
    }

    //Returns current value of objectDetected
    public static boolean detected(){
        return objectDetected;
    }

    public static void main(String args) {
        setup();
        while(true) {
            detection(generate(numPoints));
        }
    }

}