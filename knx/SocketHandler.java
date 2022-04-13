import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;

@Component
public class SocketHandler extends TextWebSocketHandler {
	
	private WebSocketSession sessionStatic;
	private static SocketHandler instance;
    public boolean isConnected = false;

	/**
     * A private Constructor prevents any other class from
     * instantiating.
     */
    private SocketHandler() {
        // nothing to do this time
    }

    /**
     * The Static initializer constructs the instance at class
     * loading time; this is to simulate a more involved
     * construction process (it it were really simple, you'd just
     * use an initializer)
     */
    static {
        instance = new SocketHandler();
    }

    /** Static 'instance' method */
    public static SocketHandler getInstance() {
        return instance;
    }

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		   sessionStatic = session ;
		   System.out.println("A new client is connected ! With IP : "+session.getLocalAddress());
           isConnected = true;
		
	}

	public WebSocketSession getSessionStatic(){
		return sessionStatic;
	}

}
