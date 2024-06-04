package com.hamza.youtubedownload.audio;

import com.hamza.youtubedownload.DownloadApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AudioPlay extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(DownloadApplication.class.getResource("audio.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Playing Audio");
        stage.show();
    }

}
