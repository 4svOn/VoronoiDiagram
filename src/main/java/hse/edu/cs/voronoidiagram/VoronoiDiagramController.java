package hse.edu.cs.voronoidiagram;

import hse.edu.cs.fortuneAlg.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class VoronoiDiagramController {
    @FXML
    private TextField numberToGeneratePoints;

    @FXML
    private Button generateButton;

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane canvasPane;

    private VoronoiDiagram diagram;

    @FXML
    protected void onGenerateButtonClick()  {
        if (numberToGeneratePoints.getCharacters().isEmpty()) return;
        int pointsNumber = Integer.parseInt(numberToGeneratePoints.getCharacters().toString());

        FortuneAlgorithm algorithm = new FortuneAlgorithm(FortuneAlgorithm.generateRandomPoints(pointsNumber));
        algorithm.constructVoronoiDiagram();
        diagram = algorithm.getDiagram();
        drawDiagram();
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

    private void drawDiagram() {
        if (diagram == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth(), height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.setLineWidth(3);
        for (InitPoint initPoint : diagram.getInitPoints()) {
            gc.strokeLine(initPoint.getPoint().x * width, initPoint.getPoint().y * height, initPoint.getPoint().x * width, initPoint.getPoint().y * height);
        }

        gc.setLineWidth(1);
        gc.setStroke(Color.BLUE);
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
                    gc.strokeLine(halfEdge.getOrigin().getPoint().x * width, halfEdge.getOrigin().getPoint().y * height,
                            halfEdge.getDestination().getPoint().x * width, halfEdge.getDestination().getPoint().y * height);
                }
                halfEdge = halfEdge.getNext();
                if (halfEdge == start)
                    break;
            }
        }
    }
}