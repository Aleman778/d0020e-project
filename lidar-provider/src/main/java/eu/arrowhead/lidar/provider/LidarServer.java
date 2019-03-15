package eu.arrowhead.lidar.provider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LidarServer {
    private ServerSocket server;
    private Socket client;
    private BufferedReader reader;

    public LidarServer(String ipAddress) throws Exception {
        if (ipAddress != null && !ipAddress.isEmpty()) 
          this.server = new ServerSocket(37777, 1, InetAddress.getByName(ipAddress));
        else 
          this.server = new ServerSocket(37777, 1, InetAddress.getLocalHost());

        System.out.println(InetAddress.getLocalHost());
    }

    public void accept() throws Exception {
        client = server.accept();
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public String readLine() throws Exception {
        return reader.readLine();
    }
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }
    
    public int getPort() {
        return this.server.getLocalPort();
    }
}