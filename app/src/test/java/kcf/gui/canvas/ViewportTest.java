package kcf.gui.canvas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewportTest {
    private final int W = 200;
    private final int H = 100;
    private Viewport viewport;

    @BeforeEach
    void beforeEach() {
        viewport = new Viewport(W, H);
    }

    @Test
    void testDimensions() {
        assertEquals(W, viewport.width());
        assertEquals(H, viewport.height());

        viewport.setDimensions(2 * W, 2 * H);
        assertEquals(2 * W, viewport.width());
        assertEquals(2 * H, viewport.height());
    }

    @Test
    void testUnit() {
        final int unit = 30;
        viewport.setUnit(unit);
        assertEquals(unit, viewport.unit());

        viewport.setUnit(Viewport.MIN_UNIT_VALUE - 1);
        assertEquals(Viewport.MIN_UNIT_VALUE, viewport.unit());
    }

    @Test
    void offsetPixel() {
        double x = viewport.x(), y = viewport.y();
        double unit = viewport.unit();
        double xOffset = 3, yOffSet = -10;
        viewport.offsetPixel((int)(unit * xOffset), (int)(unit * yOffSet));
        assertEquals(x + xOffset, viewport.x());
        assertEquals(y - yOffSet, viewport.y());
    }

    @Test
    void offset() {
        double x = viewport.x(), y = viewport.y();
        double xOffset = 3, yOffSet = -10;
        viewport.offset(xOffset, yOffSet);
        assertEquals(x + xOffset, viewport.x());
        assertEquals(y + yOffSet, viewport.y());
    }

    @Test
    void testXToPixels() {
        double x = viewport.x();
        double unit = viewport.unit();

        assertEquals(0, viewport.xToPixels(x));
        assertEquals(2 * unit, viewport.xToPixels(x + 2));
        assertEquals(-2 * unit, viewport.xToPixels(x - 2));
    }

    @Test
    void testYToPixels() {
        double y = viewport.y();
        double unit = viewport.unit();

        assertEquals(0, viewport.yToPixels(y));
        assertEquals(2 * unit, viewport.yToPixels(y - 2));
        assertEquals(-2 * unit, viewport.yToPixels(y + 2));
    }

    @Test
    void testXFromPixels() {
        double x = viewport.x();
        double unit = viewport.unit();

        assertEquals(x, viewport.xFromPixels(0));
        assertEquals(x + 2, viewport.xFromPixels((int)(2 * unit)));
        assertEquals(x - 2, viewport.xFromPixels((int)(-2 * unit)));
    }

    @Test
    void testYFromPixels() {
        double y = viewport.y();
        double unit = viewport.unit();

        assertEquals(y, viewport.yFromPixels(0));
        assertEquals(y + 2, viewport.yFromPixels((int)(-2 * unit)));
        assertEquals(y - 2, viewport.yFromPixels((int)(2 * unit)));
    }

    @Test
    void testZoom() {
        double centerX = getCenterX(viewport);
        double centerY = getCenterY(viewport);

        double unit = 50;
        viewport.zoom(unit);
        assertEquals(unit, viewport.unit());
        assertTrue(checkError(centerX, getCenterX(viewport)));
        assertTrue(checkError(centerY, getCenterY(viewport)));

        unit = Viewport.MIN_UNIT_VALUE - 1;
        viewport.zoom(unit);
        assertEquals(Viewport.MIN_UNIT_VALUE, viewport.unit());
        assertTrue(checkError(centerX, getCenterX(viewport)));
        assertTrue(checkError(centerY, getCenterY(viewport)));
    }


    double getCenterX(Viewport viewport) {
        return viewport.xFromPixels(viewport.width() / 2);
    }

    double getCenterY(Viewport viewport) {
        return viewport.yFromPixels(viewport.height() / 2);
    }

    boolean checkError(double a, double b) {
        return Math.abs(a - b) <= 1.0E-9;
    }
}