package kcf;

import kcf.grid.Point;
import kcf.sim.grid.GridAlgorithm;
import kcf.sim.grid.GridEnv;
import kcf.sim.grid.GridRobot;
import kcf.sim.robot.RobotState;

public class KcfRobot extends GridRobot<KcfConfig> {
    private final boolean orientation;

    public KcfRobot(Point position, GridEnv<KcfConfig> gridEnv, GridAlgorithm<KcfConfig> algorithm, boolean orientation) {
        super(position, gridEnv, algorithm);
        this.orientation = orientation;
    }

    public boolean getOrientation() {
        return orientation;
    }

    @Override
    protected void compute() {
        if (orientation) super.compute();
        else {
            nextMove = KcfUtils.inverse(algorithm.compute(config.inverse, KcfUtils.inverse(position)));
            state = RobotState.MOVE;
        }
    }

    @Override
    public String toString() {
        return "KcfR{" + position + ", " + (orientation ? '+' : '-') + ", " + getActiveRounds() + ", " + state + '}';
    }
}
