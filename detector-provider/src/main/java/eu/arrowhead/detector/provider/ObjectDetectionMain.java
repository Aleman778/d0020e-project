package eu.arrowhead.detector.provider;

import eu.arrowhead.lidar.common.DetectionReadout;
import eu.arrowhead.lidar.common.LidarPoint;
import java.util.ArrayList;
import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.core.ServiceRegistryClient;
import eu.arrowhead.common.api.server.ArrowheadGrizzlyHttpServer;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.misc.ArrowheadProperties;
import eu.arrowhead.common.model.ServiceRegistryEntry;


public class ObjectDetectionMain extends ArrowheadApplication {

    private static int numPoints = 10;
    private static double margin = 1.0;
    private static ArrayList<LidarPoint> calibration = LidarGenerator.generate(numPoints);
    private ArrayList<LidarPoint> data;

    public ObjectDetectionMain(String[] args) throws ArrowheadException {
        super(args);
    }

    public ObjectDetectionMain(){
        this.data = LidarGenerator.generate(numPoints);
    }

    protected void onStart() throws ArrowheadException {
        final ArrowheadProperties props = getProps();
        if (props.getBooleanProperty("payload_from_file", false)) {
            customResponsePayload = props.getProperty("custom_payload");
        }
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext.createFromProperties(true);
        final ArrowheadHttpServer server = ArrowheadGrizzlyHttpServer
                .createFromProperties(securityContext)
                .addResources(ObjectDetectionResource.class)
                .addPackages("eu.arrowhead.demo", "eu.arrowhead.demo.provider.filter")
                .start();

        final ServiceRegistryClient registry = ServiceRegistryClient.createFromProperties(securityContext);
        registry.register(ServiceRegistryEntry.createFromProperties(server));
    }

    protected void onStop() {

    }


    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    boolean detection() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).distance < ((calibration.get(i).distance) - margin)) {
                return(true);
            }
        }
        return(false);
    }

    public static void main(String[] args) throws ArrowheadException{
        new ObjectDetectionMain(args).start();
    }

}