package com.esir.resiot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;

import com.google.common.io.Resources;
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
        System.out.println("----------handling request----------");
        System.out.println("Method: "+request.getMethod());
        System.out.println("Target: "+target);
        StringBuilder body = Utils.readBody(request);
        System.out.println("Body  : "+body);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        StringBuffer stringBuffer = Utils.sendBuffer("index.html");
        response.getWriter().println(stringBuffer);

        /* Exemple de fonction handle de la doc
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        response.getWriter().println("<h1>"+_greeting+"</h1>");
        if (_body!=null) response.getWriter().println(_body);
         */
    }

}