package Lab5.a;

public class Changed {
    private volatile boolean changed = true;

    synchronized boolean getChanged() {
        return changed;
    }

    synchronized void setChanged(boolean changed) {
        this.changed = changed;
    }
}
