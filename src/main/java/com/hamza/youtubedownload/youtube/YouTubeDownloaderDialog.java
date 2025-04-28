package com.hamza.youtubedownload.youtube;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/**
 * Dialog for downloading YouTube videos.
 */
@Log4j2
public class YouTubeDownloaderDialog {

    private static final String FXML_PATH = "YouTubeDownloader.fxml";
    private final Stage dialogStage;
    @Getter
    private final YouTubeDownloaderController controller;

    /**
     * Creates a new YouTube downloader dialog.
     *
     * @param parentStage the parent stage
     * @throws IOException if the FXML file cannot be loaded
     */
    public YouTubeDownloaderDialog(Stage parentStage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Parent root = loader.load();
        
        // Get the controller
        controller = loader.getController();
        
        // Create the dialog stage
        dialogStage = new Stage();
        dialogStage.setTitle("YouTube Video Downloader");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        
        // Set the scene
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        
        // Set the stage in the controller
        controller.setStage(dialogStage);
    }

    /**
     * Shows the dialog and waits for it to be closed.
     */
    public void showAndWait() {
        dialogStage.showAndWait();
    }

    /**
     * Shows the dialog.
     */
    public void show() {
        dialogStage.show();
    }

}