package hse.edu.cs.fortuneAlg;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class FortuneAlgorithm {
    protected final VoronoiDiagram diagram;
    protected final PriorityQueue<Event> events = new PriorityQueue<>();
    protected Beachline beachline = new Beachline();
    protected double beachlineY;

    public FortuneAlgorithm(ArrayList<Point> points) {
        diagram = new VoronoiDiagram(points);
    }

    public VoronoiDiagram getDiagram() {
        return diagram;
    }

    public void constructVoronoiDiagram() {
        for(InitPoint initPoint : diagram.getInitPoints()) {
            events.add(new Event(initPoint));
        }

        while (!events.isEmpty()) {
            processEvent(events.poll());
        }

        boundDiagram();
    }

    protected void processEvent(Event event) {
        if (!event.isValid())
            return;
        beachlineY = event.y;
        if (event.getType() == Event.EventType.Point)
            handleInitPointEvent(event);
        else
            handleCircleEvent(event);
    }

    protected void handleInitPointEvent(Event event) {
        InitPoint initPoint = event.getInitPoint();
        if (beachline.isEmpty()) {
            beachline.setRoot(beachline.createArc(initPoint));
            return;
        }
        // Look for the arc above the initPoint
        Arc arcAbove = beachline.findArcAbove(initPoint.getPoint(), beachlineY);
        arcAbove.makeEventInvalid();
        // Replace this arc by the new arcs
        Arc middleArc = breakArc(arcAbove, initPoint);
        Arc leftArc = middleArc.prev;
        Arc rightArc = middleArc.next;
        // Add an edge in the diagram
        addEdge(leftArc, middleArc);
        middleArc.rightHalfEdge = middleArc.leftHalfEdge;
        rightArc.leftHalfEdge = leftArc.rightHalfEdge;
        // Add new circle events
        if (!beachline.isNullNode(leftArc.prev))
            addEvent(leftArc.prev, leftArc, middleArc);
        if (!beachline.isNullNode(rightArc.next))
            addEvent(middleArc, rightArc, rightArc.next);
    }

    protected void handleCircleEvent(Event event) {
        Point point = event.getPoint();
        Arc arc = event.getArc();
        // Add cell point
        CellPoint cellPoint = diagram.createCellPoint(point);
        // Delete all the events with this arc
        Arc leftArc = arc.prev;
        Arc rightArc = arc.next;
        leftArc.makeEventInvalid();
        rightArc.makeEventInvalid();
        //Update the beachline and the diagram
        removeArc(arc, cellPoint);
        // Add new circle events
        if (!beachline.isNullNode(leftArc.prev))
            addEvent(leftArc.prev, leftArc, rightArc);
        // Right triplet
        if (!beachline.isNullNode(rightArc.next))
            addEvent(leftArc, rightArc, rightArc.next);
    }

    protected Arc breakArc(Arc arc, InitPoint initPoint) {
        // Create a new subtree
        Arc middleArc = beachline.createArc(initPoint);
        Arc leftArc = beachline.createArc(arc.initPoint);
        leftArc.leftHalfEdge = arc.leftHalfEdge;
        Arc rightArc = beachline.createArc(arc.initPoint);
        rightArc.rightHalfEdge = arc.rightHalfEdge;
        // Insert the subtree in the beachline
        beachline.replace(arc, middleArc);
        beachline.insertBefore(middleArc, leftArc);
        beachline.insertAfter(middleArc, rightArc);
        return middleArc;
    }

    protected void addEdge(Arc left, Arc right) {
        // Create two new half edges
        left.rightHalfEdge = diagram.createHalfEdge(left.initPoint.cell);
        right.leftHalfEdge = diagram.createHalfEdge(right.initPoint.cell);
        // Set the two half edges twins
        left.rightHalfEdge.twin = right.leftHalfEdge;
        right.leftHalfEdge.twin = left.rightHalfEdge;
    }

    protected void addEvent(Arc left, Arc middle, Arc right) {
        DoubleWrapper y = new DoubleWrapper();
        Point convergencePoint = computeConvergencePoint(left.initPoint.getPoint(), middle.initPoint.getPoint(), right.initPoint.getPoint(), y);
        boolean isBelow = Double.compare(y.value, beachlineY) <= 0; // <=
        //boolean isBelow = y.value <= beachlineY;
        boolean leftBreakpointMovingRight = isMovingRight(left, middle);
        boolean rightBreakpointMovingRight = isMovingRight(middle, right);
        double leftInitialX = getInitialX(left, middle, leftBreakpointMovingRight);
        double rightInitialX = getInitialX(middle, right, rightBreakpointMovingRight);
        boolean isValid =
                ((leftBreakpointMovingRight && leftInitialX < convergencePoint.x) ||
                (!leftBreakpointMovingRight && leftInitialX > convergencePoint.x)) &&
                ((rightBreakpointMovingRight && rightInitialX < convergencePoint.x) ||
                (!rightBreakpointMovingRight && rightInitialX > convergencePoint.x));
        if (isValid && isBelow) {
            Event event = new Event(y.value, convergencePoint, middle);
            middle.event = event;
            events.add(event);
        }
    }

    protected Point computeConvergencePoint(Point point1, Point point2, Point point3, DoubleWrapper y) {
        Point v1 = Point.sub(point1, point2).getOrthogonal();
        Point v2 = Point.sub(point2, point3).getOrthogonal();
        Point delta = Point.sub(point3, point1);
        delta.mul(0.5);
        double t = delta.getDet(v2) / v1.getDet(v2);
        Point center = Point.sum(point1, point2);
        center.mul(0.5);
        v1.mul(t);
        center.add(v1);
        double r = center.getDistance(point1);
        y.value = center.y - r;
        return center;
    }

    protected boolean isMovingRight(Arc left, Arc right) {
        return left.initPoint.getPoint().y < right.initPoint.getPoint().y;
    }

    protected double getInitialX(Arc left, Arc right, boolean movingRight) {
        return movingRight ? left.initPoint.getPoint().x : right.initPoint.getPoint().x;
    }

    protected void removeArc(Arc arc, CellPoint cellPoint) {
        // End edges
        setDestination(arc.prev, arc, cellPoint);
        setDestination(arc, arc.next, cellPoint);
        // Join the edges of the middle arc
        arc.leftHalfEdge.next = arc.rightHalfEdge;
        arc.rightHalfEdge.prev = arc.leftHalfEdge;
        // Update beachline
        beachline.remove(arc);
        // Create a new edge
        HalfEdge prevHalfEdge = arc.prev.rightHalfEdge;
        HalfEdge nextHalfEdge = arc.next.leftHalfEdge;
        addEdge(arc.prev, arc.next);
        setOrigin(arc.prev, arc.next, cellPoint);
        setPrevHalfEdge(arc.prev.rightHalfEdge, prevHalfEdge);
        setPrevHalfEdge(nextHalfEdge, arc.next.leftHalfEdge);
    }

    protected void
    setDestination(Arc left, Arc right, CellPoint cellPoint) {
        left.rightHalfEdge.origin = cellPoint;
        right.leftHalfEdge.destination = cellPoint;
    }

    protected void setOrigin(Arc left, Arc right, CellPoint cellPoint) {
        left.rightHalfEdge.destination = cellPoint;
        right.leftHalfEdge.origin = cellPoint;
    }

    protected void setPrevHalfEdge(HalfEdge prev, HalfEdge next) {
        prev.next = next;
        next.prev = prev;
    }

    public void boundDiagram() {
        if (beachline.isEmpty()) return;
        Arc leftArc = beachline.getLeastArc();
        Arc rightArc = leftArc.next;
        while (!beachline.isNullNode(rightArc)) {
            Point direction = Point.sub(leftArc.initPoint.getPoint(), rightArc.initPoint.getPoint()).getOrthogonal();
            Point origin = Point.mul(Point.sum(leftArc.initPoint.getPoint(), rightArc.initPoint.getPoint()), 0.5);
            Point intersection = getIntersection(origin, direction);
            CellPoint cellPoint = diagram.createCellPoint(intersection);
            setDestination(leftArc, rightArc, cellPoint);
            leftArc = rightArc;
            rightArc = rightArc.next;
        }
        beachline.setRoot(beachline.nullNode);
    }

    protected Point getIntersection(Point point, Point direction) {
        double t;
        Point intersection;
        //if (Double.compare(direction.x, 0D) > 0)
        if (direction.x > 0D)
            t = (2D - point.x) / direction.x;
        else
            t = (-1D - point.x) / direction.x;
        intersection = Point.sum(point, Point.mul(direction, t));
        double newT;
        //if (Double.compare(direction.y, 0D) > 0)
        if (direction.y > 0D)
            newT = (2D - point.y) / direction.y;
        else
            newT = (-1D - point.y) / direction.y;
        if (Double.compare(newT, t) < 0)
            intersection = Point.sum(point, Point.mul(direction, newT));
        return intersection;
    }

    public static ArrayList<Point> generateRandomPoints(int n) {
        ArrayList<Point> points = new ArrayList<>(n);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; ++i) {
            points.add(new Point(random.nextDouble(), random.nextDouble()));
        }
        return points;
    }
}
