package kcf.gui;

import kcf.sim.Simulation;
import kcf.sim.SimulationLoader;
import kcf.sim.grid.FileFormatException;
import kcf.sim.scheduler.ASYNC;
import kcf.sim.scheduler.FSYNC;
import kcf.sim.scheduler.SSYNC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;

public class SimulationLoaderPanel<K extends Simulation, S> extends JPanel implements ActionListener, ItemListener {
    private static final String FSYNC = "FSYNC";
    private static final String SSYNC = "SSYNC";
    private static final String ASYNC = "ASYNC";

    private final SimulationLoader<K, S> simulationLoader;
    private S setup;

    private final JButton loadFileButton;
    private final JButton resetSimButton;
    private final JFileChooser fileChooser;
    private final JComboBox<String> schedulerComboBox;

    private final SimulationControlPanel<K> simulationControlPanel;

    public SimulationLoaderPanel(
            SimulationLoader<K, S> simulationLoader,
            SimulationControlPanel<K> simulationControlPanel
    ) {
        this.simulationLoader = simulationLoader;
        this.simulationControlPanel = simulationControlPanel;

        loadFileButton = new JButton("Load File");
        resetSimButton = new JButton("Rest");
        fileChooser = new JFileChooser();
        schedulerComboBox = new JComboBox<>();

        schedulerComboBox.addItem(ASYNC);
        schedulerComboBox.addItem(SSYNC);
        schedulerComboBox.addItem(FSYNC);

        schedulerComboBox.setSelectedItem(ASYNC);
        simulationLoader.setScheduler(new ASYNC());

        loadFileButton.addActionListener(this);
        resetSimButton.addActionListener(this);
        schedulerComboBox.addItemListener(this);

        this.setLayout(new FlowLayout());
        this.add(loadFileButton);
        this.add(schedulerComboBox);
        this.add(resetSimButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadFileButton) {
            int returnVal = fileChooser.showOpenDialog(SimulationLoaderPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    setup = simulationLoader.loadFromFile(fileChooser.getSelectedFile());
                    setSimulation();
                } catch (FileNotFoundException ex)  {
                    JOptionPane.showMessageDialog(SimulationLoaderPanel.this, "File not found exception!");
                } catch (FileFormatException ex) {
                    JOptionPane.showMessageDialog(SimulationLoaderPanel.this,
                            "FileFormatException: " + ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SimulationLoaderPanel.this, ex);
                }
            }
        } else if (e.getSource() == resetSimButton) {
            setSimulation();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == schedulerComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            String s = (String) schedulerComboBox.getSelectedItem();
            if (s == null) return;
            switch (s) {
                case FSYNC: simulationLoader.setScheduler(new FSYNC()); break;
                case SSYNC: simulationLoader.setScheduler(new SSYNC()); break;
                case ASYNC: simulationLoader.setScheduler(new ASYNC()); break;
            }
            setSimulation();
        }
    }

    private void setSimulation() {
        if (setup != null) {
            simulationControlPanel.setSimulation(simulationLoader.getSimulation(setup));
            System.out.println("Simulation loaded: " + setup + " with "
                    + schedulerComboBox.getSelectedItem() + " scheduler");
        }
    }
}
