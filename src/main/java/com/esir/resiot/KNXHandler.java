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

    private Boolean chaseState = false;
    private String chaseDirection = "ltr"; // left to right
    private double chaseSpeed = 0.5;

    public KNXHandler(RemoteEndpoint.Async remote) {
        this.remote = remote;
    }

    public void setRemote(RemoteEndpoint.Async remote) {
        this.remote = remote;
    }

    public void processRequest(String message){
        Gson gson = builder.create();
        ClientCommand clientCommand = gson.fromJson(message, ClientCommand.class);
        switch(clientCommand.getCommand()){
            case "changeState":
                if(Boolean.parseBoolean(clientCommand.getArg())){
                    this.startChaser();
                }else{
                    this.stopChaser();
                }
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

    /* TODO : Pour la partie JS utiliser un Web Worker pour faire tourner le chenillard en parallèle de KNX
        Ainsi on envoie des données serveur->client uniquement pour confirmer les changements de paramètres
        et pas à chaque changement d'état des LEDs
        */
    private void startChaser(){
        LOG.info("Starting chaser");
        //TODO interaction KNX
    }

    private void stopChaser(){
        LOG.info("Stopping chaser");
        //TODO interaction KNX
    }

    private void changeDirection(){
        this.chaseDirection = (this.chaseDirection.equals("ltr")) ? "rtl" : "ltr";
        LOG.info("Changing direction to {}",this.chaseDirection);
        //TODO interaction KNX
    }

    private void changeSpeed(double speed){
        this.chaseSpeed = speed;
        LOG.info("Changing speed to {}",this.chaseSpeed);
        //TODO interaction KNX
    }

}
