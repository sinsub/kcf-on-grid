package kcf.sim;

import kcf.sim.scheduler.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class SimulationLoader<K extends Simulation, S> {
    protected Scheduler scheduler;

    public SimulationLoader(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public abstract S loadFromFile(File file) throws FileNotFoundException;

    public abstract K getSimulation(S setup);
}
