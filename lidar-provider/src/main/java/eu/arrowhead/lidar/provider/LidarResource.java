/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.lidar.provider;


import eu.arrowhead.lidar.common.LidarReadout;
import eu.arrowhead.lidar.common.LidarPoint;


import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Random;
import javax.validation.Valid;
import java.util.ArrayList;

@Path("/")
//REST service example
public class LidarResource {

    static final String SERVICE_URI = "lidar";

    static final LidarReadout readout = new LidarReadout();


    @GET
    @Path(SERVICE_URI)
    public Response getIt(@Context SecurityContext context, @QueryParam("token") String token, @QueryParam("signature") String signature) {
        return Response.status(200).entity(readout).build();
    }

    public static void start() {
        Thread thread = new Thread(() -> {
            try {
                LidarServer server = new LidarServer(null);
                System.out.println("Waiting for connection...");
                server.accept();
                System.out.println("Connected!");
                while (true) {
                    String data = server.readLine();
                    if (data != null && !data.isEmpty()) {
                        String[] dist = data.split(",");
                        ArrayList<LidarPoint> result = new ArrayList<>();

                        int numPoints = dist.length;
                        double angle = 0;
                        double increment = (2.0 * Math.PI) / numPoints;

                        for(int i = 0; i < numPoints; i++){
                            result.add(new LidarPoint(Double.parseDouble(dist[i]), angle));
                            angle += increment;
                            System.out.println(Double.parseDouble(dist[i]));
                        }
                        readout.data = result;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
