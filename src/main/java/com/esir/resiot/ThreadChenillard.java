package com.esir.resiot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tuwien.auto.calimero.KNXException;

import javax.websocket.RemoteEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadChenillard extends Thread {
    private final GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();
    Random r = new Random();

    public enum Directions {
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

    volatile boolean running = false;
    int nbleds = 1;
    double speed = 0.5;
    private int L_Rdirection = 1;
    Directions direction = Directions.L2R;
    List<Integer> activeLeds = new ArrayList<Integer>();
    List<Integer> previousLeds = new ArrayList<Integer>();
    List<Integer> ledsOff = new ArrayList<Integer>();
    List<Integer> ledsOn = new ArrayList<Integer>();

    private RemoteEndpoint.Async remote; // Client connecté depuis l'Appli Web
    private ProcessCommunication processCommunication; // Communication avec le KNX

    public ThreadChenillard() throws KNXException, InterruptedException {
        processCommunication = new ProcessCommunication(this);
        activeLeds.add(0);
    }

    public void setRemote(RemoteEndpoint.Async remote) {
        this.remote = remote;
    }

    public ProcessCommunication getProcessCommunication() {
        return processCommunication;
    }


    public void changeThreadState(){
        this.running = !(this.running);
        changeThreadState(this.running);
    }

    public void changeThreadState(boolean val){
        this.running = val;
        System.out.println("Changed chaser state to " + this.running);
        ServerCommand toSend = new ServerCommand("status", this.running);
        remote.sendText(gson.toJson(toSend));
        toSend = new ServerCommand("speed", this.speed);
        remote.sendText(gson.toJson(toSend));
        toSend = new ServerCommand("direction", direction.label);
        remote.sendText(gson.toJson(toSend));
    }

    public void changeChaserSpeed(double speed) {
        double newSpeed = this.speed + 0.25 * speed; // speed = -1 ou 1
        if(newSpeed < 0.25 || newSpeed > 1) return;
        this.speed = newSpeed;
        System.out.println("Changed chaser speed to " + this.speed);
        ServerCommand toSend = new ServerCommand("speed", this.speed);
        remote.sendText(gson.toJson(toSend));
    }

    public void changeChaserDirection() {
        this.direction = direction.next();
        System.out.println("Changed chaser direction to " + this.direction);
        System.out.println(direction);
        ServerCommand toSend = new ServerCommand("direction", direction.label);
        remote.sendText(gson.toJson(toSend));
    }

    @Override
    public void run() {
        while (true) {
            while (running) {
                previousLeds.clear();
                previousLeds.addAll(activeLeds);
                activeLeds.clear();
                ledsOff.clear();
                ledsOn.clear();
                switch (this.direction) {
                    case L2R:
                        activeLeds.add((previousLeds.get(0) == 3) ? 0 : previousLeds.get(0) + 1);
                        break;

                    case R2L:
                        activeLeds.add((previousLeds.get(0) == 0) ? 3 : previousLeds.get(0) - 1);
                        break;

                    case L_R:
                        if (previousLeds.get(0) == 3) {
                            L_Rdirection = -1;
                        } else if (previousLeds.get(0) == 0) {
                            L_Rdirection = 1;
                        }
                        activeLeds.add(previousLeds.get(0) + L_Rdirection);
                        break;

                    case RANDOM:
                        int val = r.nextInt(4);
                        while (previousLeds.get(0) == val) {
                            val = r.nextInt(4);
                        }
                        activeLeds.add(val);
                        break;
                }


                /*  Ancienne implémentation : le serveur actualise lui-même les LEDs (sans attendre la réponse du KNX)
                boolean[] LEDs = new boolean[4];
                Arrays.fill(LEDs, Boolean.FALSE);
                for (int led : activeLeds) {
                    LEDs[led] = true;
                }
                ServerCommand toSend = new ServerCommand("LEDStatus", LEDs);
                // remote.sendText(gson.toJson(toSend));
                 */

                for (int led : previousLeds) {
                    if (!(activeLeds.contains(led))) {
                        ledsOff.add(led);
                    }
                }
                for (int led : activeLeds) {
                    if (!(previousLeds.contains(led))) {
                        ledsOn.add(led);
                    }
                }

                for (int led : ledsOff) processCommunication.ecrireKNXdata("0/0/" + (led + 1), false);
                for (int led : ledsOn) processCommunication.ecrireKNXdata("0/0/" + (led + 1), true);

                try {
                    Thread.sleep((long) (250 * (1 / speed)));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}