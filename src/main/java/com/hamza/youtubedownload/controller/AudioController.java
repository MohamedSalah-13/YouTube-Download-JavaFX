package com.hamza.youtubedownload.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Getter
public class AudioController implements Initializable {
    private static final String DEFAULT_AUDIO_PATH = "I:\\قران\\5- ابتهالات الشيخ محمد عمران\\008.mp3";

    @FXML
    private Button play;
    @FXML
    private Button stop;
    @FXML
    private Button pause;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label l1;
    @FXML
    private Label l2;

    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMediaPlayer();
        setupButtonHandlers();
    }

    private void initializeMediaPlayer() {
        File audioFile = new File(DEFAULT_AUDIO_PATH);
        if (!audioFile.exists()) {
            throw new IllegalStateException("Audio file not found: " + DEFAULT_AUDIO_PATH);
        }
        Media media = new Media(audioFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // Add listener to update progress and time labels continuously
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!mediaPlayer.getCurrentTime().equals(mediaPlayer.getStopTime())) {
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                double totalDuration = mediaPlayer.getTotalDuration().toSeconds();

                // Update progress bar directly
                progressBar.setProgress(currentTime / totalDuration);

                // Update time labels
                updateTimeLabels(currentTime, totalDuration);
            }
        });

        // Set handler for when media is ready
        mediaPlayer.setOnReady(() -> {
            updateTimeLabels(0, mediaPlayer.getTotalDuration().toSeconds());
        });
    }

    private void setupButtonHandlers() {
        play.setOnAction(event -> handlePlayButton());
        stop.setOnAction(event -> handleStopButton());
        pause.setOnAction(event -> handlePauseButton());
    }

    private void handlePlayButton() {
        logCurrentTime();
        mediaPlayer.play();
        // Progress tracking is now handled by the listener on currentTimeProperty
    }

    private void handleStopButton() {
        logCurrentTime();
        mediaPlayer.stop();
        // Reset progress bar and time labels
        progressBar.setProgress(0);
        updateTimeLabels(0, mediaPlayer.getTotalDuration().toSeconds());
    }

    private void handlePauseButton() {
        logCurrentTime();
        mediaPlayer.pause();
        updateTimeLabels(mediaPlayer.getCurrentTime().toSeconds(), mediaPlayer.getTotalDuration().toSeconds());
    }

    // Progress tracking is now handled by the listener on currentTimeProperty

    private void updateTimeLabels(double currentTime, double totalDuration) {
        l1.setText(formatTime(currentTime));
        l2.setText(formatTime(totalDuration));
    }

    private String formatTime(double seconds) {
        int minutes = (int) (seconds / 60);
        int remainingSeconds = (int) (seconds % 60);
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void logCurrentTime() {
        System.out.println(mediaPlayer.getCurrentTime());
    }

    // MediaProgressTask class removed as progress tracking is now handled by the listener on currentTimeProperty
}
