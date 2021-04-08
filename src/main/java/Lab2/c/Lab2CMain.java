package Lab2.c;

import java.util.concurrent.ForkJoinPool;

public class Lab2CMain {

    public static void main(String[] args) {
        int monksNumber = 50;
        if (args[0] != null && Integer.parseInt(args[0]) > 0) {
            monksNumber = Integer.parseInt(args[0]);
        }
        Competition comp = new Competition(monksNumber);
        ForkJoinPool pool = new ForkJoinPool();
        Monk winner = pool.invoke(comp);
        String monastery = winner.getMonastery() == 0 ? "Huan-un" : "Huan-in";
        System.out.println("Winner: " + winner.getEnergy() + " Monastary: " +  monastery);
    }
}

