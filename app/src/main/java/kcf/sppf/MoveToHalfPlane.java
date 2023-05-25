package kcf.sppf;

import kcf.KcfUtils;
import kcf.grid.Direction;
import kcf.grid.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MoveToHalfPlane {
    // input
    private final List<Point> R;
    private final Point rk;
    private final int e;
    private final boolean orientation;

    // output
    private Direction dk;

    public MoveToHalfPlane(List<Point> R, Point rk, int e, boolean orientation) {
        this.R = R;
        this.rk = rk;
        this.e = e;
        this.orientation = orientation;
        this.dk = null;
        solve();
    }

    public Direction getDk() {
        return dk;
    }

    private void solve() {
        // no need to solve if rk is not on y-axis
        if (rk.x != 0) return;

        List<Point> R = new ArrayList<>(this.R);
        Point rk = this.rk;

        // fix orientation
        if (!orientation) R = KcfUtils.inverse(R);


        List<Point> T = R.stream().filter(r -> r.x == 0).sorted().limit(e).collect(Collectors.toList());
        Collections.reverse(T);

        // if rk is not in T no need to compute further
        if (rk.compareTo(T.get(0)) > 0) return;

        HashSet<Point> RSet = new HashSet<>(R);
        int yd = Integer.MAX_VALUE;

        for (Point r : T) {
            int x = r.x, y = r.y;

            yd = Math.min(yd - 1, y);
            while (RSet.contains(new Point(1, yd))) yd--;

            if (rk.equals(r)) {
                if (y > yd && !RSet.contains(new Point(x, y - 1))) dk = Direction.D;
                else if (y == yd) dk = Direction.R;
                break;  // no need to compute for the rest
            }

        }

        // fix orientation
        if (!orientation) dk = KcfUtils.inverse(dk);

    }
}
