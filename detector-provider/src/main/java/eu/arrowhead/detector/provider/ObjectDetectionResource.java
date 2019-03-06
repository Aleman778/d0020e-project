package eu.arrowhead.detector.provider;

@Path("objectdetect")
@Produces(MediaType.APPLICATION_JSON)

public class ObjectDetectionResource extends ArrowheadResource {

    public ObjectDetectionResource(ArrowheadHttpServer server) throws ArrowheadException {
        super(server);
    }

    @GET
    public Response get() {
        ObjectDetectionMain readout = new ObjectDetectionMain().detection();
        return Response.status(200).entity(readout).build();
    }
}
