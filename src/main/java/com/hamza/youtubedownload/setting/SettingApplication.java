package com.hamza.youtubedownload.setting;

import com.hamza.youtubedownload.DownloadApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(DownloadApplication.class.getResource("setting.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Setting");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
