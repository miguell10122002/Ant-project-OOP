package main;

abstract class Event implements Comparable<Event> {
    public Double time;

    public Event(Double time) {
        this.time = time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}
