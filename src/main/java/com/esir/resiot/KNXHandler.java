package com.esir.resiot;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.websocket.RemoteEndpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class KNXHandler {

    private static final Logger LOG = Log.getLogger(Websocket.class);
    private static final GsonBuilder builder = new GsonBuilder();

    private final ThreadChenillard thread;


    public KNXHandler(RemoteEndpoint.Async remote) {
        thread = new ThreadChenillard(remote);
        thread.start();
    }

    public void processRequest(String message){
        Gson gson = builder.create();
        ClientCommand clientCommand = gson.fromJson(message, ClientCommand.class);

        switch(clientCommand.getCommand()){
            case "changeState":
                if(Boolean.parseBoolean(clientCommand.getArg())){
                    LOG.info("Starting chaser");
                    thread.changeThreadState(true);
                }else{
                    LOG.info("Stopping chaser");
                    thread.changeThreadState(false);
                }

                break;

            case "changeDirection":
                thread.changeChaserDirection();
                break;

            case "changeSpeed":
                thread.changeChaserSpeed(Double.parseDouble(clientCommand.getArg()));
                break;
        }
    }
}
