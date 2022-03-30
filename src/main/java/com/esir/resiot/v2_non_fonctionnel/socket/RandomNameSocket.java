package com.esir.resiot.v2_non_fonctionnel.socket;

import com.esir.resiot.v2_non_fonctionnel.util.DemoBeanUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class RandomNameSocket {
    @OnWebSocketConnect
    public void onConnect(Session session) {
        DemoBeanUtil.getRandomNameService().addSession(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int _closeCode, String _closeReason) {
        DemoBeanUtil.getRandomNameService().removeSession(session);
    }
}
