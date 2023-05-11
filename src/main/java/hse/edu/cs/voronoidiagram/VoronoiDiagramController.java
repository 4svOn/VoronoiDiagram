package hse.edu.cs.voronoidiagram;

import hse.edu.cs.fortuneAlg.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;

public class VoronoiDiagramController {
    @FXML
    private TextField numberToGeneratePoints;

    @FXML
    private Canvas canvas;

    private VoronoiDiagram diagram;

    private AnimationTimer animationTimer;

    private final DoubleProperty beachlineY = new SimpleDoubleProperty();

    private ArrayList<Point> sortedPoints = new ArrayList<>();

    private final ArrayList<DiagramEdge> sortedDiagramEdges = new ArrayList<>();

    @FXML
    protected void onGenerateButtonClick()  {
        if (numberToGeneratePoints.getCharacters().isEmpty()) return;
        int pointsNumber = Integer.parseInt(numberToGeneratePoints.getCharacters().toString());
        if (animationTimer != null)
            animationTimer.stop();

        sortedPoints = FortuneAlgorithm.generateRandomPoints(pointsNumber);
        sortedPoints.sort(null);

        constructVoronoiDiagram();
        drawDiagram(1.0);
    }

    @FXML
    protected void onPlayAnimationButton() {
        playAnimation(10000);
    }

    @FXML
    protected void formatTextFieldToNumbersOnly() {
        numberToGeneratePoints.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberToGeneratePoints.setText(newValue.replaceAll("\\D", "")); // force the text field to be numeric only
            }
        });
    }

    @FXML
    protected void redrawCanvas() {
        drawDiagram(1.0);
    }

    @FXML
    public void onClickCanvas(MouseEvent mouseEvent) {
        double x = mouseEvent.getX() / canvas.getWidth(), y = mouseEvent.getY() / canvas.getHeight();
        sortedPoints.add(new Point(x, y));
        constructVoronoiDiagram();
        drawDiagram(1.0);
    }

    @FXML
    public void onClearCanvasButton() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        sortedPoints.clear();
        if (animationTimer != null)
            animationTimer.stop();
    }

    private void constructVoronoiDiagram() {
        FortuneAlgorithm algorithm = new FortuneAlgorithm(sortedPoints);
        algorithm.construct();
        diagram = algorithm.getDiagram();
        sortedPoints.clear();
        sortedDiagramEdges.clear();

        // get points
        for (InitPoint initPoint : diagram.getInitPoints()) {
            sortedPoints.add(initPoint.getPoint());
        }
        sortedPoints.sort(null);

        // get halfedges
        for (InitPoint initPoint : diagram.getInitPoints()) {
            Point center = initPoint.getPoint();
            Cell cell = initPoint.getCell();
            HalfEdge halfEdge = cell.getHalfEdge();
            if (halfEdge == null)
                continue;
            while (halfEdge.getPrev() != null) {
                halfEdge = halfEdge.getPrev();
                if (halfEdge == cell.getHalfEdge())
                    break;
            }
            HalfEdge start = halfEdge;
            while (halfEdge != null) {
                if (halfEdge.getOrigin() != null && halfEdge.getDestination() != null) {
                    sortedDiagramEdges.add(new DiagramEdge(halfEdge));
                }
                halfEdge = halfEdge.getNext();
                if (halfEdge == start)
                    break;
            }
        }
        sortedDiagramEdges.sort(null);
    }

    private void drawDiagram(double line) {
        if (diagram == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth(), height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.setLineWidth(3);
        gc.setStroke(Color.BLACK);
        for (Point point : sortedPoints) {
            gc.strokeLine(point.x * width, point.y * height, point.x * width, point.y * height);
        }
        gc.setLineWidth(1);
        gc.setStroke(Color.BLUE);
        for (DiagramEdge diagramEdge : sortedDiagramEdges) {
            if (Double.compare(diagramEdge.getMaxY(), line) <= 0)
                diagramEdge.draw(gc);
            else break;
        }
    }

    private void playAnimation(long durationInMillis) {
        if (diagram == null) return;
        AnimatedFortuneAlgorithm algorithm = new AnimatedFortuneAlgorithm(sortedPoints, canvas.getGraphicsContext2D());
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                double line = beachlineY.doubleValue();
                algorithm.updateVoronoiDiagram(line);
            }
        };

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        actionEvent -> animationTimer.start(),
                        new KeyValue(beachlineY, 1.0)
                ),
                new KeyFrame(Duration.millis(durationInMillis),
                        actionEvent -> animationTimer.stop(),
                        new KeyValue(beachlineY, -0.1)
                )
        );
        timeline.play();
    }
}