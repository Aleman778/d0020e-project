package eu.arrowhead.lidar.consumer;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadConverter;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.HttpClient;
import eu.arrowhead.common.api.clients.OrchestrationStrategy;
import eu.arrowhead.common.api.clients.core.OrchestrationClient;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.common.model.OrchestrationFlags;
import eu.arrowhead.common.model.ServiceMetadata;
import eu.arrowhead.common.model.ServiceRequestForm;
import eu.arrowhead.lidar.common.LidarReadout;

import javax.ws.rs.core.Response;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The HMIConsumer class sets up an Arrowhead Consumer to
 * consume LiDAR data and presents on the {@link LidarView} panel.
 */
public class HMIConsumer extends ArrowheadApplication {

    public static HMIWindow window;

    public HMIConsumer(String[] args) throws ArrowheadException {
        super(args);

        window = new HMIWindow();
        window.setVisible(true);
    }

    @Override
    protected void onStart() throws ArrowheadException {
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext
                .createFromProperties(true);

        final ArrowheadSystem me = ArrowheadSystem.createFromProperties();
        final ServiceRequestForm srf = new ServiceRequestForm.Builder(me)
                .requestedService("temperature", "json", getProps().isSecure())
                .metadata(ServiceMetadata.Keys.UNIT, "meters")
                .flag(OrchestrationFlags.Flags.OVERRIDE_STORE, true)
                .flag(OrchestrationFlags.Flags.PING_PROVIDERS, false)
                .flag(OrchestrationFlags.Flags.METADATA_SEARCH, true)
                .flag(OrchestrationFlags.Flags.ENABLE_INTER_CLOUD, true)
                .build();
        log.info("Service Request payload: " + ArrowheadConverter.json().toString(srf));

        final OrchestrationClient orchestration = OrchestrationClient
                .createFromProperties(securityContext);
        final OrchestrationStrategy strategy = new OrchestrationStrategy
                .Always(orchestration, srf);
        final HttpClient client = new HttpClient(strategy, securityContext);

        Timer timer = new Timer();
        TimerTask authTask = new TimerTask() {
            @Override
            public void run() {
                final Response response = client.request(HttpClient.Method.GET);
                LidarReadout readout = response.readEntity(LidarReadout.class);
                System.out.println(readout);
            }
        };
        timer.schedule(authTask, 2L * 1000L, 8L * 1000L);
    }

    @Override
    protected void onStop() {

    }

    public static void main(String[] args) throws ArrowheadException {
        new HMIConsumer(args).start();
    }
}
