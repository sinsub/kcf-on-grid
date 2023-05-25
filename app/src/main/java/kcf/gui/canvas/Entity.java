package kcf.gui.canvas;

import java.awt.*;

public interface Entity {
    void update();
    void render(Graphics2D g2D, Viewport viewport);
}
