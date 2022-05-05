import java.net.InetSocketAddress;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

public class KNXConnection {

    private KNXNetworkLink knxLink;
    private ProcessCommunicator pc;
    private ChenillardThread chenillardThread;
    private static final InetSocketAddress local = new InetSocketAddress(0);


    public KNXConnection(String ip, ChenillardThread chenillardThread) {
        InetSocketAddress socket = new InetSocketAddress(ip, 3671);
        try {
            chenillardThread = new ChenillardThread(this);
            this.knxLink = KNXNetworkLinkIP.newTunnelingLink(null, socket, false, new TPSettings());
            System.out.println("Connection established to server");
            this.chenillardThread = chenillardThread;
            this.chenillardThread.start();
            this.pc = new ProcessCommunicatorImpl(knxLink) {
            };
            pc.addProcessListener(new KNXListener(chenillardThread, SocketHandler.getInstance()));
        } catch (KNXException | InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public void demarrerChenillard(){
        chenillardThread.playChenillard();
    }

    public void arreterChenillard(){
        chenillardThread.stopChenillard();
    }

    public void pauseChenillard(){
        chenillardThread.breakChenillard();
    }

    public void modifierVitesseChenillard(int vitesse){
        chenillardThread.modifySpeedChenillard(vitesse);
    }

    public void inverserChenillard(){
        chenillardThread.reverseChenillard();
    }
    
    public void reprendreChenillard(){
        chenillardThread.resumeChenillard();
    }

    public void deconnectertKNXConnection() {
        System.out.println("Closing KNX connection...");
        pc.close();
        knxLink.close();
    }

    public void ecrireKNXdata(String group, boolean value) {
        try {
            pc.write(new GroupAddress(group), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean lireKNXdata(String group) {
        try {
            boolean result = pc.readBool(new GroupAddress(group));
            //System.out.println("Reading value from " + group + " -> " + result);
            return result;
        } catch (KNXException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}