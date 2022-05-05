package com.esir.resiot;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class Websocket extends Endpoint implements MessageHandler.Whole<String>
{
    private static final Logger LOG = Log.getLogger(Websocket.class);
    private Session session;
    private RemoteEndpoint.Async remote;
    // private KNXHandler knxHandler; // Deprecated

    private static ThreadChenillard thread;

    public static void setChaserThread(ThreadChenillard newThread){
        thread = newThread;
    }

    @Override
    public void onClose(Session session, CloseReason close)
    {
        super.onClose(session,close);
        this.session = null;
        this.remote = null;
        LOG.info("WebSocket Close: {} - {}",close.getCloseCode(),close.getReasonPhrase());
        thread.changeThreadState(false);
    }

    public void onOpen(Session session, EndpointConfig config)
    {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        //knxHandler = new KNXHandler(this.remote); // Deprecated
        LOG.info("WebSocket Connect: {}",session);
        ServerCommand toSend = new ServerCommand("info", "You are now connected to " + this.getClass().getName());
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        this.remote.sendText(gson.toJson(toSend));
        // attach echo message handler
        session.addMessageHandler(this);
        thread.setRemote(this.remote);
        thread.changeThreadState(false);
    }

    @Override
    public void onError(Session session, Throwable cause)
    {
        super.onError(session,cause);
        LOG.warn("WebSocket Error",cause);
    }

    @Override
    public void onMessage(String message)
    {
        if (this.session != null && this.session.isOpen() && this.remote != null)
        {
            // knxHandler.processRequest(message); // Deprecated
        }
    }
}
