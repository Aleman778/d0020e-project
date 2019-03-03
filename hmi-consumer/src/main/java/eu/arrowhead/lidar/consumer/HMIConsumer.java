package eu.arrowhead.lidar.consumer;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.misc.TypeSafeProperties;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.ServiceRequestForm;
import eu.arrowhead.lidar.common.LidarPoint;
import eu.arrowhead.lidar.common.LidarReadout;
import eu.arrowhead.lidar.common.ServiceUtility;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The HMIConsumer class sets up an Arrowhead Consumer to
 * consume LiDAR data and presents on the {@link LidarView} panel.
 */
public class HMIConsumer {

    private static boolean isSecure;
    private static String orchestratorUrl;
    private static String providerUrl;
    private static TypeSafeProperties props = Utility.getProp();
    private static ServiceRequestForm srf;
    private static final String consumerSystemName = props.getProperty("consumer_system_name");

    public static HMIWindow hmi;

    /**
     * Private constructor.
     * @param args the program arguments
     */
    private HMIConsumer(String[] args) {
        isSecure = ServiceUtility.checkSecureMode(args);
        orchestratorUrl = ServiceUtility.getOrchestratorUrl(props, isSecure);
        srf = compileSRF();
        providerUrl = ServiceUtility.sendOrchestrationRequest(srf, orchestratorUrl);
        hmi = new HMIWindow();
        hmi.setVisible(true);
    }

    /**
     * HMI consumer entry point.
     * @param args the program arguments
     */
    public static void main(String[] args) {
        new HMIConsumer(args);
    }

    public static void findService() {

    }

    public static List<LidarPoint> consumeService() {
        Response getResponse = Utility.sendRequest(providerUrl, "GET", null);
        LidarReadout readout = new LidarReadout();
        try {
            readout = getResponse.readEntity(LidarReadout.class);
            System.out.println("Provider Response payload: " + Utility.toPrettyJson(null, readout));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("Provider did not send the LiDAR readout in the correct format.");
        }
        return readout.data;
    }

    /**
     * Compile service request form.
     * @return a service request form.
     */
    private ServiceRequestForm compileSRF() {
        ArrowheadSystem consumer = new ArrowheadSystem(consumerSystemName, "localhost", 8080, "null");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("distanceUnit", "meters");
        metadata.put("angleUnit", "radians");
        if (isSecure) {
            metadata.put("security", "token");
        }
        ArrowheadService service = new ArrowheadService("LidarGenerator", Collections.singleton("json"), metadata);

        Map<String, Boolean> orchestrationFlags = new HashMap<>();
        orchestrationFlags.put("overrideStore", true);
        orchestrationFlags.put("pingProviders", false);
        orchestrationFlags.put("metadataSearch", true);
        orchestrationFlags.put("enableInterCloud", true);

        ServiceRequestForm srf = new ServiceRequestForm.Builder(consumer).requestedService(service).orchestrationFlags(orchestrationFlags).build();
        System.out.println("Service Request payload: " + Utility.toPrettyJson(null, srf));
        return srf;
    }
}
