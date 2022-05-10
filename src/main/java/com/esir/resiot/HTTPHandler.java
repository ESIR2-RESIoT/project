package com.esir.resiot;

// ========================================================================
// Copyright (c) 2009-2009 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses.
// ========================================================================

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import tuwien.auto.calimero.KNXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HTTPHandler extends AbstractHandler
{
    private final ThreadChenillard thread;

    public HTTPHandler() throws KNXException, InterruptedException {
        thread = new ThreadChenillard();
        thread.start();
        Websocket.setChaserThread(thread);

    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        System.out.println("requete recue");
        baseRequest.setHandled(true);

        if(baseRequest.getMethod().equals("POST")){
            System.out.println("POST recu sur "+baseRequest.getRequestURI());
            BufferedReader in = new BufferedReader(new InputStreamReader(baseRequest.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line=readBuffer(in))!=null) {
                sb.append(line);
            }
            String body = String.valueOf(sb);
            System.out.println("Corps de la requete: "+ body);

            // Routage
            switch(baseRequest.getRequestURI()){
                case "/state":
                    try {
                        thread.changeThreadState(Boolean.parseBoolean(body));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case "/direction":
                    thread.changeChaserDirection();
                    break;

                case "/speed":
                    thread.changeChaserSpeed(Double.parseDouble(body));
                    break;
            }
        }
    }

    private static String readBuffer(Reader reader) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2048; i++) {
            int c = reader.read();
            if (c == -1) {
                return ((sb.length() > 0) ? sb.toString() : null);
            }
            if (((char) c == '\n') || ((char) c == '\r')) {
                break;
            }
            sb.append((char) c);
        }
        return sb.toString();
    }

}
