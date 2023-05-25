package kcf.gui.canvas;

public class Viewport {
    public static final double MIN_UNIT_VALUE = 8.0;

    // width and height of the canvas
    private int width, height;

    // coordinates of the top left corner of the screen
    private double x, y;

    // number of pixels that correspond to 1 unit
    private double unit;

    public Viewport(int width, int height) {
        this.width = width;
        this.height = height;
        this.unit = 2 * MIN_UNIT_VALUE;
        this.x = 0;
        this.y = 0;
        offsetPixel(-width / 2, -height / 2);
    }

    // get methods

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double unit() {
        return unit;
    }


    // set methods

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setUnit(double unit) {
        this.unit = Math.max(MIN_UNIT_VALUE, unit);
    }


    // offset

    public void offsetPixel(int xPixels, int yPixels) {
        offset(xPixels / unit, -yPixels / unit);
    }

    public void offset(double x, double y) {
        this.x += x;
        this.y += y;
    }


    // conversion methods

    public int xToPixels(double x) {
        return (int)((x - this.x) * unit);
    }

    public int yToPixels(double y) {
        return (int)((this.y - y) * unit);
    }

    public double xFromPixels(int xPixels) {
        return xPixels / unit + this.x;
    }

    public double yFromPixels(int yPixels) {
        return this.y - yPixels / unit;
    }


    // util methods
    public void zoom(double unit) {
        offsetPixel(width / 2, height / 2);
        setUnit(unit);
        offsetPixel(-width / 2, -height / 2);
    }
}
