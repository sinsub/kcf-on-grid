package kcf.grid;

import java.util.Objects;

public class Point implements Comparable<Point> {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Point that) {
        if (this.y != that.y)
            return Integer.compare(this.y, that.y);
        return Integer.compare(this.x, that.x);
    }

    public Point add(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    public Point add(Point that) {
        return add(that.x, that.y);
    }

    public Point subtract(int dx, int dy) {
        return new Point(x - dx, y - dy);
    }

    public Point subtract(Point that) {
        return subtract(that.x, that.y);
    }

    public double distance(Point that) {
        return Math.sqrt((this.x - that.x) * (this.x - that.x)
                + (this.y - that.y) * (this.y - that.y));
    }
}
