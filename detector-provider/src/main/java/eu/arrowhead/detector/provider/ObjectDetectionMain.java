package eu.arrowhead.detector.provider;

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

    private static int numPoints;
    private static double margin;
    private static ArrayList<LidarPoint> calibration;

    public ObjectDetectionMain(String[] args) throws ArrowheadException {
        super(args);
    }

    //Setup for variables.
    private static void setup() {
        numPoints = 10;
        margin = 1.0;
        calibration = LidarGenerator.generate(numPoints);
    }

    protected void onStart() throws ArrowheadException {
        final ArrowheadProperties props = getProps();
        if (props.getBooleanProperty("payload_from_file", false)) {
            customResponsePayload = props.getProperty("custom_payload");
        }
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext.createFromProperties(true);
        final ArrowheadHttpServer server = ArrowheadGrizzlyHttpServer
                .createFromProperties(securityContext)
                .addResources(LidarGenerator.class, LidarPoint.class)
                .addPackages("eu.arrowhead.demo", "eu.arrowhead.demo.provider.filter")
                .start();

        final ServiceRegistryClient registry = ServiceRegistryClient.createFromProperties(securityContext);
        registry.register(ServiceRegistryEntry.createFromProperties(server));
    }

    protected void onStop() {

    }


    //Checks if a datapoint in a LidarPoint list is less than the calibrated value for that datapoint
    private static void detection(ArrayList<LidarPoint> data) {
        String lidarData = "{\n\t\"LidarData\": {\n";
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).distance < ((calibration.get(i).distance) - margin)) {
                lidarData = lidarData + "\t\t\"Datapoint " + (i+1) + "\": {\n\t\t\t\"Angle\": \"" + data.get(i).angle + "\"\n\t\t\t\"Detected\": \"TRUE\"\n\t\t}";
            } else {
                lidarData = lidarData + "\t\t\"Datapoint " + (i+1) + "\": {\n\t\t\t\"Angle\": \"" + data.get(i).angle + "\"\n\t\t\t\"Detected\": \"FALSE\"\n\t\t}";
            }
            if((i+1) < data.size()){
                lidarData = lidarData + ",\n";
            }
        }
        lidarData = lidarData + "\n\t}\n}";
        System.out.println(lidarData);
    }

    public static void main(String[] args) {
        setup();
        new ObjectDetectionMain(args).start();
    }

}