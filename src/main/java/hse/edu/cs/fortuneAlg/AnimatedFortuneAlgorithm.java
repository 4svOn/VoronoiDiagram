package hse.edu.cs.fortuneAlg;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AnimatedFortuneAlgorithm extends FortuneAlgorithm {

    private final GraphicsContext graphicsContext;
    private final AnimatedBeachline animatedBeachline;
    private final double width;
    private final double height;

    private class EdgeToDraw{
        Point p1, p2;

        EdgeToDraw(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        void draw() {
            graphicsContext.strokeLine(p1.x * width, p1.y * height, p2.x * width, p2.y * height);
        }
    }

    private final ArrayList<EdgeToDraw> edgesToDraw = new ArrayList<>();

    public AnimatedFortuneAlgorithm(ArrayList<Point> points, GraphicsContext graphicsContext) {
        super(points);
        this.graphicsContext = graphicsContext;
        width = graphicsContext.getCanvas().getWidth();
        height = graphicsContext.getCanvas().getHeight();

        for(InitPoint initPoint : diagram.getInitPoints()) {
            events.add(new Event(initPoint));
        }
        drawInitPoints();

        animatedBeachline = new AnimatedBeachline(graphicsContext);
        beachline = animatedBeachline;
    }

    public void updateVoronoiDiagram(double line) {
        drawInitPoints();
        graphicsContext.setLineWidth(3);
        graphicsContext.setStroke(Color.CRIMSON);
        graphicsContext.strokeLine(0, line * height, width, line * height);

        while (!events.isEmpty() && Double.compare(events.peek().y, line) >= 0) {
            processEvent(events.poll());
        }

        drawCellPoints();
        if (!animatedBeachline.isEmpty()) {
            animatedBeachline.draw(animatedBeachline.getLeastArc(), line);
        }
        if (Double.compare(line, 0) <= 0) {
            while (!events.isEmpty()) {
                processEvent(events.poll());
            }
            boundDiagram();
        }
        drawEdges();

    }

    @Override
    protected void setDestination(Arc left, Arc right, CellPoint cellPoint) {
        super.setDestination(left, right, cellPoint);
        if (left.rightHalfEdge.getOrigin() == null || left.rightHalfEdge.getDestination() == null) return;
        edgesToDraw.add(new EdgeToDraw(left.rightHalfEdge.getOrigin().getPoint(), left.rightHalfEdge.getDestination().getPoint()));
    }

    private void drawEdges() {
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
        for (EdgeToDraw edge : edgesToDraw) {
            edge.draw();
        }
    }

    private void drawInitPoints() {
        graphicsContext.clearRect(0D, 0D, width, height);
        graphicsContext.setLineWidth(3);
        graphicsContext.setStroke(Color.BLACK);
        for(InitPoint initPoint : diagram.getInitPoints()) {
            graphicsContext.strokeLine(
                    initPoint.getPoint().x * width, initPoint.getPoint().y * height,
                    initPoint.getPoint().x * width, initPoint.getPoint().y * height
            );
        }
    }

    private void drawCellPoints() {
        graphicsContext.setLineWidth(4);
        graphicsContext.setStroke(Color.DARKBLUE);
        for (CellPoint cellPoint : diagram.getCellPoints()) {
            graphicsContext.strokeLine(
                    cellPoint.getPoint().x * width, cellPoint.getPoint().y * height,
                    cellPoint.getPoint().x * width, cellPoint.getPoint().y * height
            );
        }
    }
}
