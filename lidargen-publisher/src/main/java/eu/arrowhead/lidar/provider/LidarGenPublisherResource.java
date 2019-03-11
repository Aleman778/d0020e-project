package eu.arrowhead.lidar.provider;

import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.exception.ArrowheadException;

import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.api.server.ArrowheadResource;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.lidar.common.LidarPoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Map;

@Path("publisher")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LidarGenPublisherResource extends ArrowheadResource {

    public LidarGenPublisherResource(ArrowheadHttpServer server) throws ArrowheadException {
        super(server);
    }

    @GET
    public Response getIt() {
        return Response.ok().build();
    }

    @POST
    @Path("feedback")
    public Response receiveEvent(Map<String, Boolean> results) {
        return Response.ok().build();
    }

}
