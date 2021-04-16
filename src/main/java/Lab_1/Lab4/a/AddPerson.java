package Lab_1.Lab4.a;

import java.io.*;
import java.util.Random;

public class AddPerson implements Runnable {
    private final Random random;
    private final File file;

    private final RWLock rwLock;

    AddPerson(File db, RWLock rwLock) {
        random = new Random();
        file = db;

        this.rwLock = rwLock;
    }

    @Override
    public void run() {
        int pos;
        String surname, name, phone;
        while (!Thread.currentThread().isInterrupted()) {
            rwLock.lockWrite();

            pos = random.nextInt(Constants.surnames.size());
            surname = Constants.surnames.get(pos);
            pos = random.nextInt(Constants.names.size());
            name = Constants.names.get(pos);
            pos = random.nextInt(Constants.phones.size());
            phone = Constants.phones.get(pos);
            try {
                PrintWriter printWriter = new PrintWriter(new FileWriter(file, true));
                printWriter.println(surname + " " + name + " " + phone);
                System.out.println("AddPerson thread added new person: " + surname + " " + name + " " + phone);

                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rwLock.unlockWrite();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
