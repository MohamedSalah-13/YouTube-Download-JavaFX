package com.hamza.youtubedownload.controller;

import com.hamza.youtubedownload.model.LinkModel;
import com.hamza.youtubedownload.utils.TestCommands;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class VideoDetailsController implements Initializable {
    
    @FXML
    private Label videoNameLabel;
    
    @FXML
    private Label videoUrlLabel;
    
    @FXML
    private Label videoLengthLabel;
    
    @FXML
    private Label videoSaveToLabel;
    
    @FXML
    private VBox thumbnailContainer;
    
    @FXML
    private TextArea downloadProgressTextArea;
    
    private LinkModel videoDetails;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UI components
    }
    
    /**
     * Sets the video details to display in this window
     * @param videoDetails The LinkModel containing video details
     */
    public void setVideoDetails(LinkModel videoDetails) {
        this.videoDetails = videoDetails;
        updateUI();
    }
    
    /**
     * Updates the UI with the video details
     */
    private void updateUI() {
        if (videoDetails == null) {
            return;
        }
        
        videoNameLabel.setText(videoDetails.getVideoName());
        videoUrlLabel.setText(videoDetails.getVideoUrl());
        videoLengthLabel.setText(videoDetails.getLength());
        videoSaveToLabel.setText(videoDetails.getSaveTo());
        
        // Add thumbnail if available
        ImageView thumbnail = videoDetails.getImageView();
        if (thumbnail != null) {
            // Create a new ImageView with the same image to avoid parent issues
            ImageView thumbnailCopy = new ImageView(thumbnail.getImage());
            thumbnailCopy.setFitHeight(150);
            thumbnailCopy.setFitWidth(200);
            thumbnailCopy.setPreserveRatio(true);
            thumbnailContainer.getChildren().clear();
            thumbnailContainer.getChildren().add(thumbnailCopy);
        }
    }
    
    /**
     * Starts the download and shows progress in the text area
     * @param command The download command to execute
     */
    public void startDownload(String command) {
        // Create a new TestCommands instance with our text area
        new Thread(() -> new TestCommands(downloadProgressTextArea).processSetting(command)).start();
    }
    
    /**
     * Closes the window
     */
    @FXML
    protected void close() {
        Stage stage = (Stage) videoNameLabel.getScene().getWindow();
        stage.close();
    }
    
    private static void logError(Exception e) {
        log.error(e.getClass().getCanonicalName(), e.getCause());
    }
}