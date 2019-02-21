package consumer;

import javax.swing.*;
import java.awt.*;

public class LidarView extends JPanel {

    public LidarView() {
        setName("LidarView");
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        int width=64;
        int height=48;
        for(int x=0;x<10;x++) {
            for(int y=0;y<10;y++) {
                g.drawRect(x*width,y*height,width,height);
            }
        }
    }
}
