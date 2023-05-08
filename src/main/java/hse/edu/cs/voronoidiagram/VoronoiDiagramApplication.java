package hse.edu.cs.voronoidiagram;

import hse.edu.cs.fortuneAlg.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.PriorityQueue;

public class VoronoiDiagramApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        VoronoiDiagramController voronoiDiagramController = new VoronoiDiagramController();
        fxmlLoader.setController(voronoiDiagramController);
        fxmlLoader.setLocation(getClass().getResource("tmp.fxml"));
        Parent root = fxmlLoader.load();

        //FortuneAlgorithm algorithm = new FortuneAlgorithm(FortuneAlgorithm.generateRandomPoints(20));
//        FortuneAlgorithm algorithm = new FortuneAlgorithm(FortuneAlgorithm.generatePoints());
        //double X = 600, Y = 600;
        //Group root = new Group();
        //Canvas canvas = new Canvas(X, Y);
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        //drawDiagram(gc, algorithm.getDiagram(), X, Y);
        //TranslateTransition transition = drawAnimation(root, X, Y);
        //stage.setTitle("Hello!");

        stage.setScene(new Scene(root));
        stage.show();
    }

    private TranslateTransition drawAnimation(Group root, double width, double height) {
        Line beachline = new Line(0, 0, width, 0);
        beachline.setStroke(Color.CRIMSON);
        beachline.setStrokeWidth(5);
        Duration duration = Duration.millis(5000);
        TranslateTransition transition = new TranslateTransition(duration, beachline);
        transition.setByY(height);
        root.getChildren().add(beachline);

        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        for(int i = 0; i < 10; ++i) {
            KeyFrame keyFrame = new KeyFrame(Duration.millis(17));

        }
        timeline.setCycleCount(Animation.INDEFINITE);

        return transition;
    }

    public static void main(String[] args) {
        launch();
//        PriorityQueue<Event> events = new PriorityQueue<>();
    }
}