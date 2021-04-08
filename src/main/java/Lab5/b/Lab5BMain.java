package Lab5.b;

import java.util.concurrent.CyclicBarrier;

public class Lab5BMain {

    public static void main(String[] args) {
        final int threadsNumber = 4;
        CyclicBarrier barrier = new CyclicBarrier(3);
        String tmp;
        for (int i = 0; i < threadsNumber; i++) {
            tmp = StringGenerator.generateString();
            Thread thread = new Thread(new StringModifier(i, tmp, barrier));
            thread.start();
        }
    }
}
