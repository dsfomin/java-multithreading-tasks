package Lab3.a;

public class Bee implements Runnable{
    Bear bear;
    Pot pot;
    int numberOfBee;
    Bee (int numberOfBee, Bear bear, Pot pot){
        this.numberOfBee = numberOfBee;
        this.bear = bear;
        this.pot = pot;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Bee #" + numberOfBee + " + 1 honey");
            pot.addHoney();
            if (pot.isFull()) {
                System.out.println("Is full");
                bear.wakeUp();
            }
        }
    }
}
