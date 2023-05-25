package kcf.gui.canvas;

import java.util.LinkedList;

public class EventQueue {
    private final LinkedList<Event> queue = new LinkedList<>();

    public synchronized void enqueue(Event e) {
        queue.addLast(e);
    }

    public synchronized Event dequeue() {
        return queue.removeFirst();
    }

    public synchronized int size() {
        return queue.size();
    }
}
