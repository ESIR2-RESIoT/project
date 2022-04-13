import java.io.IOException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

public class KNXListener implements ProcessListener {

    private ChenillardThread chenillard;
    private SocketHandler listener;
    private boolean[] statutLamp = {false,false,false,false};

    public KNXListener(ChenillardThread chenillard, SocketHandler listHandler) {
        System.out.println("Initializing KNX listener...");
        this.chenillard = chenillard;
        this.listener = listHandler;
    }

    @Override
    public void groupReadRequest(ProcessEvent e) {
        // System.out.println("REDQSERSquret" + e.toString());
    }

    @Override
    public void groupReadResponse(ProcessEvent e) {
        // System.out.println("RDSOIRESPID" + e.toString());
    }

    @Override
    public void groupWrite(ProcessEvent e) {
        //System.out.println("groupWriteEvent " + e.getDestination().toString());
        if (e.getDestination().toString().contains("1/0/")) {
            System.out.println("Boutton appuyé");

            // String groupCMD = "0" + e.getDestination().toString().substring(1);
            // String groupState = groupCMD.replace("/0/", "/1/");
            String value = e.toString().substring(e.toString().length() - 1);

            if (value.contains("0")) {
                if (e.getDestination().toString().endsWith("1")) {
                    chenillard.resumeChenillard();
                } else if (e.getDestination().toString().endsWith("2")) {
                    chenillard.pauseChennillard();
                } else if (e.getDestination().toString().endsWith("3")) {
                    chenillard.speedUpChenillard();
                } else if (e.getDestination().toString().endsWith("4")) {
                    chenillard.speedDownChenillard();
                }
            }

            // System.out.println("Value " + value + " | group " + groupCMD);
        } else if (e.getDestination().toString().contains("0/1/")) {
            while(!listener.isConnected){
                try {
                    Thread.sleep(2000);   
                } catch (Exception eS) {
                    System.err.println(eS.toString());
                }
            }
            System.out.println("listenerKNX");
            DPTXlatorBoolean t;
            //boolean[] statutLampe = {false,false,false,false};
            try {
                t = new DPTXlatorBoolean(DPTXlatorBoolean.DPT_BOOL);
                t.setData(e.getASDU());
                System.out.println(e.getDestination().toString() + " -> " +t.getValueBoolean());
                statutLampe[Integer.parseInt(String.valueOf(e.getDestination().toString().charAt(e.getDestination().toString().length()-1)))-1] = t.getValueBoolean();
            } catch (KNXFormatException e2) {
                System.out.println("KNX format wrong check the message format ! ");
                e2.printStackTrace();
            }

            JSONObject jsonEtatlampe = new JSONObject();
            
                jsonEtatlampe.put("Lampe1", statutLampe[0]);
                jsonEtatlampe.put("Lampe2", statutLampe[1]);
                jsonEtatlampe.put("Lampe3", statutLampe[2]);
                jsonEtatlampe.put("Lampe4", statutLampe[3]);
            
            try {
                listener.getSessionStatic().sendMessage(new TextMessage(jsonEtatlampe.toString()));
                System.out.println("Message JSON :"+jsonEtatlampe.toString());
            } catch (IOException e1) {
                System.out.println("IO execption when sending message to Websocket ! ");
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void detached(DetachEvent e) {
        // System.out.println(e.toString());
    }

}