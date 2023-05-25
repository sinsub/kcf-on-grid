package kcf.gui;

import kcf.gui.canvas.Entity;
import kcf.gui.canvas.Viewport;
import kcf.sim.Simulation;
import kcf.sim.SimulationRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationControlPanel<K extends  Simulation> extends JPanel implements ActionListener, ChangeListener, Entity {
    private static final int DELAY_MIN = 1;
    private static final int DELAY_MAX = 20;
    private static final int DELAY_INIT = 10;

    private K simulation;
    private SimulationRenderer<K> renderer;

    private boolean paused;
    private int delay;
    private int framesSinceLastUpdate;

    private final JButton playBtn;
    private final JButton pauseBtn;
    private final JSlider delaySlider;

    public SimulationControlPanel() {
        this.paused = true;
        this.delay = DELAY_INIT;
        this.framesSinceLastUpdate = 0;

        playBtn = new JButton("Play");
        pauseBtn = new JButton("Pause");
        delaySlider = new JSlider(JSlider.HORIZONTAL, DELAY_MIN, DELAY_MAX, delay);
        delaySlider.setMajorTickSpacing(5);
        delaySlider.setMinorTickSpacing(1);

        playBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        delaySlider.addChangeListener(this);

        this.setLayout(new FlowLayout());
        this.add(playBtn);
        this.add(pauseBtn);
        this.add(delaySlider);
    }

    public SimulationControlPanel(SimulationRenderer<K> renderer) {
        this();
        this.renderer = renderer;
    }

    public void setSimulation(K simulation) {
        pauseBtnClickHandler();
        this.simulation = simulation;
    }

    public void setRenderer(SimulationRenderer<K> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playBtn) {
            playBtnClickHandler();
        } else if (e.getSource() == pauseBtn) {
            pauseBtnClickHandler();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == delaySlider) {
            this.delay = delaySlider.getValue();
        }
    }

    private void playBtnClickHandler() {
        playBtn.setEnabled(false);
        pauseBtn.setEnabled(true);
        paused = false;
    }

    private void pauseBtnClickHandler() {
        playBtn.setEnabled(true);
        pauseBtn.setEnabled(false);
        paused = true;
    }

    @Override
    public void update() {
        if (paused || simulation == null) return;
        if (framesSinceLastUpdate < delay) {
            framesSinceLastUpdate++;
            return;
        }
        framesSinceLastUpdate = 0;
        simulation.step();
    }

    @Override
    public void render(Graphics2D g2D, Viewport viewport) {
        if (simulation == null || renderer == null) return;
        renderer.render(g2D, viewport, simulation);
    }
}
