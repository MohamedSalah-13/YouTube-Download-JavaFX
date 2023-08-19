package com.hamza.youtubedownload.setting;

import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Choose;
import com.hamza.youtubedownload.utils.Configs;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private TextField text;
    @FXML
    private Button button, save;
    private Choose choose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choose = new Choose();
        text.setText(choose.getPlacedSave());
        action();
    }

    private void action() {
        button.setOnAction(actionEvent -> text.setText(choose.chooseFile()));
        save.setOnAction(actionEvent -> {
            new Configs().saveProp("save", text.getText());
            new AlertSetting().alertInformation("Save done");
        });
    }
}
