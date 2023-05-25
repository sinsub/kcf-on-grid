package kcf.gui;

import kcf.*;
import kcf.gui.canvas.Canvas;
import kcf.sppf.SelectPartitionPatternFormation;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(String title) {
        super(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 600));

        this.setLayout(new BorderLayout());

        Canvas canvas = new Canvas();
        canvas.addEntity(new GridBackground());
        this.add(canvas, BorderLayout.CENTER);

        SimulationControlPanel<KcfSimulation> simulationControlPanel =
                new SimulationControlPanel<>(new KcfSimulationRenderer());
        canvas.addEntity(simulationControlPanel);

        SimulationLoaderPanel<KcfSimulation, KcfSetup> simulationLoaderPanel = new SimulationLoaderPanel<>(
                new KcfLoader(null, new SelectPartitionPatternFormation()),
                simulationControlPanel
        );


        JPanel bottomPanel = new JPanel();

        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(simulationLoaderPanel);
        bottomPanel.add(simulationControlPanel);

        this.add(bottomPanel, BorderLayout.SOUTH);

        this.setVisible(true);

    }
}
