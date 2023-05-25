package kcf;

import kcf.grid.Point;

import java.util.Comparator;

public class ConfigurationRank implements Comparator<Point> {

    private Boolean orientation;

    public ConfigurationRank(Boolean orientation) {
        this.orientation = orientation;
    }

    @Override
    public int compare(Point p1, Point p2) {
        return getRank(p1).compareTo(getRank(p2));
    }

    public Boolean getOrientation() {
        return orientation;
    }

    public void setOrientation(Boolean orientation) {
        this.orientation = orientation;
    }

    public Point getRank(Point p) {
        if (orientation == null) return new Point(Math.abs(p.x), p.y);
        return orientation ? p : new Point(-p.x, p.y);
    }
}