package kcf;

import kcf.grid.Circle;
import kcf.grid.Point;

import java.util.List;

public class KcfConfig {
    public final List<Point> R;
    public final List<Circle> F;
    public final KcfConfig inverse;

    public KcfConfig(List<Point> R, List<Circle> F, List<Point> _R, List<Circle> _F) {
        this.R = R;
        this.F = F;
        this.inverse = new KcfConfig(_R, _F, this);
    }

    public KcfConfig(List<Point> R, List<Circle> F, KcfConfig inverse) {
        this.R = R;
        this.F = F;
        this.inverse = inverse;
    }

    @Override
    public String toString() {
        return '{' + R.toString() + ", " + F.toString() +'}';
    }
}
