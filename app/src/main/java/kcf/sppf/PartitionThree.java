package kcf.sppf;

import kcf.ConfigurationRank;
import kcf.KcfConfig;
import kcf.KcfUtils;
import kcf.grid.Direction;
import kcf.grid.Point;

import java.util.Collections;
import java.util.List;

public class PartitionThree {
    // input
    private final KcfConfig config;
    private final List<Point> S;
    private final Point rk;

    // output
    private Direction dk;
    private Configuration configuration;

    private int halfPlane1 = 0, halfPlane2 = 0, axis = 0;

    public PartitionThree(KcfConfig config, List<Point> s, Point rk) {
        this.config = config;
        S = s;
        this.rk = rk;
        solve();
    }

    public Direction getDk() {
        return dk;
    }

    public boolean isC8() {
        return configuration == Configuration.C8;
    }


    private void solve() {
        for (Point r : config.R) {
            if (r.x == 0) axis++;
            else if (r.x > 0) halfPlane1++;
            else halfPlane2++;
        }

        for (Point s : S) {
            if (s.x == 0) axis--;
            else if (s.x > 0) halfPlane1--;
            else halfPlane2--;
        }

        configuration = findConfiguration();

        if (isC8()) return;

        switch (configuration) {
            case C1: case C2: case C3: case C4: {
                if (halfPlane1 > 0) dk = new MoveToAxis(config.R, rk, halfPlane1, true).getDk();
                else dk = new MoveToAxis(config.R, rk, halfPlane2, false).getDk();
                break;
            }
            case C5: {
                if (halfPlane1 < 0) dk = new MoveToHalfPlane(config.R, rk, axis, true).getDk();
                else dk = new MoveToHalfPlane(config.R, rk, axis, false).getDk();
                break;
            }
            case C6: {
                if (halfPlane1 == halfPlane2) dk = new MoveToHalfPlane(config.R, rk, 1, true).getDk();
                else if (halfPlane1 > halfPlane2) dk = new MoveToHalfPlane(config.R, rk, -halfPlane1, true).getDk();
                else dk = new MoveToHalfPlane(config.R, rk, -halfPlane2, false).getDk();
                break;
            }
            case C7: {
                if (halfPlane1 < halfPlane2) dk = new MoveToAxis(config.R, rk, halfPlane1, true).getDk();
                else if (halfPlane2 < halfPlane1) dk = new MoveToAxis(config.R, rk, halfPlane2, false).getDk();
                else if (config.R.stream().anyMatch(r -> r.x == 0))
                    dk = new MoveToHalfPlane(config.R, rk, 1, true).getDk();
                else {
                    Point S_asymmetric = KcfUtils.getAsymmetricPoint(S);
                    if (S_asymmetric != null) {
                        dk = new MoveToAxis(config.R, rk, 1, S_asymmetric.x > 0).getDk();
                        break;
                    }
                    Point R_asymmetric = KcfUtils.getAsymmetricPoint(config.R);
                    if (R_asymmetric != null) {
                        Point r_max = Collections.max(config.R, new ConfigurationRank(R_asymmetric.x > 0));
                        if (!R_asymmetric.equals(r_max)) {
                            if (rk.equals(r_max)) dk = Direction.U;
                        }
                        else if (R_asymmetric.x > 0) dk = new MoveToAxis(config.R, rk, halfPlane2, false).getDk();
                        else dk = new MoveToAxis(config.R, rk, halfPlane1, true).getDk();
                    }
                }
                break;
            }
        }

    }

    private Configuration findConfiguration() {
        if (halfPlane1 == 0 && axis == 0 && halfPlane2 == 0)
            return Configuration.C8;
        if (halfPlane1 > 0 && axis < 0 && halfPlane2 > 0)
            return Configuration.C7;
        if (halfPlane1 < 0 && axis > 0 && halfPlane2 < 0)
            return Configuration.C6;
        if (axis > 0 && ((halfPlane1 == 0 && halfPlane2 < 0) || (halfPlane1 < 0 && halfPlane2 == 0)))
            return Configuration.C5;
        if (axis < 0 && ((halfPlane1 > 0 && halfPlane2 < 0) || (halfPlane1 < 0 && halfPlane2 > 0)))
            return Configuration.C4;
        if (axis < 0 && ((halfPlane1 > 0 && halfPlane2 == 0) || (halfPlane1 == 0 && halfPlane2 > 0)))
            return Configuration.C3;
        if (axis == 0 && ((halfPlane1 > 0 && halfPlane2 < 0) || (halfPlane1 < 0 && halfPlane2 > 0)))
            return Configuration.C2;
        if (axis > 0 && ((halfPlane1 > 0 && halfPlane2 < 0) || (halfPlane1 < 0 && halfPlane2 > 0)))
            return Configuration.C1;
        throw new IllegalStateException("Not a part of any of the 8 configurations!");
    }


    private enum Configuration {
        C1, C2, C3, C4, C5, C6, C7, C8
    }
}
