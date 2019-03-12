package eu.arrowhead.lidar.common;

import java.time.Instant;
import eu.arrowhead.lidar.common.ObjectDetection;

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
    
    public getId(){
        return this.id;
    }
    
    public getDetected(){
        return this.isDetected;
    }
    
    public getTime(){
        return this.time;
    }
}
