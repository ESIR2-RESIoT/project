package com.esir.resiot;

import org.eclipse.jetty.server.ServerConnector;


public class Server {
    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080);
        http.setIdleTimeout(30000);
        server.addConnector(http);
        server.setHandler(new IndexHandler());
        server.start();
        server.join();
    }
}
