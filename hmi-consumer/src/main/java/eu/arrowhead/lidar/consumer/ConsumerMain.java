/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.lidar.consumer;

import eu.arrowhead.client.common.CertificateBootstrapper;
import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.misc.ClientType;
import eu.arrowhead.client.common.misc.TypeSafeProperties;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRequestForm;
import eu.arrowhead.client.common.model.TemperatureReadout;
import eu.arrowhead.lidar.common.LidarPoint;
import eu.arrowhead.lidar.common.LidarReadout;
import java.util.ArrayList;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLContextConfigurator.GenericStoreException;

public class ConsumerMain {

private static boolean isSecure;
private static String orchestratorUrl;
private static TypeSafeProperties props = Utility.getProp();
private static final String consumerSystemName = "LidarSensor";
private boolean isOpen = false;

public static HMIWindow window;


  private ConsumerMain(String[] args) {

    HMIThemeManager.setTheme("dark");

    window = new HMIWindow();
    window.setVisible(true);

    //Prints the working directory for extra information. Working directory should always contain a config folder with the app.conf file!
    System.out.println("Working directory: " + System.getProperty("user.dir"));

    //Compile the URL for the orchestration request.
    getOrchestratorUrl(args);

    //Start a timer, to measure the speed of the Core Systems and the provider application system.
    //long startTime = System.currentTimeMillis();

    //Compile the payload, that needs to be sent to the Orchestrator - THIS METHOD SHOULD BE MODIFIED ACCORDING TO YOUR NEEDS
    ServiceRequestForm lidarSRF = compileSRF("LidarProvider");

    //Sending the orchestration request and parsing the response
    String lidarUrl;
    
    /*
     * This piece of code below might be tricky to understand, and might also look pretty dumb. But what we actually wanted to accomplish with this
     * is that the consumer should not crash with error messages, should the consumer start before the providers when we run the startUp.sh script.
     * This works because when the consumer are trying to send an orchestration request to a non-started provider, an exception will occur. When we
     * catch that exception, we will give the providers another 2000ms (2 seconds) to start up, and this process will be repeated over and over again
     * until the providers have started up successfully, because then we will reach the 'break;' statement that exits the loop
    */
    while(true){
      try{
        lidarUrl = sendOrchestrationRequest(lidarSRF);
        break;  //When reached, the providers have been started and the loop will be exited.
      }catch(Exception e){
        try{
          Thread.sleep(2000);
        }catch(Exception ex){
          ex.printStackTrace();
        }
      }
      
    }



    //Printing out the elapsed time during the orchestration and service consumption
    //long endTime = System.currentTimeMillis();
    //System.out.println("Orchestration and Service consumption response time: " + Long.toString(endTime - startTime));


    //Show a message dialog with the response from the service provider
    //JLabel label = new JLabel("The indoor temperature is " + temperature + " degrees celsius.");
    //label.setFont(new Font("Arial", Font.BOLD, 18));
    //JOptionPane.showMessageDialog(null, label, "Provider Response", JOptionPane.INFORMATION_MESSAGE);
  }

    
    public static void main(String[] args) {
        new ConsumerMain(args);
    }
    
    //Compiles the payload for the orchestration request
    private ServiceRequestForm compileSRF(String service_name) {
        
        /*
          ArrowheadSystem: systemName, (address, port, authenticationInfo)
          Since this Consumer skeleton will not receive HTTP requests (does not provide any services on its own),
          the address, port and authenticationInfo fields can be set to anything.
          SystemName can be an arbitrarily chosen name, which makes sense for the use case.
        */
        
        ArrowheadSystem consumer = new ArrowheadSystem(consumerSystemName, "localhost", 8080, "null");
        
        //You can put any additional metadata you look for in a Service here (key-value pairs)
        Map<String, String> metadata = new HashMap<>();
        
        if(service_name.equals("LidarSensor")) {        
	  metadata.put("distanceUnit", "meters");
	  metadata.put("angleUnit", "radians");
        }
        if (isSecure) {
          //This is a mandatory metadata when using TLS, do not delete it
          metadata.put("security", "token");
        }
        
        /*
          ArrowheadService: serviceDefinition (name), interfaces, metadata
          Interfaces: supported message formats (e.g. JSON, XML, JSON-SenML), a potential provider has to have at least 1 match,
          so the communication between consumer and provider can be facilitated.
        */
        
        ArrowheadService service = new ArrowheadService("LidarGenerator", Collections.singleton("JSON"), metadata);
        
        //Some of the orchestrationFlags the consumer can use, to influence the orchestration process
        Map<String, Boolean> orchestrationFlags = new HashMap<>();
        
        //When true, the orchestration store will not be queried for "hard coded" consumer-provider connections
        orchestrationFlags.put("overrideStore", true);
        
        //When true, the Service Registry will ping every potential provider, to see if they are alive/available on the network
        orchestrationFlags.put("pingProviders", false);
        
        //When true, the Service Registry will only providers with the same exact metadata map as the consumer
        orchestrationFlags.put("metadataSearch", false);
        
        //When true, the Orchestrator can turn to the Gatekeeper to initiate interCloud orchestration, if the Local Cloud had no adequate provider
        orchestrationFlags.put("enableInterCloud", false);
        
        //Build the complete service request form from the pieces, and return it
        ServiceRequestForm srf = new ServiceRequestForm.Builder(consumer).requestedService(service).orchestrationFlags(orchestrationFlags).build();
        System.out.println("Service Request payload: " + Utility.toPrettyJson(null, srf));
        return srf;
    }
    
