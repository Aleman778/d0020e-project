package eu.arrowhead.lidar.consumer;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.core.EventHandlerClient;
import eu.arrowhead.common.api.server.ArrowheadGrizzlyHttpServer;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.ArrowheadSystem;

public class HMISubscriber extends ArrowheadApplication {

    public static void main(String[] args) throws ArrowheadException {
        new HMISubscriber(args).start();
    }

    private HMISubscriber(String[] args) throws ArrowheadException {
        super(args);

        HMIConsumer.window = new HMIWindow();
        HMIConsumer.window.setVisible(true);
    }

    @Override
    protected void onStart() throws ArrowheadException {
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext.createFromProperties(true);
        final ArrowheadHttpServer server = ArrowheadGrizzlyHttpServer
                .createFromProperties(securityContext)
                .addResources(HMISubscriberResource.class)
                .setSecurityFilter(null)
                .start();

        final ArrowheadSystem me = ArrowheadSystem.createFromProperties(server);

        final EventHandlerClient eventHandler = EventHandlerClient.createFromProperties(securityContext);
        eventHandler.subscribe(getProps().getEventType(), me);
    }

    @Override
    protected void onStop() {

    }
}
