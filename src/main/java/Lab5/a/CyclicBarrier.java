package Lab5.a;

public class CyclicBarrier {
    private final int parties;
    private int partiesAwait;
    private final Runnable event;

    CyclicBarrier(int parties, Runnable event) {
        this.parties = parties;
        this.partiesAwait = parties;
        this.event = event;
    }

    synchronized void await() throws InterruptedException {
        partiesAwait++;
        if (partiesAwait < parties) {
            wait();
        } else {
            partiesAwait = 0;
            notifyAll();
            event.run();
        }
    }
}
