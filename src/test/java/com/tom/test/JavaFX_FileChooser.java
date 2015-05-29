package com.tom.test;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JavaFX_FileChooser extends Application {
    
    File file;
    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Hello World!");
        final Label labelFile = new Label();
        Button btn = new Button();
        btn.setText("Open FileChooser'");
        
        btn.setOnAction(event -> {
                                    FileChooser fileChooser = new FileChooser();
                                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AVI files (*.avi)", "*.avi");
                                    fileChooser.getExtensionFilters().add(extFilter);
                                    file = fileChooser.showOpenDialog(null);
                                    labelFile.setText(file.getPath());
                                }
                       );
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(labelFile, btn);
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}