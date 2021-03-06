/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.detector.subscriber;

import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.api.server.ArrowheadResource;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.Event;
import eu.arrowhead.common.api.ArrowheadConverter;
import eu.arrowhead.lidar.common.DetectionReadout;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("subscriber")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DetectionSubscriberResource extends ArrowheadResource {
    public DetectionSubscriberResource(ArrowheadHttpServer server) throws ArrowheadException {
        super(server);
    }

    @GET
    public Response getIt() {
        return Response.ok().build();
    }

    @POST
    @Path("notify")
    public Response receiveEvent(Event event) {
        DetectionReadout readout = ArrowheadConverter.json().fromString(event.getPayload(), DetectionReadout.class);
        System.out.println("Id: " + readout.getId() + ", isDetected: " + readout.getDetected() + ", time: " + readout.getTime());
        return Response.ok().build();
    }
}
