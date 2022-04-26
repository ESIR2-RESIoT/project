package com.esir.resiot;

import com.google.gson.JsonElement;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.websocket.RemoteEndpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class KNXHandler {

    private static final Logger LOG = Log.getLogger(Websocket.class);
    private static final GsonBuilder builder = new GsonBuilder();
    private RemoteEndpoint.Async remote;

    private Boolean chaseState = false;
    private Boolean chaser[] = {false,false,false,false};
    private String chaseDirection = "ltr"; // left to right
    private double chaseSpeed = 0.5;

    public KNXHandler(RemoteEndpoint.Async remote) {
        this.remote = remote;
    }
    public KNXHandler() {
        this.remote = null;
    }

    public void setRemote(RemoteEndpoint.Async remote) {
        this.remote = remote;
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

    /* Piste : Pour la partie JS utiliser un Web Worker pour faire tourner le chenillard en parallèle de KNX
        Ainsi on envoie des données serveur->client uniquement pour confirmer les changements de paramètres
        et pas à chaque changement d'état des LEDs
    */
    private void changeState(boolean state){
        if(state){
            //TODO: démarrer le thread chenillard
            LOG.info("Starting chaser");
            Gson gson = builder.create();
            boolean[] chaserArray = {true, false, false, false};
            String toSend = gson.toJson(chaserArray);
            this.remote.sendText(toSend);

        }else{
            //TODO: récupérer l'état du chenillard puis arrêter le thread
            LOG.info("Stopping chaser");
            Gson gson = builder.create();
            boolean[] chaserArray = {false, true, false, false};
            String toSend = gson.toJson(chaserArray);
            this.remote.sendText(toSend);
        }
    }

    private void changeDirection(){
        this.chaseDirection = (this.chaseDirection.equals("ltr")) ? "rtl" : "ltr";
        LOG.info("Changing direction to {}",this.chaseDirection);
        //TODO: modifier la variable direction du thread chenillard
        Gson gson = builder.create();
        boolean[] chaserArray = {false, false, true, false};
        String toSend = gson.toJson(chaserArray);
        this.remote.sendText(toSend);
    }

    private void changeSpeed(double speed){
        this.chaseSpeed = speed;
        LOG.info("Changing speed to {}",this.chaseSpeed);
        //TODO: modifier la variable vitesse du thread chenillard
        Gson gson = builder.create();
        boolean[] chaserArray = {false, false, false, true};
        String toSend = gson.toJson(chaserArray);
        this.remote.sendText(toSend);
    }
}
