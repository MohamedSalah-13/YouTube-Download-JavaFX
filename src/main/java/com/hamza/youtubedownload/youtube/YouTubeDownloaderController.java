package com.hamza.youtubedownload.youtube;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the YouTube downloader dialog.
 */
@Log4j2
public class YouTubeDownloaderController implements Initializable {

    @FXML
    private TextField txtYoutubeUrl;

    @FXML
    private TextField txtVideoId;

    @FXML
    private TextField txtOutputDirectory;

    @FXML
    private TextField txtFilename;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnExtract;

    @FXML
    private Button btnDownload;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label lblStatus;

    @FXML
    private VBox root;

    @Setter
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set default output directory to user's Downloads folder
        txtOutputDirectory.setText(System.getProperty("user.home") + File.separator + "Downloads");
        
        // Set default filename
        txtFilename.setText("youtube_video");
        
        // Initialize progress bar
        progressBar.setProgress(0);
        progressBar.setVisible(false);
        
        // Set up button actions
        setupButtonActions();
    }

    /**
     * Sets up the actions for the buttons.
     */
    private void setupButtonActions() {
        // Extract button action
        btnExtract.setOnAction(event -> extractVideoId());
        
        // Browse button action
        btnBrowse.setOnAction(event -> browseForOutputDirectory());
        
        // Download button action
        btnDownload.setOnAction(event -> downloadVideo());
    }

    /**
     * Extracts the video ID from the YouTube URL.
     */
    private void extractVideoId() {
        String youtubeUrl = txtYoutubeUrl.getText().trim();
        if (youtubeUrl.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a YouTube URL");
            return;
        }
        
        Optional<String> videoIdOpt = YouTubeUtils.extractVideoId(youtubeUrl);
        if (videoIdOpt.isPresent()) {
            String videoId = videoIdOpt.get();
            txtVideoId.setText(videoId);
            lblStatus.setText("Video ID extracted: " + videoId);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not extract video ID from URL");
            lblStatus.setText("Failed to extract video ID");
        }
    }

    /**
     * Opens a directory chooser to select the output directory.
     */
    private void browseForOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Directory");
        
        // Set initial directory if it exists
        String currentDir = txtOutputDirectory.getText().trim();
        if (!currentDir.isEmpty()) {
            File dir = new File(currentDir);
            if (dir.exists() && dir.isDirectory()) {
                directoryChooser.setInitialDirectory(dir);
            }
        }
        
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            txtOutputDirectory.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Downloads the video using the extracted video ID.
     */
    private void downloadVideo() {
        String videoId = txtVideoId.getText().trim();
        if (videoId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please extract a video ID first");
            return;
        }
        
        String outputDirectory = txtOutputDirectory.getText().trim();
        if (outputDirectory.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an output directory");
            return;
        }
        
        String filename = txtFilename.getText().trim();
        if (filename.isEmpty()) {
            filename = "youtube_video";
        }
        
        // Show progress bar
        progressBar.setVisible(true);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        lblStatus.setText("Downloading video...");
        
        // Disable buttons during download
        btnExtract.setDisable(true);
        btnDownload.setDisable(true);
        
        // Start download task
        Task<File> downloadTask = YouTubeUtils.downloadVideo(videoId, outputDirectory, filename);
        
        // Handle download completion
        downloadTask.setOnSucceeded(event -> {
            File downloadedFile = downloadTask.getValue();
            progressBar.setProgress(1.0);
            lblStatus.setText("Download completed: " + downloadedFile.getAbsolutePath());
            
            // Re-enable buttons
            btnExtract.setDisable(false);
            btnDownload.setDisable(false);
            
            showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Video downloaded successfully to: " + downloadedFile.getAbsolutePath());
        });
        
        // Handle download failure
        downloadTask.setOnFailed(event -> {
            Throwable exception = downloadTask.getException();
            progressBar.setVisible(false);
            lblStatus.setText("Download failed: " + exception.getMessage());
            
            // Re-enable buttons
            btnExtract.setDisable(false);
            btnDownload.setDisable(false);
            
            showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to download video: " + exception.getMessage());
            
            log.error("Error downloading video", exception);
        });
    }

    /**
     * Shows an alert dialog.
     *
     * @param alertType the type of alert
     * @param title the title of the alert
     * @param message the message to display
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}