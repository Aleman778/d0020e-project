package eu.arrowhead.lidar.provider;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.core.ServiceRegistryClient;
import eu.arrowhead.common.api.server.ArrowheadGrizzlyHttpServer;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.ServiceRegistryEntry;

public class LidarGenProvider extends ArrowheadApplication {

    public LidarGenProvider(String[] args) throws ArrowheadException {
        super(args);
    }

    @Override
    protected void onStart() throws ArrowheadException {
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext
                .createFromProperties();
        final ArrowheadHttpServer server = ArrowheadGrizzlyHttpServer
                .createFromProperties(securityContext)
                .addResources(LidarGenResource.class)
                .addPackages("eu.arrowhead.lidar.provider")
                .start();

        final ServiceRegistryClient registry = ServiceRegistryClient
                .createFromProperties(securityContext);
        registry.register(ServiceRegistryEntry.createFromProperties(server));
    }

    @Override
    protected void onStop() {

    }

    public static void main(String[] args) throws ArrowheadException {
        new LidarGenProvider(args).start();
    }
}