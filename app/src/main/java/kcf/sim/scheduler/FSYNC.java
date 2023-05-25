package kcf.sim.scheduler;

import kcf.sim.robot.Robot;
import kcf.sim.robot.RobotState;

import java.util.Arrays;
import java.util.List;

public class FSYNC extends Scheduler {
    private RobotState state;
    private int round;
    private boolean inLook;

    public FSYNC() {}

    public FSYNC(List<? extends Robot> robots) {
        super(robots);
        state = RobotState.LOOK;
        ensureState(RobotState.INACTIVE, RobotState.LOOK);
        round = 0;
        inLook = false;
    }

    @Override
    public void setRobots(List<? extends Robot> robots) {
        super.setRobots(robots);
        state = RobotState.LOOK;
        ensureState(RobotState.INACTIVE, RobotState.LOOK);
        round = 0;
        inLook = false;
    }

    @Override
    public void schedule() {
        if (robots == null) return;
        switch (state) {
            case LOOK: {
                if (!inLook) {
                    inLook = true;
                    round++;
                    System.out.println("---------- Round #" + round + " ----------");
                }
                ensureState(RobotState.INACTIVE, RobotState.LOOK, RobotState.COMPUTE);
                System.out.println("FSYNC Phase: " + state);
                step();
                if (checkState(RobotState.INACTIVE, RobotState.COMPUTE)) {
                    inLook = false;
                    state = RobotState.COMPUTE;
                }
            }
            case COMPUTE: {
                ensureState(RobotState.INACTIVE, RobotState.COMPUTE, RobotState.MOVE);
                System.out.println("FSYNC Phase: " + state);
                step();
                if (checkState(RobotState.INACTIVE, RobotState.MOVE))
                    state = RobotState.MOVE;
            }
            case MOVE: {
                ensureState(RobotState.INACTIVE, RobotState.MOVE, RobotState.LOOK);
                System.out.println("FSYNC Phase: " + state);
                step();
                if (checkState(RobotState.INACTIVE, RobotState.LOOK))
                    state = RobotState.LOOK;
            }
        }
    }

    private void ensureState(RobotState ...states) {
        List<RobotState> stateList = Arrays.asList(states);
        for (Robot r : robots) {
            RobotState robotState = r.getRobotState();
            if (!stateList.contains(robotState))
                System.out.print("Expected states: " + stateList + ", found: " + robotState);
        }
    }

    private boolean checkState(RobotState ...states) {
        List<RobotState> stateList = Arrays.asList(states);
        for (Robot r : robots) {
            RobotState robotState = r.getRobotState();
            if (!stateList.contains(robotState))
                return false;
        }
        return true;
    }

    private void step() {
        for (Robot r : robots) {
            if (r.getRobotState().equals(state))
                r.step();
        }
    }

    public RobotState getState() {
        return state;
    }

    public int getRound() {
        return round;
    }
}
