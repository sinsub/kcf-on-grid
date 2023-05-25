package kcf.grid;

import java.util.HashSet;
import java.util.List;

public class GridUtils {
    public static final Point POINT_U = new Point(0, 1);
    public static final Point POINT_D = new Point(0, -1);
    public static final Point POINT_L = new Point(-1, 0);
    public static final Point POINT_R = new Point(1, 0);
    public static final Point POINT_NULL =  new Point(0, 0);
    public static final Point[] dirs = {POINT_U, POINT_D, POINT_L, POINT_R};

    private GridUtils() {}

    public static Point directionToPoint(Direction dir) {
        if (dir == null) return POINT_NULL;
        switch (dir) {
            case U: return POINT_U;
            case D: return POINT_D;
            case L: return POINT_L;
            case R: return POINT_R;
            default: return POINT_NULL;     // not sure why we need default
        }
    }

    public static Direction pointToDirection(Point point) {
        if (point == null) throw new IllegalArgumentException("point is null");
        if (point.equals(POINT_U)) return Direction.U;
        if (point.equals(POINT_D)) return Direction.D;
        if (point.equals(POINT_L)) return Direction.L;
        if (point.equals(POINT_R)) return Direction.R;
        if (point.equals(POINT_NULL)) return null;
        throw new IllegalArgumentException("point " + point + " cannot be converted to a direction");
    }

    public static Point centerOfMass(Iterable<Point> points) {
        int x = 0, y = 0, n = 0;
        for (Point point : points) {
            x += point.x;
            y += point.y;
            n++;
        }
        return n == 0 ? POINT_NULL :  new Point(x / n, y / n);
    }

    public static boolean overlap(Iterable<Circle> circles) {
        HashSet<Point> points = new HashSet<>();
        for (Circle circle : circles) {
            for (Point point : circle.getPointsOnCircle()) {
                if (!points.add(point))
                    return true;
            }
        }
        return false;
    }

    public static<C> boolean unique(List<C> list) {
        return list.size() == new HashSet<>(list).size();
    }
}
