/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.detector.subscriber;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.api.ArrowheadSecurityContext;
import eu.arrowhead.common.api.clients.core.EventHandlerClient;
import eu.arrowhead.common.api.server.ArrowheadGrizzlyHttpServer;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.ArrowheadSystem;

public class DetectionSubscriberMain extends ArrowheadApplication {
    public static void main(String[] args) throws ArrowheadException {
        new DetectionSubscriberMain(args).start();
    }

    private DetectionSubscriberMain(String[] args) throws ArrowheadException {
        super(args);
    }

    @Override
    protected void onStart() throws ArrowheadException {
        final ArrowheadSecurityContext securityContext = ArrowheadSecurityContext.createFromProperties(true);
        final ArrowheadHttpServer server = ArrowheadGrizzlyHttpServer
                .createFromProperties(securityContext)
                .addResources(DetectionSubscriberResource.class)
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
