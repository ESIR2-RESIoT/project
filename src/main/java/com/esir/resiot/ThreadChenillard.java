package com.esir.resiot;

import javax.websocket.RemoteEndpoint;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class ThreadChenillard extends Thread {
    private static final Logger LOG = Log.getLogger(Websocket.class);

    private RemoteEndpoint.Async remote;
    private volatile boolean running = false;
    int counter = 0;
    double speed = 1;
    int direction = 1;

    public ThreadChenillard(RemoteEndpoint.Async remote){
        this.remote = remote;
    }

    public void changeThreadState() {
        LOG.info("Changing chaser state to "+this.running);
        this.running = !(this.running);
    }

    public void changeChaserSpeed(double speed){
        this.speed = speed;
        LOG.info("Changing chaser speed to "+this.speed);
    }

    public void changeChaserDirection(){
        if(this.direction == 1){
            this.direction = -1;
        }else{
            this.direction = 1;
        }
        LOG.info("Changing chaser direction to "+this.direction);
    }

    @Override
    public void run() {
        while(true) {
            while (running) {
                remote.sendText(String.valueOf(counter));
                try {
                    counter+= this.direction;
                    Thread.sleep((long) (250*(1/speed)));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}