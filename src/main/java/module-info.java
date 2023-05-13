module hse.edu.cs.voronoidiagram {
    requires javafx.controls;
    requires javafx.fxml;

    opens hse.edu.cs.voronoidiagram to javafx.fxml;
    exports hse.edu.cs.voronoidiagram;
    exports hse.edu.cs.fortuneAlg;
    opens hse.edu.cs.fortuneAlg to javafx.fxml;
}