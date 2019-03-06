package eu.arrowhead.lidar.provider;

import eu.arrowhead.common.api.server.ArrowheadHttpServer;
import eu.arrowhead.common.api.server.ArrowheadResource;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.lidar.common.LidarPoint;
import eu.arrowhead.lidar.common.LidarReadout;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("lidargen")
@Produces(MediaType.APPLICATION_JSON)
public class LidarGenResource extends ArrowheadResource {

    static final ArrayList<LidarPoint> data = generate(40);

    public LidarGenResource(ArrowheadHttpServer server) throws ArrowheadException {
        super(server);
    }

    public Response get() {
        update();
        LidarReadout readout = new LidarReadout("Faker_LidarGenerator");
        readout.data = data;
        return Response.status(200).entity(readout).build();
    }

    public static ArrayList<LidarPoint> generate(int numPoints){
        ArrayList<LidarPoint> data = new ArrayList<>();

        double angle = 0;
        double distance = Math.random() * 3.0 + 1.0;
        double increment = (2.0 * Math.PI) / numPoints;

        for(int i = 0; i < numPoints; i++){
            data.add(new LidarPoint(distance, angle));
            distance += Math.random() * 0.5 - 0.25;
            if (distance < 0.5)
                distance = 0.0;
            angle += increment;
        }
        return data;
    }


    public static void update() {
        for (LidarPoint p : data) {
            p.distance += Math.random() * 0.04 - 0.02;
            if (p.distance < 0.5)
                p.distance = 0.5;
        }
    }
}
