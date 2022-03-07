package com.esir.resiot;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class SimplestServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080);
        http.setIdleTimeout(30000);
        server.addConnector(http);
        server.setHandler(new HelloHandler());
        server.start();
        server.join();
    }
}
