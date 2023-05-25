package kcf.gui.canvas;

public abstract class DrawLoop {
    private final int msPerUpdate;

    public DrawLoop(int msPerUpdate) {
        if (msPerUpdate < 1)
            throw new IllegalArgumentException("msPerUpdate is less than 1");
        this.msPerUpdate = msPerUpdate;
        new Thread(this::loop).start();
    }

    private void loop() {
        while (true) {
            update();
            render();
            try {
                Thread.sleep(msPerUpdate);
            } catch (InterruptedException e) {
                // do nothing.
            }
        }
    }

    protected abstract void update();
    protected abstract void render();
}
