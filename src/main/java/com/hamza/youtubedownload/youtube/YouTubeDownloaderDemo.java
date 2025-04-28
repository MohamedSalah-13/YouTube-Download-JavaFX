package com.hamza.youtubedownload.youtube;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/**
 * Demo application for the YouTube downloader.
 */
@Log4j2
public class YouTubeDownloaderDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("YouTube Downloader Demo");

        // Create a button to open the YouTube downloader dialog
        Button button = new Button("Open YouTube Downloader");
        button.setOnAction(event -> {
            try {
                // Create and show the YouTube downloader dialog
                YouTubeDownloaderDialog dialog = new YouTubeDownloaderDialog(primaryStage);
                dialog.showAndWait();
            } catch (IOException e) {
                log.error("Error opening YouTube downloader dialog", e);
            }
        });

        // Create the scene
        StackPane root = new StackPane();
        root.getChildren().add(button);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * Main method to launch the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}