    private ArrayList<LidarPoint> consumeService(String providerUrl) {
        /*
          Sending request to the provider, to the acquired URL. The method type and payload should be known beforehand.
          If needed, compile the request payload here, before sending the request.
          Supported method types at the moment: GET, POST, PUT, DELETE
        */
        Response getResponse = Utility.sendRequest(providerUrl, "GET", null);
        
        /*
          Parsing the response from the provider here. This code prints an error message, if the answer is not in the expected JSON format, but custom
          error handling can also be implemented here. For example the Orchestrator will send back a JSON with the structure of the eu.arrowhead.client
          .common.exception.ErrorMessage class, and the errors from the Orchestrator are parsed this way.
        */
        
        LidarReadout readout = new LidarReadout();
        
        try {
            readout = getResponse.readEntity(LidarReadout.class);
            System.out.println("Provider Response payload: " + Utility.toPrettyJson(null, readout));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("Provider did not send the LiDAR readout in the correct format.");
        }
        return readout.data;
}
    
    
     /*
        Methods that should be modified to your use case ↑
     ----------------------------------------------------------------------------------------------------------------------------------
        Methods that do not need to be modified ↓
     */
    
    //DO NOT MODIFY - Gets the correct URL where the orchestration requests needs to be sent (from app.conf config file + command line argument)
    private void getOrchestratorUrl(String[] args) {
        String orchAddress = props.getProperty("orch_address", "0.0.0.0");
        int orchInsecurePort = props.getIntProperty("orch_insecure_port", 8440);
        int orchSecurePort = props.getIntProperty("orch_secure_port", 8441);
        
        for (String arg : args) {
            if (arg.equals("-tls")) {
                isSecure = true;
                SSLContextConfigurator sslCon = new SSLContextConfigurator();
                sslCon.setKeyStoreFile(props.getProperty("keystore"));
                sslCon.setKeyStorePass(props.getProperty("keystorepass"));
                sslCon.setKeyPass(props.getProperty("keypass"));
                sslCon.setTrustStoreFile(props.getProperty("truststore"));
                sslCon.setTrustStorePass(props.getProperty("truststorepass"));
        
                try {
                    SSLContext sslContext = sslCon.createSSLContext(true);
                    Utility.setSSLContext(sslContext);
                } 
                catch (GenericStoreException e) {
                    System.out.println("Provided SSLContext is not valid, moving to certificate bootstrapping.");
                    e.printStackTrace();
                    sslCon = CertificateBootstrapper.bootstrap(ClientType.CONSUMER, consumerSystemName);
                    props = Utility.getProp();
                    Utility.setSSLContext(sslCon.createSSLContext(true));
                }
            break;
            }
        }
        
        if (isSecure) {
            Utility.checkProperties(props.stringPropertyNames(), ClientType.CONSUMER.getSecureMandatoryFields());
            orchestratorUrl = Utility.getUri(orchAddress, orchSecurePort, "orchestrator/orchestration", true, false);
        } 
        else {
            orchestratorUrl = Utility.getUri(orchAddress, orchInsecurePort, "orchestrator/orchestration", false, false);
        }
    }
    
    /* NO NEED TO MODIFY (for basic functionality)
       Sends the orchestration request to the Orchestrator, and compiles the URL for the first provider received from the OrchestrationResponse */
    
    private String sendOrchestrationRequest(ServiceRequestForm srf) {
          
        //Sending a POST request to the orchestrator (URL, method, payload)
        Response postResponse = Utility.sendRequest(orchestratorUrl, "POST", srf);
        
        //Parsing the orchestrator response
        OrchestrationResponse orchResponse = postResponse.readEntity(OrchestrationResponse.class);
        System.out.println("Orchestration Response payload: " + Utility.toPrettyJson(null, orchResponse));
        if (orchResponse.getResponse().isEmpty()) {
            throw new ArrowheadException("Orchestrator returned with 0 Orchestration Forms!");
        }
        
        //Getting the first provider from the response
        ArrowheadSystem provider = orchResponse.getResponse().get(0).getProvider();
        String serviceURI = orchResponse.getResponse().get(0).getServiceURI();
        
        //Compiling the URL for the provider
        UriBuilder ub = UriBuilder.fromPath("").host(provider.getAddress()).scheme("http");
        
        if (serviceURI != null) {
            ub.path(serviceURI);
        }
        
        if (provider.getPort() != null && provider.getPort() > 0) {
            ub.port(provider.getPort());
        }
        
        if (orchResponse.getResponse().get(0).getService().getServiceMetadata().containsKey("security")) {
            ub.scheme("https");
            ub.queryParam("token", orchResponse.getResponse().get(0).getAuthorizationToken());
            ub.queryParam("signature", orchResponse.getResponse().get(0).getSignature());
        }
        
        System.out.println("Received provider system URL: " + ub.toString());
        return ub.toString();
    }

}
