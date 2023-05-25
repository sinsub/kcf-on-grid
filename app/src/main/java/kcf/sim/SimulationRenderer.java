package kcf.sim;

import kcf.gui.canvas.Viewport;

import java.awt.*;

public interface SimulationRenderer<K extends Simulation> {
    void render(Graphics2D g2D, Viewport viewport, K simulation);
}
