package eu.arrowhead.lidar.common;

import eu.arrowhead.client.common.CertificateBootstrapper;
import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.misc.ClientType;
import eu.arrowhead.client.common.misc.TypeSafeProperties;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRequestForm;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * A simple service utility class used for setting up consumer/ provider services.
 * This class is setup so code can be reused instead of copied for each separate service.
 */
public class ServiceUtility {

    /**
     * Check if the system is running in secure mode.
     * @param args the program arguments
     * @return true if system is running secure mode, false otherwise
     */
    public static boolean checkSecureMode(String[] args) {
        for (String arg : args) {
            if (arg.equals("-tls")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the orchestration URL used for consumers.
     * @param props the system properties
     * @param isSecure is the system running in secure mode (-tls)?
     * @return the orchestration url
     */
    public static String getOrchestratorUrl(TypeSafeProperties props, boolean isSecure) {

        String orchAddress = props.getProperty("orch_address", "0.0.0.0");
        int orchInsecurePort = props.getIntProperty("orch_insecure_port", 8440);
        int orchSecurePort = props.getIntProperty("orch_secure_port", 8441);

        if (isSecure) {
            SSLContextConfigurator sslCon = new SSLContextConfigurator();
            sslCon.setKeyStoreFile(props.getProperty("keystore"));
            sslCon.setKeyStorePass(props.getProperty("keystorepass"));
            sslCon.setKeyPass(props.getProperty("keypass"));
            sslCon.setTrustStoreFile(props.getProperty("truststore"));
            sslCon.setTrustStorePass(props.getProperty("truststorepass"));

            try {
                SSLContext sslContext = sslCon.createSSLContext(true);
                Utility.setSSLContext(sslContext);
            } catch (SSLContextConfigurator.GenericStoreException e) {
                System.out.println("Provided SSLContext is not valid, moving to certificate bootstrapping.");
                e.printStackTrace();
                sslCon = CertificateBootstrapper.bootstrap(ClientType.CONSUMER, props.getProperty("consumer_system_name"));
                props = Utility.getProp();
                Utility.setSSLContext(sslCon.createSSLContext(true));
            }
            Utility.checkProperties(props.stringPropertyNames(), ClientType.CONSUMER.getSecureMandatoryFields());
            return Utility.getUri(orchAddress, orchSecurePort, "orchestrator/orchestration", true, false);
        } else {
            return Utility.getUri(orchAddress, orchInsecurePort, "orchestrator/orchestration", false, false);
        }
    }

    /**
     * Send orchestration request using a service request form srf and the orchestration URL
     * @param srf the service request form
     * @param orchestratorUrl the orchestrationURL
     * @return the provider URI
     */
    public static String sendOrchestrationRequest(ServiceRequestForm srf, String orchestratorUrl) {
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
