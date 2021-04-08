package Lab3.b;

import java.util.concurrent.Semaphore;

public class BarberShop implements Runnable {
    private final Semaphore waitingChairs = new Semaphore(3);
    private final Semaphore barberChair = new Semaphore(1);
    Barber barber;

    public void run() {
        barber = new Barber();
        new Thread(barber).start();

        long i = 0L;
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Customer customer = new Customer(i);
            new Thread(customer).start();
            i++;
        }
    }

    class Barber implements Runnable {
        private boolean isSleeping;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                cutHair();
            }
        }

        void cutHair() {
            if (isSleeping) {
                System.out.println("The barber is sleeping");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                if (barberChair.tryAcquire())
                    isSleeping = true;
                else {
                    System.out.println("The barber is cutting hair");
                    barberChair.release();
                }
            }
        }

        void wakeUp() {
            if (isSleeping) {
                isSleeping = false;
                barberChair.release();
                System.out.println("The barber is woken up");
            }
        }
    }

    class Customer implements Runnable {
        private final long id;

        Customer(long id) {
            this.id = id;
        }

        @Override
        public void run() {
            if (waitingChairs.tryAcquire()) {
                System.out.println("Client " + id + " waiting");
                while (!barberChair.tryAcquire()) {
                    barber.wakeUp();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Client " + id + " set in barber chair");
                waitingChairs.release();
            } else {
                System.out.println("No free sits");
            }
        }
    }
}
