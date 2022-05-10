package deprecated;

import com.esir.resiot.ClientCommand;
import com.esir.resiot.ThreadChenillard;
import com.esir.resiot.Websocket;

import javax.websocket.RemoteEndpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Deprecated
public class KNXHandler {

    private static final GsonBuilder builder = new GsonBuilder();

    //private final ThreadChenillard thread;


    public KNXHandler(RemoteEndpoint.Async remote) {
        //thread = new ThreadChenillard(remote);
        //thread.start();
    }

    public void processRequest(String message){
        Gson gson = builder.create();
        ClientCommand clientCommand = gson.fromJson(message, ClientCommand.class);

        switch(clientCommand.getCommand()){
            case "changeState":
                if(Boolean.parseBoolean(clientCommand.getArg())){
                    System.out.println("Starting chaser");
                    //thread.changeThreadState(true);
                }else{
                    System.out.println("Stopping chaser");
                    //thread.changeThreadState(false);
                }

                break;

            case "changeDirection":
                //thread.changeChaserDirection();
                break;

            case "changeSpeed":
                //thread.changeChaserSpeed(Double.parseDouble(clientCommand.getArg()));
                break;
        }
    }
}
