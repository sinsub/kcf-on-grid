package kcf.sim.scheduler;

import kcf.sim.robot.Robot;

import java.util.List;

public abstract class Scheduler {
    protected List<? extends Robot> robots;

    public Scheduler() {}

    public Scheduler(List<? extends Robot> robots) {
        this.robots = robots;
    }

    public void setRobots(List<? extends Robot> robots) {
        this.robots = robots;
    }

    public abstract void schedule();
}
