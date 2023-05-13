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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;

public class VoronoiDiagramController {

    @FXML
    private HBox mainPane;

    @FXML
    private FlowPane controlPane;

    @FXML
    private TextField numberToGeneratePoints;

    @FXML
    private Canvas canvas;

    @FXML
    private Button animationButton;

    @FXML
    private TextArea textArea;

    @FXML
    private Button getDiagram;

    private VoronoiDiagram diagram;

    private AnimationTimer animationTimer;

    private Timeline animationTimeline;

    private final DoubleProperty beachlineY = new SimpleDoubleProperty();

    private ArrayList<Point> diagramPoints = new ArrayList<>();

    private final ArrayList<ArrayList<DiagramEdge>> cellEdges = new ArrayList<>();

    private final Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);

    private final String lineSeparator = "\n";

    @FXML
    protected void onGenerateButtonClick()  {
        if (numberToGeneratePoints.getCharacters().isEmpty()) return;
        int pointsNumber = Integer.parseInt(numberToGeneratePoints.getCharacters().toString());
        if (animationTimer != null)
            animationTimer.stop();

        diagramPoints = FortuneAlgorithm.generateRandomPoints(pointsNumber);

        constructVoronoiDiagram();
        drawDiagram();
        showPointsInTextArea();
    }

    @FXML
    protected void onPlayAnimationButton() {
        if (animationButton.getText().equals("Play animation"))
            playAnimation(10000);
        else
            stopAnimation();
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
        drawDiagram();
    }

    @FXML
    protected void onClickCanvas(MouseEvent mouseEvent) {
        stopAnimation();
        double x = mouseEvent.getX() / canvas.getWidth(), y = mouseEvent.getY() / canvas.getHeight();
        diagramPoints.add(new Point(x, y));
        constructVoronoiDiagram();
        drawDiagram();
        showPointsInTextArea();
    }

    @FXML
    protected void onClearCanvasButton() {
        stopAnimation();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        diagramPoints.clear();
        cellEdges.clear();
        showPointsInTextArea();
    }

    @FXML
    protected void onDrawDiagramButton() {
        stopAnimation();
        String text = textArea.getText().strip();
        String[] tokens = text.split(" +|" + lineSeparator + "+");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (tokens.length % 2 == 1) {
            alert.setTitle("Warning!");
            alert.setHeaderText("Incorrect number of tokens!");
            alert.setContentText("There is an odd number of tokens in text field.");
            alert.show();
            return;
        }
        ArrayList<Double> doubles = new ArrayList<>();
        for (String token : tokens) {
            try {
                double x = Double.parseDouble(token.strip());
                doubles.add(x);
                if (Double.compare(x, 0) < 0 || Double.compare(x, 1) > 0) {
                    alert.setTitle("Warning!");
                    alert.setHeaderText("Incorrect range!");
                    alert.setContentText("One of numbers does not belong to the segment from 0 to 1.");
                    alert.show();
                    return;
                }
            } catch (NumberFormatException e) {
                alert.setTitle("Warning!");
                alert.setHeaderText("Incorrect symbols!");
                alert.setContentText("\"" + token + "\" is not a floating point number!");
                alert.show();
                return;
            }
        }

        diagramPoints.clear();
        for (int i = 0; i < doubles.size(); i += 2) {
            diagramPoints.add(new Point(doubles.get(i), doubles.get(i + 1)));
        }
        constructVoronoiDiagram();
        drawDiagram();
        showPointsInTextArea();
    }

    @FXML
    protected void showDiagramInTextArea() {
        if (getDiagram.getText().equals("Get points")) {
            showPointsInTextArea();
            return;
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < cellEdges.size(); ++i) {
            text.append(diagramPoints.get(i).toString()).append(": ");
            for (int j = 0; j < cellEdges.get(i).size(); ++j) {
                text.append(cellEdges.get(i).get(j).toString());
                if (j < cellEdges.get(i).size() - 1) {
                    text.append(", ");
                }
            }
            text.append(lineSeparator);
        }
        textArea.setText(text.toString());
        getDiagram.setText("Get points");
    }

    private void showPointsInTextArea() {
        StringBuilder text = new StringBuilder();
        for (Point point : diagramPoints) {
            text.append(point).append(lineSeparator);
        }
        textArea.setText(text.toString());
        getDiagram.setText("Get diagram");
    }

    @FXML
    protected void onHelpButton() {
        informationAlert.setTitle("Help");
        informationAlert.setHeaderText("Here is some information about using the text field");
        String alertText = "Points are output according to the following pattern: " +
                lineSeparator + "point1.x point1.y" + lineSeparator +
                "point1.x point1.y" + lineSeparator + "..." + lineSeparator +
                "You can add your own points to this list" + lineSeparator + lineSeparator +
                "Diagram is output according to the following pattern:" + lineSeparator +
                "point1.x point1.y: segment1.x1 segment1.y1 segment1.x2 segment1.y2, ..." + lineSeparator +
                lineSeparator + "!All points belong to the segment from 0 to 1!" ;
        informationAlert.setContentText(alertText);
        informationAlert.show();
    }

    private void constructVoronoiDiagram() {
        FortuneAlgorithm algorithm = new FortuneAlgorithm(diagramPoints);
        algorithm.construct();
        diagram = algorithm.getDiagram();
        diagramPoints.clear();
        cellEdges.clear();

        // get points
        for (InitPoint initPoint : diagram.getInitPoints()) {
            diagramPoints.add(initPoint.getPoint());
        }

        // get halfedges
        for (InitPoint initPoint : diagram.getInitPoints()) {
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
            cellEdges.add(new ArrayList<>());
            while (halfEdge != null) {
                if (halfEdge.getOrigin() != null && halfEdge.getDestination() != null) {
                    cellEdges.get(cellEdges.size() - 1).add(new DiagramEdge(halfEdge));
                }
                halfEdge = halfEdge.getNext();
                if (halfEdge == start)
                    break;
            }
        }
    }

    private void drawDiagram() {
        if (diagram == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth(), height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.setLineWidth(3);
        gc.setStroke(Color.BLACK);
        for (Point point : diagramPoints) {
            gc.strokeLine(point.x * width, point.y * height, point.x * width, point.y * height);
        }
        gc.setLineWidth(1);
        gc.setStroke(Color.BLUE);
        for (ArrayList<DiagramEdge> curCellEdgeS : cellEdges) {
            for (DiagramEdge diagramEdge : curCellEdgeS) {
                diagramEdge.draw(gc);
            }
        }

    }

    private void playAnimation(long durationInMillis) {
        if (diagram == null) return;
        AnimatedFortuneAlgorithm algorithm = new AnimatedFortuneAlgorithm(diagramPoints, canvas.getGraphicsContext2D());
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                double line = beachlineY.doubleValue();
                algorithm.updateVoronoiDiagram(line);
            }
        };

        animationTimeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        actionEvent -> startAnimation(),
                        new KeyValue(beachlineY, 1.0)
                ),
                new KeyFrame(Duration.millis(durationInMillis),
                        actionEvent -> stopAnimation(),
                        new KeyValue(beachlineY, -0.1)
                )
        );
        animationTimeline.play();
    }

    private void stopAnimation() {
        if (animationTimer != null) {
            animationTimeline.stop();
            animationTimer.stop();
            animationTimer = null;
            drawDiagram();
            animationButton.setText("Play animation");
        }
    }

    private void startAnimation() {
        if (animationTimer != null) {
            animationTimer.start();
            animationButton.setText("Stop animation");
        }
    }

    @FXML
    protected void handleWidthChange() {
        canvas.setWidth(mainPane.getWidth() - 200);
        controlPane.setPrefWidth(200);
        if (animationTimer == null)
            drawDiagram();
    }

    @FXML
    protected void handleHeightChange() {
        canvas.setHeight(mainPane.getHeight());
        textArea.setPrefHeight(mainPane.getHeight() - 300);
        if (animationTimer == null)
            drawDiagram();
    }
}