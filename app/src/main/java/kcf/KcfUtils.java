package kcf;

import kcf.grid.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class KcfUtils {
    private KcfUtils() {}

    public static Point inverse(Point point) {
        return new Point(-point.x, point.y);
    }

    public static Direction inverse(Direction direction) {
        if (direction == null) return null;
        switch (direction) {
            case L: return Direction.R;
            case R: return Direction.L;
            default: return direction;
        }
    }

    public static Circle inverse(Circle circle) {
        return new Circle(inverse(circle.center), circle.radius);
    }

    public static List<Point> inverse(List<Point> points) {
        return Collections.unmodifiableList(points.stream()
                .map(KcfUtils::inverse)
                .collect(Collectors.toList()));
    }

    public static List<Circle> inverseCircles(List<Circle> circles) {
        return Collections.unmodifiableList(circles.stream()
                .map(KcfUtils::inverse)
                .collect(Collectors.toList()));
    }

    /**
     * Returns the point with the largest configuration rank which does
     * not have a symmetric image about the y-axis in points.
     * It is assumed that points are unique and the configuration rank is
     * calculated based on null orientation.
     * @param points list of points.
     * @return asymmetric point with the largest CR or null if points is symmetric.
     */
    public static Point getAsymmetricPoint(List<Point> points) {
        HashSet<Point> pointSet = new HashSet<>();
        for (Point point : points) {
            if (point.x == 0) continue;
            if (pointSet.contains(inverse(point)))
                pointSet.remove(inverse(point));
            else pointSet.add(point);
        }
        if (pointSet.isEmpty()) return null;
        return Collections.max(pointSet, new ConfigurationRank(null));
    }


    /**
     * Returns the circle with the largest configuration rank which does
     * not have a symmetric image about the y-axis in F.
     * It is assumed that there are no concentric circles in F and the
     * configuration rank is calculated based on null orientation.
     * Returns null if F is symmetric.
     * @param F list of circles.
     * @return asymmetric circle with the largest CR or null if F is symmetric.
     */
    public static Circle getAsymmetricCircle(List<Circle> F) {
        HashMap<Point, Circle> centerMap = new HashMap<>();
        for (Circle circle : F) {
            if (circle.center.x == 0) continue;
            if (centerMap.containsKey(inverse(circle.center)))
                centerMap.remove(inverse(circle.center));
            else centerMap.put(circle.center, circle);
        }
        if (centerMap.isEmpty()) return null;
        Point center = Collections.max(centerMap.keySet(), new ConfigurationRank(null));
        return centerMap.get(center);
    }

    public static boolean isSolved(KcfConfig config) {
        // check robot positions are unique
        if (config.R.size() != new HashSet<>(config.R).size())
            throw new IllegalArgumentException("R has duplicates");

        HashMap<Point, Integer> map = new HashMap<>();
        for (int i = 0; i < config.F.size(); i++) {
            for (Point p : config.F.get(i).getPointsOnCircle())
                map.put(p, i);
        }

        int[] count = new int[config.F.size()];

        for (Point r : config.R) {
            if (!map.containsKey(r)) return false;
            count[map.get(r)]++;
        }

        int k = config.R.size() / config.F.size();

        for (int c : count)
            if (c != k) return false;
        return true;
    }

    public static boolean isUnsolvable(KcfConfig config) {
        int k = config.R.size() / config.F.size();
        if (getAsymmetricPoint(config.R) != null) return false;
        if (getAsymmetricCircle(config.F) != null) return false;
        if (config.R.stream().anyMatch(r -> r.x == 0)) return false;
        if (k % 2 == 1 && config.F.stream().anyMatch(c -> c.center.x == 0)) return true;
        return config.F.stream().anyMatch(c -> c.center.x == 0 && c.getPointsOnCircle().size() == k);
    }
}
