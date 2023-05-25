package kcf.sim.grid;

import kcf.grid.Direction;
import kcf.grid.GridUtils;
import kcf.grid.Point;
import kcf.sim.robot.Robot;
import kcf.sim.robot.RobotState;

public class GridRobot<C> extends Robot {
    private final GridEnv<C> gridEnv;
    protected Point position;
    protected GridAlgorithm<C> algorithm;

    protected C config;
    protected Direction nextMove;

    public GridRobot(Point position, GridEnv<C> gridEnv,  GridAlgorithm<C> algorithm) {
        this.position = position;
        this.gridEnv = gridEnv;
        this.algorithm = algorithm;
    }

    public Point getPosition() {
        return position;
    }

    public GridAlgorithm<C> getAlgorithm() {
        return algorithm;
    }


    @Override
    protected void look() {
        config = gridEnv.getConfig();
        System.out.println(this);
        state = RobotState.COMPUTE;
    }

    @Override
    protected void compute() {
        nextMove = algorithm.compute(config, position);
        System.out.println(this + ": " + nextMove);
        state = RobotState.MOVE;
    }

    @Override
    protected void move() {
        if (nextMove == null) state = RobotState.LOOK;
        else {
            Point nextPosition = position.add(GridUtils.directionToPoint(nextMove));
            if (gridEnv.move(getId(), nextPosition)) {
                System.out.println(this + ": " + nextPosition);
                position = nextPosition;
                state = RobotState.LOOK;
            }
        }
    }
}
