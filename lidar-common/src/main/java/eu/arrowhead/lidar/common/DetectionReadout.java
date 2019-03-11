package eu.arrowhead.lidar.common;

import java.time.Instant;

public class DetectionReadout {

    private boolean isDetected;
    private int id;
    private double time;

    public DetectionReadout(boolean isDetected, int id) {

        this.isDetected = isDetected;
        this.id = id;
        this.time = Instant.now().getEpochSecond();
    }
}
