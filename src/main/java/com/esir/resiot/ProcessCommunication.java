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

import java.net.InetSocketAddress;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

/**
 * Example of Calimero process communication, we read (and write) a boolean datapoint in the KNX network. By default,
 * this example will not change any datapoint value in the network.
 */
public class ProcessCommunication
{
    private final Logger LOG = Log.getLogger(ProcessCommunication.class);

    // Address of your KNXnet/IP server. Replace the IP host or address as necessary.
    private static final String remoteHost = "192.168.0.201";

    // We will read a boolean from the KNX datapoint with this group address, replace the address as necessary.
    // Make sure this datapoint exists, otherwise you will get a read timeout!
    private static final String ecritureLampe1 = "0/0/3";
    private static final String lectureLampe1 = "0/1/1";
    private static ProcessCommunicator pc;
    private static KNXNetworkLink knxLink;

    public ProcessCommunication(){
        final InetSocketAddress remote = new InetSocketAddress(remoteHost, 3671);
        // Create our network link, and pass it to a process communicator
        try{
            knxLink = KNXNetworkLinkIP.newTunnelingLink(null, remote, false, new TPSettings());
            pc = new ProcessCommunicatorImpl(knxLink);
            //System.out.println("read boolean value from datapoint " + lectureLampe1);
            //final boolean value = pc.readBool(new GroupAddress(lectureLampe1));
            //System.out.println("datapoint " + lectureLampe1 + " value = " + value);
            System.out.println("Successfully connected to the KNX");
        }
        catch (KNXException | InterruptedException e) {
            System.out.println("Error accessing KNX datapoint: " + e.getMessage());
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
}
