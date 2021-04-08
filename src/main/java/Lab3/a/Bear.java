package Lab3.a;

public class Bear implements Runnable {
    private final Pot pot;
    private volatile boolean isAwake = false;

    Bear(Pot pot){
        this.pot = pot;
    }

    public synchronized void wakeUp(){
        isAwake = true;
        notifyAll();
    }

    @Override
    public void run() {
        while(true) {
            synchronized (this) {
                while (!isAwake) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pot.eatAll();
                isAwake = false;
                notifyAll();
            }
        }
    }

}
