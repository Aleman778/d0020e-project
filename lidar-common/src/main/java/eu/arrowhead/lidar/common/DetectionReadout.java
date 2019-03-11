package eu.arrowhead.lidar.common;

import java.time.Instant;

public class DetectionReadout {

    private boolean isDetected;
    private int id;
    private double time;

    public DetectionReadout() {

        ObjectDetection object = new ObjectDetection();
        this.isDetected = object.detection();
        this.id = object.getId();
        this.time = Instant.now().getEpochSecond();
    }

    public int getId(){
        return this.id;
    }

    public boolean getDetected(){
        return this.isDetected;
    }

    public double getTime(){
        return this.time;
    }
}
