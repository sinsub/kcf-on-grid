package kcf.sim.scheduler;

import kcf.sim.robot.Robot;
import kcf.sim.robot.RobotState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SSYNC extends Scheduler {
    private RobotState state;
    private int round;
    private boolean inLook;
    private final List<Robot> selectedRobots;

    public SSYNC() {
        selectedRobots = new ArrayList<>();
    }

    public SSYNC(List<? extends Robot> robots) {
        super(robots);
        state = RobotState.LOOK;
        ensureState(RobotState.INACTIVE, RobotState.LOOK);
        round = 0;
        inLook = false;
        selectedRobots = new ArrayList<>();
    }

    @Override
    public void setRobots(List<? extends Robot> robots) {
        super.setRobots(robots);
        state = RobotState.LOOK;
        ensureState(RobotState.INACTIVE, RobotState.LOOK);
        round = 0;
        inLook = false;
        selectedRobots.clear();
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
                    selectRobots();
                }
                ensureState(RobotState.INACTIVE, RobotState.LOOK, RobotState.COMPUTE);
                System.out.println("SSYNC Phase: " + state);
                step();
                if (checkState(RobotState.INACTIVE, RobotState.COMPUTE)) {
                    inLook = false;
                    state = RobotState.COMPUTE;
                }
            }
            case COMPUTE: {
                ensureState(RobotState.INACTIVE, RobotState.COMPUTE, RobotState.MOVE);
                System.out.println("SSYNC Phase: " + state);
                step();
                if (checkState(RobotState.INACTIVE, RobotState.MOVE))
                    state = RobotState.MOVE;
            }
            case MOVE: {
                ensureState(RobotState.INACTIVE, RobotState.MOVE, RobotState.LOOK);
                System.out.println("SSYNC Phase: " + state);
                step();
                if (checkState(RobotState.INACTIVE, RobotState.LOOK))
                    state = RobotState.LOOK;
            }
        }
    }

    private void ensureState(RobotState ...states) {
        List<RobotState> stateList = Arrays.asList(states);
        for (Robot r : selectedRobots) {
            RobotState robotState = r.getRobotState();
            if (!stateList.contains(robotState))
                System.out.print("Expected states: " + stateList + ", found: " + robotState);
        }
    }

    private boolean checkState(RobotState ...states) {
        List<RobotState> stateList = Arrays.asList(states);
        for (Robot r : selectedRobots) {
            RobotState robotState = r.getRobotState();
            if (!stateList.contains(robotState))
                return false;
        }
        return true;
    }

    private void selectRobots() {
        selectedRobots.clear();
        int count = (int)(Math.random() * robots.size());
        selectedRobots.addAll(robots);
        Collections.shuffle(selectedRobots);
        while (count-- > 0)
            selectedRobots.remove(selectedRobots.size() - 1);
        System.out.println("SSYNC No of robots: " + selectedRobots.size());
    }

    private void step() {
        for (Robot r : selectedRobots) {
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
