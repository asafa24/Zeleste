package com.iza.zeleste;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ui.fxml"));
        AnchorPane uiPane = loader.load();

        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, uiPane);


        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Zeleste");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }




}
