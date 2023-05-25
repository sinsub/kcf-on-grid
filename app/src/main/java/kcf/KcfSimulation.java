package kcf;

import kcf.grid.Circle;
import kcf.grid.Point;
import kcf.sim.Simulation;
import kcf.sim.grid.GridAlgorithm;
import kcf.sim.grid.GridEnv;
import kcf.sim.scheduler.Scheduler;

import java.util.*;
import java.util.stream.Collectors;

public class KcfSimulation implements Simulation, GridEnv<KcfConfig> {
    private final List<KcfRobot> robots;
    private final Scheduler scheduler;
    private KcfSimulationState state;
    private final HashMap<UUID, KcfRobot> uuidToIndexMap;

    private final Set<Point> R;
    private final Set<Point> _R;
    private final List<Circle> F;
    private final List<Circle> _F;

    private KcfConfig currentConfig;
    boolean dirtyConfig;

    public KcfSimulation(KcfSetup setup, GridAlgorithm<KcfConfig> algorithm, Scheduler scheduler) {
        F = setup.F;
        _F = KcfUtils.inverseCircles(setup.F);
        R = new HashSet<>(setup.R);
        _R = new HashSet<>(KcfUtils.inverse(setup.R));
        updateConfig();

        robots = Collections.unmodifiableList(setup.R.stream()
                .map(r -> new KcfRobot(r, this, algorithm, Math.random() < 0.5))
                .collect(Collectors.toList()));

        uuidToIndexMap = new HashMap<>();
        for (KcfRobot robot : robots) uuidToIndexMap.put(robot.getId(), robot);

        this.scheduler = scheduler;
        scheduler.setRobots(robots);

        updateState();
    }

    public KcfSimulationState getState() {
        return state;
    }

    public List<KcfRobot> getRobots() {
        return robots;
    }

    public List<Circle> getF() {
        return F;
    }

    @Override
    public void step() {
        if (state == KcfSimulationState.RUNNING)
            scheduler.schedule();
    }

    @Override
    public KcfConfig getConfig() {
        if (dirtyConfig) updateConfig();
        return currentConfig;
    }

    @Override
    public boolean move(UUID robotId, Point position) {
        if (state != KcfSimulationState.RUNNING) return false;
        KcfRobot robot = uuidToIndexMap.get(robotId);
        Point oldPosition = robot.getPosition();
        if (position.equals(oldPosition)) return true;
        if (R.contains(position)) {
            state = KcfSimulationState.ERROR;
            System.out.println(robot + " tried to move to occupied position " + position);
            return false;
        }
        R.remove(oldPosition);
        R.add(position);
        _R.remove(KcfUtils.inverse(oldPosition));
        _R.add(KcfUtils.inverse(position));
        dirtyConfig = true;
        updateState();
        return true;
    }


    private void updateConfig() {
        currentConfig = new KcfConfig(
                Collections.unmodifiableList(new ArrayList<>(R)),
                F,
                Collections.unmodifiableList(new ArrayList<>(_R)),
                _F
        );
        dirtyConfig = false;
    }

    private void updateState() {
        if (state == KcfSimulationState.ERROR) return;
        if (KcfUtils.isUnsolvable(getConfig())) state = KcfSimulationState.UNSOLVABLE;
        else if (KcfUtils.isSolved(getConfig())) state = KcfSimulationState.SOLVED;
        else state = KcfSimulationState.RUNNING;
    }
}
