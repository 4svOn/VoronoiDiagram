package hse.edu.cs.fortuneAlg;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class AnimatedBeachline extends Beachline{

    private final Canvas canvas;

    AnimatedBeachline(Canvas canvas) {
        super();
        this.canvas = canvas;
    }
    void draw(Arc node, double line) {
        if (isNullNode(node))
            return;
        canvas.getGraphicsContext2D().setStroke(Color.PURPLE);
        canvas.getGraphicsContext2D().setLineWidth(2);
        canvas.getGraphicsContext2D().beginPath();
        double leftBreakPoint = 0D;
        while (!isNullNode(node.next)) {
            leftBreakPoint = drawArc(node.initPoint.getPoint(), node.next.initPoint.getPoint(), line, leftBreakPoint);
            node = node.next;
        }
        // draw the most right line
        drawArc(node.initPoint.getPoint(), null, line, leftBreakPoint);
        canvas.getGraphicsContext2D().stroke();
        canvas.getGraphicsContext2D().closePath();
    }

    private double drawArc(Point point1, Point point2, double line, double leftBreakPoint) {
        Point p1 = new Point(point1.x * canvas.getWidth(), point1.y * canvas.getHeight());
        line *= canvas.getHeight();
        if (point2 == null) {
            AnimatedArc arc = new AnimatedArc(p1, line);
            arc.convertToBezierCurve(leftBreakPoint, canvas.getWidth());
            arc.draw(canvas.getGraphicsContext2D());
            return canvas.getWidth();
        }
        Point p2 = new Point(point2.x * canvas.getWidth(), point2.y * canvas.getHeight());
        double rightBreakPoint = computeBreakPoint(p1, p2, line);
        AnimatedArc arc = new AnimatedArc(p1, line);
        arc.convertToBezierCurve(leftBreakPoint, rightBreakPoint);
        arc.draw(canvas.getGraphicsContext2D());
        return rightBreakPoint;
    }
}
