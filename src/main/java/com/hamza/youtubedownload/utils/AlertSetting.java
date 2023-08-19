package com.hamza.youtubedownload.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.util.Optional;

public class AlertSetting {


    public static boolean confirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Save Data .....");
        alert.setHeaderText("Did you want to save ?");
        alert.setTitle("Download File");
        Toolkit.getDefaultToolkit().beep();
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void alertError(String text) {
        alert(text,Alert.AlertType.ERROR);
    }

    public void alertInformation(String text) {
        alert(text, Alert.AlertType.INFORMATION);
    }

    private void alert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(text);
        Toolkit.getDefaultToolkit().beep();
        alert.show();
    }
}
