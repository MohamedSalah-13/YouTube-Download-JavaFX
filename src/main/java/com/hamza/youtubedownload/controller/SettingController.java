package com.hamza.youtubedownload.controller;

import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Choose;
import com.hamza.youtubedownload.utils.Config_Data;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private TextField text;
    @FXML
    private Button button, save;
    private String savePlace;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        savePlace = new Choose().getPlacedSave();
        text.setText(savePlace);
        action();
    }

    private void action() {
        button.setOnAction(actionEvent -> text.setText(chooseDirectory(savePlace).getAbsolutePath()));
        save.setOnAction(actionEvent -> {
            new Config_Data().saveProp("save", text.getText());
            new AlertSetting().alertInformation("Save Done");
        });
    }

    private File chooseDirectory(String path) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = new File(path);
        directoryChooser.setInitialDirectory(file);
        return directoryChooser.showDialog(new Stage());
    }
}
