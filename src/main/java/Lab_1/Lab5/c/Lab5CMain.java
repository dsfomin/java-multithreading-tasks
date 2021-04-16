package Lab_1.Lab5.c;


public class Lab5CMain {
    public static void main(String[] args) {
        int NUMBER_OF_THREADS = 3;

        BarrierAction barrierAction = new BarrierAction();
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS, barrierAction);
        ArrayThread[] threads = new ArrayThread[NUMBER_OF_THREADS];

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new ArrayThread(barrier);
            barrierAction.addThread(threads[i]);
        }

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i].start();
        }
    }
}
