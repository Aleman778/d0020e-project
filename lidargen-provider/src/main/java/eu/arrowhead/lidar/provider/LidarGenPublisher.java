package eu.arrowhead.lidar.provider;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.core.EventHandlerClient;
import eu.arrowhead.common.api.server.ArrowheadGrizzlyHttpServer;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.misc.ArrowheadProperties;
import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.common.model.Event;
import eu.arrowhead.lidar.common.LidarGenerator;
import eu.arrowhead.lidar.common.LidarPoint;
import eu.arrowhead.lidar.common.LidarReadout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LidarGenPublisher extends ArrowheadApplication {

    private static ArrayList<LidarPoint> data;

    public static void main(String[] args) throws ArrowheadException {
        new LidarGenPublisher(args).start();
    }

    private LidarGenPublisher(String[] args) throws ArrowheadException {
        super(args);
    }

    @Override
    protected void onStart() throws ArrowheadException {
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext.createFromProperties(true);
        final ArrowheadHttpServer server = ArrowheadGrizzlyHttpServer
                .createFromProperties(securityContext)
                .addResources(LidarGenPublisherResource.class)
                .setSecurityFilter(null)
                .start();

        final ArrowheadSystem me = ArrowheadSystem.createFromProperties(server);
        final EventHandlerClient eventHandler = EventHandlerClient.createFromProperties(securityContext);
        final ArrowheadProperties props = getProps();
        final String eventType = props.getEventType();
        final boolean secure = props.isSecure();

        data = LidarGenerator.generate(40);

        Timer timer = new Timer();
        TimerTask authTask = new TimerTask() {
            @Override
            public void run() {
                LidarReadout readout = new LidarReadout("LidarGenerator");
                readout.data = data;
                eventHandler.publish(new Event(eventType, readout), me);
                LidarGenerator.update(data);
            }
        };
        timer.schedule(authTask, 2L * 1000L, 8L * 1000L);
    }

    @Override
    protected void onStop() {

    }

}
