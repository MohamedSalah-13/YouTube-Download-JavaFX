package com.hamza.youtubedownload.view;

import com.hamza.youtubedownload.controller.VideoDetailsController;
import com.hamza.youtubedownload.model.LinkModel;
import com.hamza.youtubedownload.utils.ImagePath;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application class for displaying video details in a separate window
 */
public class VideoDetailsApplication extends Application {

    private LinkModel videoDetails;
    private String downloadCommand;
    private VideoDetailsController controller;

    /**
     * Default constructor required by JavaFX
     */
    public VideoDetailsApplication() {
    }

    /**
     * Constructor with video details
     * @param videoDetails The video details to display
     * @param downloadCommand The download command to execute
     */
    public VideoDetailsApplication(LinkModel videoDetails, String downloadCommand) {
        this.videoDetails = videoDetails;
        this.downloadCommand = downloadCommand;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DownloadApplication.class.getResource("video-details-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        
        // Get the controller
        controller = fxmlLoader.getController();
        
        // Set video details if available
        if (videoDetails != null) {
            controller.setVideoDetails(videoDetails);
        }
        
        // Start download if command is available
        if (downloadCommand != null && !downloadCommand.isEmpty()) {
            controller.startDownload(downloadCommand);
        }
        
        // Configure the stage
        stage.setTitle("Video Details");
        stage.getIcons().add(new Image(new ImagePath().YOUTUBE_ICON_STREAM));
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Convenience method to show the video details window
     * @param videoDetails The video details to display
     * @param downloadCommand The download command to execute
     * @throws IOException If an error occurs while loading the FXML
     */
    public static void showVideoDetails(LinkModel videoDetails, String downloadCommand) throws IOException {
        VideoDetailsApplication app = new VideoDetailsApplication(videoDetails, downloadCommand);
        app.start(new Stage());
    }
}