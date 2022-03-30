package com.esir.resiot.v2_non_fonctionnel.servlet;

import com.esir.resiot.v2_non_fonctionnel.socket.RandomNameSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class RandomNameServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(RandomNameSocket.class);
    }
}