package com.esir.resiot;

/*
    Calimero 2 - A library for KNX network access
    Copyright (c) 2013, 2021 B. Malinowsky
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

import javax.websocket.RemoteEndpoint;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of Calimero process communication, we read (and write) a boolean datapoint in the KNX network. By default,
 * this example will not change any datapoint value in the network.
 */
public class ProcessCommunication
{
    private final GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();
    // Address of your KNXnet/IP server. Replace the IP host or address as necessary.

    // We will read a boolean from the KNX datapoint with this group address, replace the address as necessary.
    // Make sure this datapoint exists, otherwise you will get a read timeout!
    private static ProcessCommunicator pc;
    private static KNXNetworkLink knxLink;
    private RemoteEndpoint.Async remoteWebApp;


    public ProcessCommunication(ThreadChenillard thread){

        final InetSocketAddress remote = new InetSocketAddress("192.168.0.202", 3671);
        // Create our network link, and pass it to a process communicator
        try{
            knxLink = KNXNetworkLinkIP.newTunnelingLink(null, remote, false, new TPSettings());
            pc = new ProcessCommunicatorImpl(knxLink);
            pc.addProcessListener(new ProcessListener() {
                @Override
                public void groupReadRequest(ProcessEvent processEvent) {
                    // Inutilisé
                }

                @Override
                public void groupReadResponse(ProcessEvent processEvent) {
                    // Inutilisé
                }

                @Override
                public void groupWrite(ProcessEvent processEvent) {
                    System.out.println("groupWrite");
                    String group = processEvent.getDestination().toString();

                        switch(group){
                            case "1/0/1": // appui bouton 1
                                if(processEvent.getASDU()[0] == 1) thread.changeThreadState();
                                break;

                            case "1/0/2": // appui bouton 2
                                if(processEvent.getASDU()[0] == 1) thread.changeChaserDirection();
                                break;

                            case "1/0/3": // appui bouton 3
                                if(processEvent.getASDU()[0] == 1) thread.changeChaserSpeed(-1);
                                break;

                            case "1/0/4": // appui bouton 4
                                if(processEvent.getASDU()[0] == 1) thread.changeChaserSpeed(1);
                                break;

                            default: // Changement d'état des leds ("0/1/1" à "0/1/4")
                                int groupNumber = Integer.parseInt(group.replace("/",""));
                                if(groupNumber >= 11 && groupNumber <= 14){
                                    List<Object> list = new ArrayList<>();
                                    list.add(Integer.parseInt(group.split("/")[2])-1); // "0/1/4" -> 3
                                    list.add(processEvent.getASDU()[0]);
                                    ServerCommand toSend = new ServerCommand("singleLedStatus", list);
                                    remoteWebApp.sendText(gson.toJson(toSend));
                                }
                                break;
                        }

                    System.out.println(processEvent.getDestination());
                    System.out.println(Arrays.toString(processEvent.getASDU()));
                }

                @Override
                public void detached(DetachEvent detachEvent) {
                    // Inutilisé
                }
            });
            System.out.println("Connexion au KNX réussie");
        }
        catch (KNXException | InterruptedException e) {
            System.out.println("Echec de la connexion au KNX : " + e.getMessage());
        }
    }

    public void ecrireKNXdata(String group, boolean value) {
        try {
            pc.write(new GroupAddress(group), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProcessCommunicator getPc(){
        return pc;
    }

    public KNXNetworkLink getKnxLink(){
        return knxLink;
    }

    public void setRemote(RemoteEndpoint.Async remote){
        this.remoteWebApp = remote;
    }
}
