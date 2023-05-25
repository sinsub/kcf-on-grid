package kcf;

import kcf.grid.Circle;
import kcf.grid.Point;
import kcf.sim.SimulationRenderer;
import kcf.gui.canvas.DrawUtils;
import kcf.gui.canvas.Viewport;

import java.awt.*;
import java.util.List;


public class KcfSimulationRenderer implements SimulationRenderer<KcfSimulation> {
    private static final Color CIRCLE = new Color(170, 170, 170);
    private static final Color ROBOT_ALIGNED = Color.BLUE;
    private static final Color ROBOT_MISALIGNED = Color.RED;

    @Override
    public void render(Graphics2D g2D, Viewport viewport, KcfSimulation simulation) {
        if (simulation == null) return;
        renderCircles(g2D, viewport, simulation.getF());
        renderRobots(g2D, viewport, simulation.getRobots());

    }

    private void renderCircles(Graphics2D g2D, Viewport viewport, List<Circle> circles) {
        int unit = (int)viewport.unit();
        int robotSize = 2 * unit / 3;
        for (Circle c : circles) {
            DrawUtils.drawX(g2D, viewport, Color.BLACK, DrawUtils.MEDIUM_STROKE, c.center.x, c.center.y, unit);
            for (Point p : c.getPointsOnCircle()) {
                DrawUtils.drawCircle(g2D, viewport, CIRCLE, DrawUtils.MEDIUM_STROKE, p.x, p.y,
                        robotSize + 2 * (int)DrawUtils.MEDIUM_STROKE.getLineWidth());
            }
        }
    }

    private void renderRobots(Graphics2D g2D, Viewport viewport, List<KcfRobot> robots) {
        int unit = (int)viewport.unit();
        int robotSize = 2 * unit / 3;
        for (KcfRobot r : robots) {
            DrawUtils.fillCircle(g2D, viewport, r.getOrientation() ? ROBOT_ALIGNED : ROBOT_MISALIGNED,
                    r.getPosition().x, r.getPosition().y, robotSize);
        }
    }
}
