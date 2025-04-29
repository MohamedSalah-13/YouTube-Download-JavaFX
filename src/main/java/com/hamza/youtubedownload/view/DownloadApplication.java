package com.hamza.youtubedownload.view;

import com.hamza.youtubedownload.utils.ImagePath;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class DownloadApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DownloadApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Youtube Download");
        stage.getIcons().add(new Image(new ImagePath().YOUTUBE_ICON_STREAM));
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}