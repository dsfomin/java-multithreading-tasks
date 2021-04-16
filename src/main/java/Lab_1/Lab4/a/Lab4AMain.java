package Lab_1.Lab4.a;

import java.io.File;

public class Lab4AMain {
    private static final File file = new File("D:\\database.txt");

    public static void main(String[] args) {

        RWLock rwLock = new RWLock();

        Thread addPerson = new Thread(new AddPerson(file, rwLock));
        Thread deletePerson = new Thread(new DeletePerson(file, rwLock));
        Thread findByPhone = new Thread(new FindByPhone(file, rwLock));
        Thread findBySurname = new Thread(new FindBySurname(file, rwLock));


        findByPhone.start();
        findBySurname.start();
        addPerson.start();
        deletePerson.start();


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addPerson.interrupt();
        deletePerson.interrupt();
        findByPhone.interrupt();
        findBySurname.interrupt();
    }
}
