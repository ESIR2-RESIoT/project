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

package com.esir.resiot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.google.common.io.Resources;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class IndexHandler extends AbstractHandler
{
    public IndexHandler()
    {

    }


    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        URL url = Resources.getResource("index.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line=readBuffer(in, 2048))!=null) {
            sb.append(line);
        }
        response.getWriter().println(sb);
    }

    public static String readBuffer(Reader reader, int limit) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit; i++) {
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