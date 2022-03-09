package com.esir.resiot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

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
        System.out.println("URL   : "+request.getRequestURL());
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        URL url = Resources.getResource("index.html"); // La page à afficher au client se trouve dans nos ressources
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); // Il faut la convertir en StringBuffer pour pouvoir l'envoyer
        StringBuffer sb = new StringBuffer();
        String line;
        while((line=Utils.readBuffer(in, 2048))!=null) {
            sb.append(line);
        }

        response.getWriter().println(sb);

        /* Exemple de fonction handle de la doc
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        response.getWriter().println("<h1>"+_greeting+"</h1>");
        if (_body!=null) response.getWriter().println(_body);
         */
    }

}