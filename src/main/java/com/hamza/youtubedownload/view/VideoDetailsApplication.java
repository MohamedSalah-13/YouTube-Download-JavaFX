package com.hamza.youtubedownload.view;

import com.hamza.youtubedownload.controller.VideoDetailsController;
import com.hamza.youtubedownload.model.LinkModel;
import com.hamza.youtubedownload.utils.ImagePath;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import javax.imageio.ImageIO;
//import java.awt.Image;


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

        controller = fxmlLoader.getController();

        if (videoDetails != null) {
            controller.setVideoDetails(videoDetails);
        }

        if (downloadCommand != null && !downloadCommand.isEmpty()) {
            controller.startDownload(downloadCommand);
        }

        // Configure the stage
        stage.setTitle("Video Details");
        stage.getIcons().add(new Image(new ImagePath().YOUTUBE_ICON_STREAM));
        stage.setScene(scene);

        // Add minimize to tray functionality
        if (SystemTray.isSupported()) {
            Platform.setImplicitExit(false);

            SystemTray tray = SystemTray.getSystemTray();
            // Convert JavaFX image to AWT image
            java.awt.Image trayImage = ImageIO.read(new ImagePath().YOUTUBE_ICON_STREAM);

            // Create tray icon
            PopupMenu popup = new PopupMenu();
            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(e -> Platform.runLater(() -> {
                stage.show();
                stage.setIconified(false);
            }));

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> {
                Platform.exit();
//                System.exit(0);
            });

            popup.add(showItem);
            popup.add(exitItem);

            TrayIcon trayIcon = new TrayIcon(trayImage, "Video Details", popup);
            trayIcon.setImageAutoSize(true);

            // Add icon to system tray
            try {
                tray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Minimize to tray when window is minimized
            stage.setOnCloseRequest(event -> {
                event.consume();
                stage.hide();
            });
        }

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