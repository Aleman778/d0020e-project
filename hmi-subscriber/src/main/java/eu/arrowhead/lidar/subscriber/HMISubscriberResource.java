package eu.arrowhead.lidar.subscriber;

import eu.arrowhead.common.api.ArrowheadConverter;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.api.server.ArrowheadResource;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.model.Event;
import eu.arrowhead.lidar.common.LidarReadout;
import eu.arrowhead.lidar.common.hmi.HMIApplication;

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
        LidarReadout readout = ArrowheadConverter.json().fromString(event.getPayload(), LidarReadout.class);
        HMIApplication.window.callback(readout.data);
        return Response.ok().build();
    }
}
