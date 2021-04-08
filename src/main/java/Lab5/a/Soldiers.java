package Lab5.a;

import java.util.ArrayList;

public class Soldiers implements Runnable {
    private final ArrayList<Boolean> array;
    private final int l;
    private final int r;
    private final CyclicBarrier barrier;
    private final Changed isChanged;

    Soldiers(ArrayList<Boolean> array, int l, int r, CyclicBarrier barrier, Changed isChanged) {
        this.array = array;
        this.l = l;
        this.r = r;
        this.barrier = barrier;
        this.isChanged = isChanged;
    }

    @Override
    public void run() {
        while (isChanged.getChanged()) {
            boolean flag = false;
            for (int i = l; i < r; i++) {
                if (!array.get(i) && array.get(i + 1)) {
                    System.out.println("Turned soldiers " + i + " " + (i + 1) + " in bounds " + l + " " + r);
                    array.set(i, true);
                    array.set(i, false);
                    flag = true;
                }
            }
            if (flag)
                isChanged.setChanged(true);
            System.out.println("Part of soldiers balanced and reached barrier: " + l + " " + r);

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

