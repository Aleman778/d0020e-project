package eu.arrowhead.detector.provider;

import eu.arrowhead.lidar.common.DetectionReadout;

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
