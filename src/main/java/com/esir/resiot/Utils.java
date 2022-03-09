package com.esir.resiot;

import java.io.IOException;
import java.io.Reader;

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

}
