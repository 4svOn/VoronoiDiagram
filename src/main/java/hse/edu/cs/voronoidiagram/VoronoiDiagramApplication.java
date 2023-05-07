package hse.edu.cs.voronoidiagram;

import hse.edu.cs.fortuneAlg.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.PriorityQueue;

public class VoronoiDiagramApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FortuneAlgorithm algorithm = new FortuneAlgorithm(FortuneAlgorithm.generateRandomPoints(20));
//        FortuneAlgorithm algorithm = new FortuneAlgorithm(FortuneAlgorithm.generatePoints());
        algorithm.constructVoronoiDiagram();

        Group root = new Group();
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawDiagram(gc, algorithm.getDiagram());

        root.getChildren().add(canvas);
        stage.setTitle("Hello!");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void drawDiagram(GraphicsContext gc, VoronoiDiagram diagram) {
        gc.setLineWidth(3);
        for (InitPoint initPoint : diagram.getInitPoints()) {
            gc.strokeLine(initPoint.getPoint().x * 600, initPoint.getPoint().y * 600, initPoint.getPoint().x * 600, initPoint.getPoint().y * 600);
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
                    gc.strokeLine(halfEdge.getOrigin().getPoint().x * 600, halfEdge.getOrigin().getPoint().y * 600,
                            halfEdge.getDestination().getPoint().x * 600, halfEdge.getDestination().getPoint().y * 600);
                }
                halfEdge = halfEdge.getNext();
                if (halfEdge == start)
                    break;
            }
        }
    }

    public static void main(String[] args) {
        launch();
//        PriorityQueue<Event> events = new PriorityQueue<>();
    }
}