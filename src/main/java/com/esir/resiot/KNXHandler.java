package com.esir.resiot;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.websocket.RemoteEndpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class KNXHandler {

    private static final Logger LOG = Log.getLogger(Websocket.class);
    private static final GsonBuilder builder = new GsonBuilder();
    private RemoteEndpoint.Async remote;

    private ThreadChenillard thread;


    public KNXHandler(RemoteEndpoint.Async remote) {
        this.remote = remote;
        thread = new ThreadChenillard(remote);
        thread.start();
    }

    public void processRequest(String message){
        Gson gson = builder.create();
        ClientCommand clientCommand = gson.fromJson(message, ClientCommand.class);
        switch(clientCommand.getCommand()){
            case "changeState":
                this.changeState(Boolean.parseBoolean(clientCommand.getArg()));
                break;
            case "changeDirection":
                this.changeDirection();
                break;

            case "changeSpeed":
                this.changeSpeed(Double.parseDouble(clientCommand.getArg()));
                break;
        }
        remote.sendText(message);
    }

    private void changeState(boolean state){
        if(state){
            LOG.info("Starting chaser");
            Gson gson = builder.create();
            boolean[] chaserArray = {true, false, false, false};
            String toSend = gson.toJson(chaserArray);
            this.remote.sendText(toSend);
        }else{
            LOG.info("Stopping chaser");
            Gson gson = builder.create();
            boolean[] chaserArray = {false, true, false, false};
            String toSend = gson.toJson(chaserArray);
            this.remote.sendText(toSend);
        }

        thread.changeThreadState();
    }

    private void changeDirection(){
        Gson gson = builder.create();
        boolean[] chaserArray = {false, false, true, false};
        String toSend = gson.toJson(chaserArray);
        this.remote.sendText(toSend);

        thread.changeChaserDirection();
    }

    private void changeSpeed(double speed){
        Gson gson = builder.create();
        boolean[] chaserArray = {false, false, false, true};
        String toSend = gson.toJson(chaserArray);
        this.remote.sendText(toSend);

        thread.changeChaserSpeed(speed);
    }
}
