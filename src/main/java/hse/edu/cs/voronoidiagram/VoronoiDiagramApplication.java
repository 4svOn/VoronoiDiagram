package hse.edu.cs.voronoidiagram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VoronoiDiagramApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("tmp.fxml"));
        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Voronoi Diagram");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}