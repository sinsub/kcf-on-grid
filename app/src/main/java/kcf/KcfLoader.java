package kcf;

import kcf.grid.Circle;
import kcf.grid.Point;
import kcf.sim.grid.FileFormatException;
import kcf.sim.SimulationLoader;
import kcf.sim.grid.GridAlgorithm;
import kcf.sim.scheduler.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class KcfLoader extends SimulationLoader<KcfSimulation, KcfSetup> {
    private GridAlgorithm<KcfConfig> algorithm;

    public KcfLoader(Scheduler scheduler, GridAlgorithm<KcfConfig> algorithm) {
        super(scheduler);
        this.algorithm = algorithm;
    }

    public void setAlgorithm(GridAlgorithm<KcfConfig> algorithm) {
        this.algorithm = algorithm;
    }

    public KcfSetup loadFromFile(File file) throws FileNotFoundException {
        try (Scanner in = new Scanner(file)) {
            List<Point> rp = new ArrayList<>();
            List<Circle> c = new ArrayList<>();

            int m = Integer.parseInt(in.nextLine());
            while (m-- > 0) {
                Scanner ls = new Scanner(in.nextLine());
                c.add(new Circle(new Point(ls.nextInt(), ls.nextInt()), ls.nextInt()));
            }

            int n = Integer.parseInt(in.nextLine());
            while (n-- > 0) {
                Scanner ls = new Scanner(in.nextLine());
                rp.add(new Point(ls.nextInt(), ls.nextInt()));
            }

            return new KcfSetup(rp, c);
        } catch (NoSuchElementException e) {
            throw new FileFormatException("Reached end of file");
        } catch (NumberFormatException e) {
            throw new FileNotFoundException("Expected a number");
        }
    }

    @Override
    public KcfSimulation getSimulation(KcfSetup setup) {
        return new KcfSimulation(setup, algorithm, scheduler);
    }

}
