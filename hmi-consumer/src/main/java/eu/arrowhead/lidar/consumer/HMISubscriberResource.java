package eu.arrowhead.lidar.consumer;

import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.api.server.ArrowheadResource;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("subscriber")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HMISubscriberResource extends ArrowheadResource {

    public HMISubscriberResource(ArrowheadHttpServer server) throws ArrowheadException {
        super(server);
    }

    @GET
    public Response getIt() {
        return Response.ok().build();
    }

    @POST
    @Path("notify")
    public Response receiveEvent(Event event) {
        log.info("Received new event: " + event.toString());
        return Response.ok().build();
    }
}
