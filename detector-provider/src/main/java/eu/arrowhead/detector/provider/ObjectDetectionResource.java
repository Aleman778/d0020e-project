package eu.arrowhead.detector.provider;

import eu.arrowhead.lidar.common.DetectionReadout;
import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.api.server.ArrowheadResource;
import eu.arrowhead.common.exception.ArrowheadException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("objectdetect")
@Produces(MediaType.APPLICATION_JSON)

public class ObjectDetectionResource extends ArrowheadResource {

    public ObjectDetectionResource(ArrowheadHttpServer server) throws ArrowheadException {
        super(server);
    }

    @GET
    public Response get() {

        ObjectDetectionMain object = new ObjectDetectionMain();
        DetectionReadout readout = new DetectionReadout(object.detection(),object.getId());
        return Response.status(200).entity(readout).build();
    }
}
