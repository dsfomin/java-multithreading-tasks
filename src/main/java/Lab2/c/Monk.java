package Lab2.c;

import java.util.Random;

public class Monk {
    private final Integer energy;
    private final int monastery;

    Monk() {
        Random r = new Random();
        energy = r.nextInt(1000);
        monastery = r.nextInt(2);
    }

    Integer getEnergy() {
        return energy;
    }
    int getMonastery(){
        return monastery;
    }

    static Monk max(Monk first, Monk second){
        return first.energy > second.energy ? first : second;
    }
}
