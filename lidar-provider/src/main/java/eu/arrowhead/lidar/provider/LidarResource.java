/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.lidar.provider;


import javax.ws.rs.POST;
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

@Path("/")
//REST service example
public class LidarResource {

    static final String SERVICE_URI = "lidar";

    private static LidarServer server;

    @POST
    @Path(SERVICE_URI)
    @Consumes(MediaType.APPLICATION_JSON)
    public void postPos(@Valid int pos,@Context SecurityContext context, @QueryParam("token") String token, @QueryParam("signature") String signature) {

        String providerName;

        if (context.isSecure()) {
            RequestVerification.verifyRequester(context, token, signature);
            providerName = "SecureLidar";
        }
        else {
            providerName = "InsecureLidar";
        }
    }

    public static void start() {
        try {
            server = new LidarServer(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread serverThread = new Thread(() -> {
            try {
                server.listen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }
}
