package eu.arrowhead.detector.provider;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.core.ServiceRegistryClient;
import eu.arrowhead.common.api.server.ArrowheadGrizzlyHttpServer;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.misc.ArrowheadProperties;
import eu.arrowhead.common.model.ServiceRegistryEntry;

public class ObjectDetectionProvider extends ArrowheadApplication{

    public ObjectDetectionProvider(String[] args) throws ArrowheadException {
        super(args);
    }

    protected void onStart() throws ArrowheadException {
        final ArrowheadProperties props = getProps();
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

    public static void main(String[] args) throws ArrowheadException{
        new ObjectDetectionProvider(args).start();
    }
}
