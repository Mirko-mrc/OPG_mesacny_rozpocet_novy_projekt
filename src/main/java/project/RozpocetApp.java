package project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RozpocetApp extends Application {
    @Override
    public void start(Stage stage) {
        Label title = new Label("Spravca rozpoctu");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        Label subtitle = new Label("Startovacia JavaFX verzia. Funkcie pribudnu v dalsich commitoch.");
        VBox root = new VBox(12, title, subtitle);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: #f5f7fb;");
        stage.setTitle("Spravca rozpoctu");
        stage.setScene(new Scene(root, 720, 420));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}