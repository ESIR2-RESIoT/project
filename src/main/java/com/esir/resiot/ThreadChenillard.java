package com.esir.resiot;

import javax.websocket.RemoteEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import java.util.*;

public class ThreadChenillard extends Thread {
    private final Logger LOG = Log.getLogger(ThreadChenillard.class);
    private final GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();

    public enum Directions{
        L2R("Gauche -> Droite"),
        R2L("Droite -> Gauche"),
        L_R("Gauche <-> Droite"),
        RANDOM("Aléatoire");

        static public final Directions[] values = values();
        public final String label;

        private Directions(String label) {
            this.label = label;
        }
        /*
        public Directions previous() { return values[(ordinal() - 1  + values.length) % values.length]; }
        */

        public Directions next() {
            return values[(ordinal() + 1) % values.length];
        }
    }

    /*
    remote is the client connected to the server via websocket.
    It is null until a websocket connection is created.
     */
    public RemoteEndpoint.Async remote;
    boolean running = false;
    double speed = 0.5;
    private int L_Rdirection = 1;
    Directions direction = Directions.L2R;
    int activeLed = -1;
    Random r = new Random();

    public ThreadChenillard(){

    }

    public void setRemote(RemoteEndpoint.Async remote){
        this.remote = remote;
    }

    public void changeThreadState(boolean val) {
        this.running = val;
        LOG.info("Changed chaser state to "+this.running);
        ServerCommand toSend = new ServerCommand("status", this.running);
        remote.sendText(gson.toJson(toSend));
        toSend = (this.running) ? new ServerCommand("speed", this.speed) : new ServerCommand("speed", 0);
        remote.sendText(gson.toJson(toSend));
        toSend = new ServerCommand("direction", direction.label);
        remote.sendText(gson.toJson(toSend));
    }

    public void changeChaserSpeed(double speed){
        this.speed = speed;
        LOG.info("Changed chaser speed to "+this.speed);
        ServerCommand toSend = new ServerCommand("speed", this.speed);
        remote.sendText(gson.toJson(toSend));
    }

    public void changeChaserDirection(){
        this.direction = direction.next();
        LOG.info("Changed chaser direction to "+this.direction);
        LOG.info(String.valueOf(direction));
        ServerCommand toSend = new ServerCommand("direction", direction.label);
        remote.sendText(gson.toJson(toSend));
    }

    @Override
    public void run() {
        while(true) {
            LOG.info(String.valueOf(running)); // Ne pas supprimer ce log sinon tout casse
            while (running) {
                //LOG.info("true");
                if(activeLed == -1){ // Cas particulier : initialisation (nécessaire sinon le chenillard risque de commencer sur une mauvaise led
                    activeLed = 0;
                }else{ // Détermination de la LED à allumer

                    // TODO : KNX - Eteindre la led d'indice activeLed

                    switch(this.direction){
                        case L2R:
                            activeLed = (activeLed == 3) ? 0 : activeLed+1;
                            break;

                        case R2L:
                            activeLed = (activeLed == 0) ? 3 : activeLed-1;
                            break;

                        case L_R:
                            if(activeLed == 3){
                                L_Rdirection = -1;
                            }else if(activeLed == 0){
                                L_Rdirection = 1;
                            }
                            activeLed += L_Rdirection;
                            break;

                        case RANDOM:
                            int val = r.nextInt(4);
                            while(activeLed == val){
                                val = r.nextInt(4);
                            }
                            activeLed = val;
                            break;
                    }
                }

                // TODO : KNX - Allumer la led d'indice activeLed

                boolean[] LEDs = new boolean[4];
                Arrays.fill(LEDs, Boolean.FALSE);
                LEDs[activeLed] = true;
                ServerCommand toSend = new ServerCommand("LEDStatus", LEDs);
                remote.sendText(gson.toJson(toSend));
                try {
                    Thread.sleep((long) (250*(1/speed)));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}