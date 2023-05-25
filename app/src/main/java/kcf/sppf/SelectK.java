package kcf.sppf;

import kcf.ConfigurationRank;
import kcf.KcfUtils;
import kcf.grid.Circle;
import kcf.grid.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectK {
    // list of circles and k  (Input)
    private final List<Circle> F;
    private final int k;

    // list of selected points (Output)
    private List<Point> S;

    private final ConfigurationRank cr;


    public SelectK(List<Circle> F, int k) {
        this.F = F;
        this.k = k;
        this.S = new ArrayList<>();
        this.cr = new ConfigurationRank(null);
        solve();
        this.S = Collections.unmodifiableList(S);
    }

    public List<Point> getS() {
        return S;
    }

    private void solve() {
        Circle asymmetric = KcfUtils.getAsymmetricCircle(F);
        if (asymmetric == null) solveSymmetric();
        else solveAsymmetric(asymmetric);
    }

    private void solveSymmetric() {
        for (Circle circle : F) {
            if (circle.center.x != 0) {
                addLargestToS(new ArrayList<>(circle.getPointsOnCircle()), k);
            } else if (circle.getPointsOnCircle().size() == k) {
                S.addAll(circle.getPointsOnCircle());
            } else {
                List<Point> pointsNotOnAxis = new ArrayList<>();
                for (Point point : circle.getPointsOnCircle()) {
                    if (point.x != 0) pointsNotOnAxis.add(point);
                    else if (k % 2 == 1 && point.y > circle.center.y) S.add(point);
                }
                addLargestToS(pointsNotOnAxis, k - k % 2);
            }
        }
    }

    private void solveAsymmetric(Circle asymmetric) {
        // agree on the orientation of y-axis
        cr.setOrientation(asymmetric.center.x > 0);

        for (Circle circle : F) {
            addLargestToS(new ArrayList<>(circle.getPointsOnCircle()), k);
        }
    }

    private void addLargestToS(List<Point> points, int count) {
        points.sort(cr);
        Collections.reverse(points);
        for (int i = 0; i < count; i++)
            S.add(points.get(i));
    }
}
