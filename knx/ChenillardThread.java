public class ChenillardThread extends Thread {

    private KNXConnection knx;
    private volatile int timer;
    private boolean pause,stop,isAlreadyStop;
    private int i ;
    private boolean isReversed = false;
    EnumMotif motif = EnumMotif.normal;

    public ChenillardThread(KNXConnection knx) {
        System.out.println("Creating chaser");
        this.knx = knx;
        pause = false;
        timer = 1000;
    }

    public void playChenillard(){
        System.out.println("Starting chaser");
        i= 0;
        stop = false;
        isAlreadyStop = false;
    }

    public void stopChenillard() {
        stop = true;
    }

    public void breakChenillard() {
        System.out.println("Pause");
        pause = true;
    }

    public void modifySpeedChenillard(int vitesse) {
        timer = vitesse;
    }

    public void reverseChenillard(){
        isReversed = !isReversed;
    }

    public void resumeChenillard() {
        pause = false;
    }

    public void speedUpChenillard() {
        timer = timer - 200;
        if (timer < 550)
            timer = 550;
    }

    public void speedDownChenillard() {
        timer = timer + 200;
    }

    @Override
    public void run() {

     i = 0;

        while (true) {
            System.out.println(pause);
            try {
                Thread.sleep(timer);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            while (!pause) {
                if(stop){
                    try {
                        Thread.sleep(timer);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                //System.out.println(pause);
                if(!stop){
                   try {
                    knx.writeKNXdata("0/0/" + (i + 1), true);
                    Thread.sleep(timer);
                    knx.writeKNXdata("0/0/" + (i + 1), false);
                    if(isReversed){
                        i = i - 1;
                        if(i == -1){
                            i=3;
                        }
                    }
                    else{
                        i = (i + 1) % 4;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } 
                } else if(!isAlreadyStop){
                    knx.writeKNXdata("0/0/1", false);
                    knx.writeKNXdata("0/0/2", false);
                    knx.writeKNXdata("0/0/3", false);
                    knx.writeKNXdata("0/0/4", false);
                    isAlreadyStop = true;
                }
            }
        }
    }


     private boolean[] makeMotif(EnumMotif motif, int i){

        switch (motif) {
            case normal:
                boolean [][] array = {
                    {true,false,false,false},
                    {false,true,false,false},
                    {false,false,true,false},
                    {false,false,false,true},
                };
                return array[i];
            case paire:
            boolean[][] array1 = {
                {true,true,false,false},
                {false,false,true,true},
            };
                return array1[i];                     
            default:
            boolean [][] array3 = {
                {true,false,false,false},
                {false,true,false,false},
                {false,false,true,false},
                {false,false,false,true},
            };
            return array3[i];
        }
    }

}
