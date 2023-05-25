package kcf;

import kcf.gui.MainWindow;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow("K Circle Formation"));
    }
}
