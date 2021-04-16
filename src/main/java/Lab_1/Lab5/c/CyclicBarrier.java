package Lab_1.Lab5.c;

public class CyclicBarrier {
    private final int parties;
    private int currentParties = 0;
    private final Runnable barrierAction;

    CyclicBarrier(int parties, Runnable barrierAction) {
        this.parties = parties;
        this.barrierAction = barrierAction;
    }

    public synchronized void await() throws InterruptedException {
        currentParties++;
        if (currentParties == parties) {
            if (barrierAction != null) barrierAction.run();
            currentParties = 0;
            notifyAll();
        } else wait();
    }
}
