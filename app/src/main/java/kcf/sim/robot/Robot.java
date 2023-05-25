package kcf.sim.robot;

import java.util.UUID;

public abstract class Robot {
    private final UUID id;
    protected RobotState state;
    private boolean deactivate;
    private int round;
    private int activeRounds;

    public Robot() {
        this.id = UUID.randomUUID();
        this.state = RobotState.LOOK;
        this.deactivate = false;
        this.round = this.activeRounds = 0;
    }

    public UUID getId() {
        return id;
    }

    public RobotState getRobotState() {
        return state;
    }

    public int getRound() {
        return round;
    }

    public int getActiveRounds() {
        return activeRounds;
    }

    public void activate() {
        if (state == RobotState.INACTIVE) activateRobot();
        else if (deactivate) deactivate = false;
    }

    // robot is deactivated when the robot is in LOOK phase
    public void deactivate() {
        if (state == RobotState.INACTIVE) return;
        if (state == RobotState.LOOK) deactivateRobot();
        else deactivate = true;
    }

    public void step() {
        switch (state) {
            case LOOK: {
                if (deactivate) deactivateRobot();
                else {
                    round++;
                    activeRounds++;
                    look();
                }
                break;
            }
            case COMPUTE: {
                compute();
                break;
            }
            case MOVE: {
                move();
                break;
            }
        }
    }

    private void deactivateRobot() {
        state = RobotState.INACTIVE;
        deactivate = false;
    }

    private void activateRobot() {
        state = RobotState.LOOK;
        activeRounds = 0;
    }


    protected abstract void look();

    protected abstract void compute();

    protected abstract void move();

}
