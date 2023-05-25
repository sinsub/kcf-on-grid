package kcf.sppf;

import kcf.KcfUtils;
import kcf.grid.Direction;
import kcf.grid.GridUtils;
import kcf.grid.Point;

import java.util.*;
import java.util.stream.Collectors;

public class ThreeAPF {
    // input
    private final List<Point> S;
    private final List<Point> R;
    private final Point rk;

    //output
    private Direction dk;

    public ThreeAPF(List<Point> S, List<Point> R, Point rk) {
        this.S = S;
        this.R = R;
        this.rk = rk;
        solve();
    }

    public Direction getDk() {
        return dk;
    }

    public void solve() {
        if (rk.x == 0)
            dk = new Axis(
                    S.stream().filter(r -> r.x == 0).collect(Collectors.toList()),
                    R.stream().filter(r -> r.x == 0).collect(Collectors.toList()),
                    rk
            ).getDk();
        else if (rk.x > 0) {
            dk = new HalfPlane(
                    S.stream().filter(r -> r.x > 0).collect(Collectors.toList()),
                    R.stream().filter(r -> r.x > 0).collect(Collectors.toList()),
                    rk
            ).getDk();
        } else {
            dk = KcfUtils.inverse(new HalfPlane(
                    S.stream().filter(r -> r.x < 0).map(KcfUtils::inverse).collect(Collectors.toList()),
                    R.stream().filter(r -> r.x < 0).map(KcfUtils::inverse).collect(Collectors.toList()),
                    KcfUtils.inverse(rk)
            ).getDk());
        }
    }

    public static class Axis {
        private final List<Point> S;
        private final List<Point> R;
        private final Point rk;

        private final Set<Point> RSet;

        private Direction dk;

        public Axis(List<Point> s, List<Point> r, Point rk) {
            S = s;
            R = r;
            this.rk = rk;
            this.RSet = new HashSet<>(R);

            if (S.size() != R.size())
                throw new IllegalArgumentException("size mismatch!");

            solve();
        }

        public Direction getDk() {
            return dk;
        }

        public void solve() {
            Collections.sort(S);
            Collections.sort(R);

            for (int i = 0; i < R.size(); i++) {
                Point r = R.get(i);
                if (!rk.equals(r)) continue;

                Point s = S.get(i);
                if (s.equals(r)) return;
                if (s.compareTo(r) > 0 && !RSet.contains(new Point(r.x, r.y + 1)))
                    dk = Direction.U;
                else if (s.compareTo(r) < 0 && !RSet.contains(new Point(r.x, r.y - 1)))
                    dk = Direction.D;
            }
        }
    }

    public static class HalfPlane {
        private final List<Point> S;
        private final List<Point> R;
        private final Point rk;
        private final int limit;
        private final Set<Point> RSet;

        private Direction dk;

        public HalfPlane(List<Point> s, List<Point> r, Point rk) {
            S = s;
            R = r;
            this.rk = rk;
            this.RSet = new HashSet<>(R);
            this.limit = Math.max(limit(S), limit(R));
            solve();
        }

        public Direction getDk() {
            return dk;
        }

        public void solve() {
            S.sort(lgc);
            R.sort(lgc);

            for (int i = 0; i < R.size(); i++) {
                Point r = R.get(i);
                if (!rk.equals(r)) continue;

                Point s = S.get(i);
                if (s.equals(r)) return;

                if (up(r, s)) {
                    Point r_up = new Point(r.x, r.y + 1);
                    if (lgc.compare(r_up, s) <= 0 && (i == R.size() - 1 || lgc.compare(r_up, R.get(i + 1)) < 0))
                        dk = Direction.U;
                    else if (!RSet.contains(next(r)))
                        dk = GridUtils.pointToDirection(next(r).subtract(r));
                } else {
                    Point r_down = new Point(r.x, r.y - 1);
                    if (lgc.compare(r_down, s) >= 0 && (i == 0 || lgc.compare(r_down, R.get(i - 1)) > 0))
                        dk = Direction.D;
                    else if (!RSet.contains(prev(r)))
                        dk = GridUtils.pointToDirection(prev(r).subtract(r));
                }
            }
        }

        public Point next(Point p) {
            if (p.y % 2 == 0) return p.x == limit ? new Point(p.x, p.y + 1) : new Point(p.x + 1, p.y);
            return p.x == 1 ? new Point(p.x, p.y + 1) : new Point(p.x - 1, p.y);
        }

        public Point prev(Point p) {
            if (p.y % 2 == 0) return p.x == 1 ? new Point(p.x, p.y - 1) : new Point(p.x - 1, p.y);
            return p.x == limit ? new Point(p.x, p.y - 1) : new Point(p.x + 1, p.y);
        }

        public static boolean up(Point r, Point s) {
            if (s.y > r.y) return true;
            if (s.y < r.y) return false;
            if (r.y % 2 == 0) return s.x > r.x;
            return s.x < r.x;
        }


        private static int limit(List<Point> points) {
            return points.stream().map(p -> p.x).max(Integer::compareTo).get();
        }

        private static final Grid lgc = new Grid();

        private static class Grid implements Comparator<Point> {

            @Override
            public int compare(Point o1, Point o2) {
                if (o1.equals(o2)) return 0;
                if (up(o1, o2)) return -1;
                return 1;
            }
        }
    }
}
