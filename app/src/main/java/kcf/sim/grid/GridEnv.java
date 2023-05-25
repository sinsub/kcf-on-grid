package kcf.sim.grid;

import kcf.grid.Point;

import java.util.UUID;

public interface GridEnv<C> {
    C getConfig();
    boolean move(UUID robotId, Point position);
}
