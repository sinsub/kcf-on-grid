package kcf.sppf;

import kcf.KcfUtils;
import kcf.grid.Direction;
import kcf.grid.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MoveToAxis {
    // input
    private final List<Point> R;
    private final Point rk;
    private final int e;
    private final boolean orientation;

    // output
    private Direction dk;

    public MoveToAxis(List<Point> R, Point rk, int e, boolean orientation) {
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
        // no need to solve if rk is not in the positive half plane
        if (orientation && rk.x <= 0) return;
        if (!orientation && rk.x >= 0) return;

        List<Point> R = new ArrayList<>(this.R);
        Point rk = this.rk;

        // fix orientation
        if (!orientation) {
            R = KcfUtils.inverse(R);
            rk = KcfUtils.inverse(rk);
        }

        List<Point> T = R.stream().filter(r -> r.x > 0).sorted().limit(e).collect(Collectors.toList());
        Collections.reverse(T);

        // if rk is not in T no need to compute further
        if (rk.compareTo(T.get(0)) > 0) return;

        HashSet<Point> RSet = new HashSet<>(R);
        int yd = Integer.MAX_VALUE;

        for (int i = 0; i < T.size(); i++) {
            Point r = T.get(i);
            int x = r.x, y = r.y;

            yd = Math.min(yd - 1, y);
            while (RSet.contains(new Point(0, yd))) yd--;

            if (rk.equals(r)) {
                if (y > yd && (i == T.size() - 1 || T.get(i + 1).compareTo(new Point(x, y-1)) < 0))
                    dk = Direction.D;
                else if ((x > 1 || y == yd) && !RSet.contains(new Point(x-1, y)))
                    dk = Direction.L;
                break;  // no need to compute for the rest
            }

        }

        // fix orientation
        if (!orientation) dk = KcfUtils.inverse(dk);

    }
}
