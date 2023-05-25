package kcf.gui;

import kcf.gui.canvas.DrawUtils;
import kcf.gui.canvas.Entity;
import kcf.gui.canvas.Viewport;

import java.awt.*;

public class GridBackground implements Entity {
    private static final Stroke gridLines = DrawUtils.LIGHT_STROKE;
    private static final Stroke axisLines = DrawUtils.MEDIUM_STROKE;
    private static final Color gridLineColor = new Color(175, 175, 175);

    @Override
    public void update() {}

    @Override
    public void render(Graphics2D g2D, Viewport viewport) {
        g2D.setColor(gridLineColor);
        for (int i = (int)viewport.x(); viewport.xToPixels(i) <= viewport.width(); i++) {
            if (i == 0) g2D.setStroke(axisLines);
            g2D.drawLine(viewport.xToPixels(i), 0, viewport.xToPixels(i), viewport.height());
            if (i == 0) g2D.setStroke(gridLines);
        }
        for (int i = (int)viewport.y(); viewport.yToPixels(i) <= viewport.height(); i--) {
            if (i == 0) g2D.setStroke(axisLines);
            g2D.drawLine(0, viewport.yToPixels(i), viewport.width(), viewport.yToPixels(i));
            if (i == 0) g2D.setStroke(gridLines);
        }
    }
}
