package hse.edu.cs.fortuneAlg;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AnimatedBeachline extends Beachline{

    private final GraphicsContext graphicsContext;
    private final double width;
    private final double height;

    AnimatedBeachline(GraphicsContext graphicsContext) {
        super();
        this.graphicsContext = graphicsContext;
        width = graphicsContext.getCanvas().getWidth();
        height = graphicsContext.getCanvas().getHeight();
    }
    void draw(Arc node, double line) {
        if (isNullNode(node))
            return;
        graphicsContext.setStroke(Color.PURPLE);
        graphicsContext.setLineWidth(2);
        graphicsContext.beginPath();
        double leftBreakPoint = 0D;
        while (!isNullNode(node.next)) {
            leftBreakPoint = drawArc(node.initPoint.getPoint(), node.next.initPoint.getPoint(), line, leftBreakPoint);
            node = node.next;
        }
        // draw the most right line
        drawArc(node.initPoint.getPoint(), null, line, leftBreakPoint);
        graphicsContext.stroke();
        graphicsContext.closePath();
    }

    private double drawArc(Point point1, Point point2, double line, double leftBreakPoint) {
        Point p1 = new Point(point1.x * width, point1.y * height);
        line *= height;
        if (point2 == null) {
            AnimatedArc arc = new AnimatedArc(p1, line);
            arc.convertToBezierCurve(leftBreakPoint, width);
            arc.draw(graphicsContext);
            return width;
        }
        Point p2 = new Point(point2.x * width, point2.y * height);
        double rightBreakPoint = computeBreakPoint(p1, p2, line);
        AnimatedArc arc = new AnimatedArc(p1, line);
        arc.convertToBezierCurve(leftBreakPoint, rightBreakPoint);
        arc.draw(graphicsContext);
        return rightBreakPoint;
    }
}
