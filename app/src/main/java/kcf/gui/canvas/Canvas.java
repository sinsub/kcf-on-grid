package kcf.gui.canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    public static final int MS_PER_FRAME = 1000 / 60;

    private final List<Entity> entities;
    private final Viewport viewport;
    private final EventQueue eventQueue;

    private boolean panEnabled;
    private boolean zoomEnabled;

    public Canvas() {
        this.setBackground(new Color(240, 240, 240));
        this.setDoubleBuffered(true);

        this.entities = new ArrayList<>();
        this.viewport = new Viewport(this.getWidth(), this.getHeight());
        this.eventQueue = new EventQueue();
        new DrawLoop(MS_PER_FRAME) {
            @Override
            protected void update() {
                Canvas.this.update();
            }

            @Override
            protected void render() {
                Canvas.this.repaint();
            }
        };

        this.panEnabled = true;
        this.zoomEnabled = true;

        this.addMouseMotionListener(new PanZoomHandler());
        this.addMouseWheelListener(new PanZoomHandler());
        this.addComponentListener(new ResizeHandler());
    }

    public boolean addEntity(Entity entity) {
        return entities.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return entities.remove(entity);
    }

    public Viewport viewport() {
        return viewport;
    }

    public boolean isPanEnabled() {
        return panEnabled;
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public void setPanEnabled(boolean panEnabled) {
        this.panEnabled = panEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }

    private void update() {
        int sz = eventQueue.size();
        while (sz-- > 0)
            eventQueue.dequeue().consume();

        for (Entity entity : entities)
            entity.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Entity entity : entities)
            entity.render(g2D, viewport);
        g2D.dispose();
    }

    private class PanZoomHandler implements MouseMotionListener, MouseWheelListener {
        private Point cursor;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (panEnabled && cursor != null) {
                int diffX = e.getPoint().x - cursor.x, diffY = e.getPoint().y - cursor.y;
                eventQueue.enqueue(() -> viewport.offsetPixel(-diffX, -diffY));
                cursor = e.getPoint();
            }
            cursor = e.getPoint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            cursor = null;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (zoomEnabled) {
                eventQueue.enqueue(() -> {
                    int n = -e.getWheelRotation();
                    double unit = viewport.unit() * Math.pow(1.1, n);
                    viewport.zoom(unit);
                });
            }
        }
    }

    private class ResizeHandler extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            viewport.setDimensions(getWidth(), getHeight());
        }
    }
}
