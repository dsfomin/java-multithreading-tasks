package Lab5.a;

import java.util.ArrayList;
import java.util.Random;

public class ArrayGenerator {
    private static final Random random = new Random();

    public static ArrayList<Boolean> generate() {
        int size = random.nextInt(400) + 100;
        ArrayList<Boolean> array = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            array.add(random.nextBoolean());
        }
        return array;
    }
}
