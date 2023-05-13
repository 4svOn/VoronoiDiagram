package hse.edu.cs.fortuneAlg;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AnimatedFortuneAlgorithm extends FortuneAlgorithm {

    private final Canvas canvas;
    private final AnimatedBeachline animatedBeachline;

    private class EdgeToDraw{
        Point p1, p2;

        EdgeToDraw(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        void draw() {
            canvas.getGraphicsContext2D().strokeLine(p1.x * canvas.getWidth(), p1.y * canvas.getHeight(),
                                                    p2.x * canvas.getWidth(), p2.y * canvas.getHeight());
        }
    }

    private final ArrayList<EdgeToDraw> edgesToDraw = new ArrayList<>();

    public AnimatedFortuneAlgorithm(ArrayList<Point> points, Canvas canvas) {
        super(points);
        this.canvas = canvas;

        for(InitPoint initPoint : diagram.getInitPoints()) {
            events.add(new Event(initPoint));
        }
        drawInitPoints();

        animatedBeachline = new AnimatedBeachline(this.canvas);
        beachline = animatedBeachline;
    }

    public void updateVoronoiDiagram(double line) {
        drawInitPoints();
        canvas.getGraphicsContext2D().setLineWidth(3);
        canvas.getGraphicsContext2D().setStroke(Color.CRIMSON);
        canvas.getGraphicsContext2D().strokeLine(0, line * canvas.getHeight(), canvas.getWidth(),
                line * canvas.getHeight());

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
        canvas.getGraphicsContext2D().setStroke(Color.BLUE);
        canvas.getGraphicsContext2D().setLineWidth(1);
        for (EdgeToDraw edge : edgesToDraw) {
            edge.draw();
        }
    }

    private void drawInitPoints() {
        canvas.getGraphicsContext2D().clearRect(0D, 0D, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().setLineWidth(3);
        canvas.getGraphicsContext2D().setStroke(Color.BLACK);
        for(InitPoint initPoint : diagram.getInitPoints()) {
            canvas.getGraphicsContext2D().strokeLine(
                    initPoint.getPoint().x * canvas.getWidth(), initPoint.getPoint().y * canvas.getHeight(),
                    initPoint.getPoint().x * canvas.getWidth(), initPoint.getPoint().y * canvas.getHeight()
            );
        }
    }

    private void drawCellPoints() {
        canvas.getGraphicsContext2D().setLineWidth(4);
        canvas.getGraphicsContext2D().setStroke(Color.DARKBLUE);
        for (CellPoint cellPoint : diagram.getCellPoints()) {
            canvas.getGraphicsContext2D().strokeLine(
                    cellPoint.getPoint().x * canvas.getWidth(), cellPoint.getPoint().y * canvas.getHeight(),
                    cellPoint.getPoint().x * canvas.getWidth(), cellPoint.getPoint().y * canvas.getHeight()
            );
        }
    }
}
