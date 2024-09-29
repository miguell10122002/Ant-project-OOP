package main;

import java.util.PriorityQueue;

class PEC {
    private PriorityQueue<Event> eventQueue;

    public PEC() {
        eventQueue = new PriorityQueue<>();
    }

    public void scheduleEvent(Event event) {
        eventQueue.add(event);
    }

    public Event getNextEvent() {
        return eventQueue.poll();
    }

    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    public int getSize() {
        return eventQueue.size();
    }
}
