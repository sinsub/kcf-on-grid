package kcf.gui.canvas;

import java.awt.*;

public class DrawUtils {
    public static final BasicStroke LIGHT_STROKE = new BasicStroke(1);
    public static final BasicStroke MEDIUM_STROKE = new BasicStroke(2);
    public static final BasicStroke HEAVY_STROKE = new BasicStroke(3);

    private DrawUtils() {}

    /**
     * Draws a square centered at (x, y) with side length d
     */
    public static void drawSquare(
            Graphics2D g2D,
            Viewport viewport,
            Color color,
            Stroke stroke,
            double x, double y, int d
    ) {
        Color curC = g2D.getColor();
        Stroke curS = g2D.getStroke();
        g2D.setColor(color);
        g2D.setStroke(stroke);
        int px = viewport.xToPixels(x), py = viewport.yToPixels(y);
        g2D.drawRect(px - d / 2, py - d / 2, d, d);
        g2D.setColor(curC);
        g2D.setStroke(curS);
    }

    /**
     * Fill a square centered at (x, y) with side length d
     */
    public static void fillSquare(
            Graphics2D g2D,
            Viewport viewport,
            Color color,
            double x, double y, int d
    ) {
        Color curC = g2D.getColor();
        g2D.setColor(color);
        int px = viewport.xToPixels(x), py = viewport.yToPixels(y);
        g2D.fillRect(px - d / 2, py - d / 2, d, d);
        g2D.setColor(curC);
    }

    /**
     * Draws a circle centered at (x, y) with diameter d
     */
    public static void drawCircle(
            Graphics2D g2D,
            Viewport viewport,
            Color color,
            Stroke stroke,
            double x, double y, int d
    ) {
        Color curC = g2D.getColor();
        Stroke curS = g2D.getStroke();
        g2D.setColor(color);
        g2D.setStroke(stroke);
        g2D.drawOval(viewport.xToPixels(x) - d / 2, viewport.yToPixels(y) - d / 2, d, d);
        g2D.setColor(curC);
        g2D.setStroke(curS);
    }

    /**
     * Fill a circle centered at (x, y) with diameter d
     */
    public static void fillCircle(
            Graphics2D g2D,
            Viewport viewport,
            Color color,
            double x, double y, int d
    ) {
        Color curC = g2D.getColor();
        g2D.setColor(color);
        g2D.fillOval(viewport.xToPixels(x) - d / 2, viewport.yToPixels(y) - d / 2, d, d);
        g2D.setColor(curC);
    }

    /**
     * Draws an X centered at (x, y) in a square of side length d
     */
    public static void drawX(
            Graphics2D g2D,
            Viewport viewport,
            Color color,
            Stroke stroke,
            double x, double y, int d)
    {
        Color curC = g2D.getColor();
        Stroke curS = g2D.getStroke();
        g2D.setColor(color);
        g2D.setStroke(stroke);
        int px = viewport.xToPixels(x), py = viewport.yToPixels(y);
        g2D.drawLine(px - d / 2, py - d / 2, px + d / 2, py + d / 2);
        g2D.drawLine(px + d / 2, py - d / 2, px - d / 2, py + d / 2);
        g2D.setColor(curC);
        g2D.setStroke(curS);
    }

}
