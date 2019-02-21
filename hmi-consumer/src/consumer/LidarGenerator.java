package consumer;

import javax.swing.*;
import java.util.ArrayList;

public class LidarGenerator {

    public static ArrayList<LidarPoint> generate(int numPoints){
        ArrayList<LidarPoint> data = new ArrayList<>();

        double angle = 0;
        double distance = Math.random() * 3.0 + 1.0;
        double increment = (numPoints - 1) / Math.PI;

        for(int i = 0; i < numPoints; i++){
            data.add(new LidarPoint(distance, angle));
            distance += Math.random() * 0.5 - 0.25;
            if (distance < 0.5)
                distance = 0.0;
            angle += increment;
        }
        return data;
    }


    public static Thread update(ArrayList<LidarPoint> data, JPanel observer) {
        return new Thread(() -> {
            while (HMI.frame.isVisible()) {
                for (LidarPoint p : data) {
                    p.distance += Math.random() * 0.04 - 0.02;
                    if (p.distance < 0.5)
                        p.distance = 0.5;
                }
                observer.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
