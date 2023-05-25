package kcf;

import kcf.grid.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KcfSetup {
    public final List<Point> R;
    public final List<Circle> F;

    public KcfSetup(List<Point> R, List<Circle> F) {
        int n = R.size();
        int m = F.size();

        if (n == 0) throw new IllegalArgumentException("n is 0");
        if (m == 0) throw new IllegalArgumentException("m is 0");
        if (n % m != 0) throw new IllegalArgumentException("n is not divisible by m");

        int k = n / m;

        // checks for robot positions
        if (!GridUtils.unique(R))
            throw new IllegalArgumentException("robot positions not unique");


        // checks for circles
        for (Circle c : F)
            if (c.getPointsOnCircle().size() < k)
                throw new IllegalArgumentException("circle " + c + " cannot fit k robots");

        List<Point> centers = F.stream().map(c -> c.center).collect(Collectors.toList());
        if (!GridUtils.unique(centers))
            throw new IllegalArgumentException("centers not unique");

        if (GridUtils.overlap(F))
            throw new IllegalArgumentException("circles overlap");


        Point origin = GridUtils.centerOfMass(centers);
        if (origin != GridUtils.POINT_NULL) {
            R = R.stream().map(p -> p.subtract(origin)).collect(Collectors.toList());
            F = F.stream().map(c -> new Circle(c.center.subtract(origin), c.radius))
                    .collect(Collectors.toList());
        }

        this.R = Collections.unmodifiableList(R);
        this.F = Collections.unmodifiableList(F);
    }

    @Override
    public String toString() {
        int n = R.size(), m = F.size(), k = n / m;
        return "KcfSetup{k: " + k+ ", n: " + n + ", m: " + m + "}";
    }
}
