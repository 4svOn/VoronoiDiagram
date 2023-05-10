package hse.edu.cs.fortuneAlg;

import javafx.scene.canvas.GraphicsContext;

public class AnimatedArc {

    double a, b, c;

    private Point point1, point2, control1, control2;

    AnimatedArc(Point focus, double line) {
        a = 1D / (2D * (focus.y - line));
        b = -2D * focus.x * a;
        c = (focus.x * focus.x + focus.y * focus.y - line * line) * a;
    }

    void convertToBezierCurve(double x1, double x2) {
        point1 = new Point(x1, getValue(x1));
        point2 = new Point(x2, getValue(x2));
        Point control = new Point(
                (x1 + x2) / 2D,
                (x2 - x1) / 2D * (2D * a * x1 + b) + getValue(x1));
        control1 = Point.sum(Point.mul(point1, 1D / 3D), Point.mul(control, 2D / 3D));
        control2 = Point.sum(Point.mul(point2, 1D / 3D), Point.mul(control, 2D / 3D));
    }

    void draw(GraphicsContext gc) {
        //double height = gc.getCanvas().getHeight(), width = gc.getCanvas().getWidth();
        //gc.moveTo(point1.x * width, point1.y * height);
        //gc.bezierCurveTo(
        //        control1.x * width, control1.y * height,
        //        control2.x * width, control2.y * height,
        //        point2.x * width, point2.y * height);
        gc.moveTo(point1.x, point1.y);
        gc.bezierCurveTo(
                 control1.x, control1.y,
                control2.x, control2.y,
                point2.x, point2.y);
    }

    double getValue(double x) {
        return a * x * x + b * x + c;
    }
}
