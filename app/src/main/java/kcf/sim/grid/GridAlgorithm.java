package kcf.sim.grid;

import kcf.grid.Direction;
import kcf.grid.Point;

public interface GridAlgorithm<C> {
    Direction compute(C config, Point position);
}
