package kcf.sim.scheduler;

import kcf.sim.robot.Robot;

import java.util.List;

public class ASYNC extends Scheduler {
    public ASYNC() {}

    public ASYNC(List<? extends Robot> robots) {
        super(robots);
    }

    @Override
    public void schedule() {
        if (robots == null) return;
        int count = (int)(Math.random() * (robots.size() + 1));
        while (count-- > 0)
            robots.get((int)(Math.random() * robots.size())).step();
    }
}
