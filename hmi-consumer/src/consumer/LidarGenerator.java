package consumer;

import java.util.ArrayList;

public class LidarGenerator {


    public static ArrayList<LidarPoint> generatePosition(int maxPoints){
        double angleIncrement = maxPoints/(2*Math.PI);
        double angle=0;
        ArrayList<LidarPoint>LidarCoord = new ArrayList<>();
        for(int i = 0; i<maxPoints;i++){
            angle +=angleIncrement;
            LidarCoord.add(new LidarPoint(Math.random()*5, angle));
        }
        return LidarCoord;
    }

}
