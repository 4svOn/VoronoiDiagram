package hse.edu.cs.voronoidiagram;

import hse.edu.cs.fortuneAlg.HalfEdge;
import hse.edu.cs.fortuneAlg.Point;
import javafx.scene.canvas.GraphicsContext;

public class DiagramEdge implements Comparable<DiagramEdge> {
    private Point point1;
    private Point point2;

    DiagramEdge(HalfEdge halfEdge) {
        if (halfEdge.getOrigin() != null && halfEdge.getDestination() != null) {
            point1 = halfEdge.getOrigin().getPoint();
            point2 = halfEdge.getDestination().getPoint();
        }
    }

    void draw(GraphicsContext gc) {
        double height = gc.getCanvas().getHeight(), width = gc.getCanvas().getWidth();
        gc.strokeLine(point1.x * width, point1.y * height, point2.x * width, point2.y * height);
    }

    double getMaxY() {
        return Math.min(1.0, Math.max(point1.y, point2.y));
    }

    @Override
    public int compareTo(DiagramEdge other) {
        return Double.compare(getMaxY(), other.getMaxY());
    }
}
