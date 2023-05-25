package kcf.sppf;

import kcf.KcfConfig;
import kcf.KcfUtils;
import kcf.grid.Direction;
import kcf.grid.Point;
import kcf.sim.grid.GridAlgorithm;

import java.util.List;

public class SelectPartitionPatternFormation implements GridAlgorithm<KcfConfig> {
    @Override
    public Direction compute(KcfConfig config, Point position) {
        return new Solver(config, position).getDk();
    }

    private static class Solver {
        // input
        private final KcfConfig config;
        private final Point position;

        // output
        private Direction dk;

        // intermediate
        private final int k;

        public Solver(KcfConfig config, Point position) {
            this.config = config;
            this.position = position;
            this.k = config.R.size() / config.F.size();
            solve();
        }

        public Direction getDk() {
            return dk;
        }

        private void solve() {
            if (KcfUtils.isUnsolvable(config) || KcfUtils.isSolved(config)) return;

            List<Point> s = new SelectK(config.F, k).getS();
            PartitionThree partitionThree = new PartitionThree(config, s, position);
            if (!partitionThree.isC8()) dk = partitionThree.getDk();
            else dk = new ThreeAPF(s, config.R, position).getDk();
        }
    }
}
