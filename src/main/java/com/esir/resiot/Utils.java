package com.esir.resiot;

import com.google.common.io.Resources;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class Utils {

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

    public static StringBuffer sendBuffer(String urlString) throws IOException {
        URL url = Resources.getResource(urlString); // La page à afficher au client se trouve dans nos ressources
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); // Il faut la convertir en StringBuffer pour pouvoir l'envoyer
        StringBuffer sb = new StringBuffer();
        String line;
        while((line=readBuffer(in, 2048))!=null) {
            sb.append(line);
        }
        return sb;
    }

    public static StringBuilder readBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        return sb;
    }

}
