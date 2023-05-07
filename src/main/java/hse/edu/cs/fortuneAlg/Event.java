package hse.edu.cs.fortuneAlg;

import java.lang.reflect.Type;

public class Event implements Comparable<Event> {
    enum EventType{
        Point,
        Circle
    }

    double y;
    private final EventType type;
    private Point point;
    private InitPoint initPoint;
    private boolean isValid;
    private Arc arc;


    Event(InitPoint initPoint) {
        this.initPoint = initPoint;
        this.y = initPoint.getPoint().y;
        this.type = EventType.Point;
        this.isValid = true;
    }

    Event(double y, Point point, Arc arc) {
        this.y = y;
        this.point = point;
        this.arc = arc;
        this.type = EventType.Circle;
        this.isValid = true;
    }

    boolean isValid() {
        return this.isValid;
    }

    void makeInvalid() {
        this.isValid = false;
    }

    EventType getType() {
        return type;
    }

    InitPoint getInitPoint() {
        return initPoint;
    }
    Point getPoint() { return point; }

    Arc getArc() { return arc; }

    @Override
    public int compareTo(Event other) {
        //if (this.type == EventType.Point && other.type == EventType.Point && Double.compare(this.y, other.y) == 0)
        //    return Double.compare(this.initPoint.getPoint().x, other.initPoint.getPoint().x);
        return Double.compare(this.y, other.y) * -1;
    }
}
