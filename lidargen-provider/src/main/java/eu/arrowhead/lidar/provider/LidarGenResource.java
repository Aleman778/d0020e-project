package eu.arrowhead.lidar.provider;

import eu.arrowhead.client.common.model.MeasurementEntry;
import eu.arrowhead.client.common.model.TemperatureReadout;
import eu.arrowhead.lidar.common.LidarPoint;
import eu.arrowhead.lidar.common.LidarReadout;

import javax.swing.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class LidarGenResource {

    static final String SERVICE_URI = "lidargen";

    static final ArrayList<LidarPoint> data = generate(40);

    @GET
    @Path(SERVICE_URI)
    public Response getIt() {
        if (LidarGenProvider.customResponsePayload != null) {
            return Response.status(200).entity(LidarGenProvider.customResponsePayload).build();
        } else {
            update();
            LidarReadout readout = new LidarReadout("Faker_LidarGenerator");
            readout.data = data;
            return Response.status(200).entity(readout).build();
        }
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
