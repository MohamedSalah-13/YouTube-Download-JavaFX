package com.hamza.youtubedownload.add;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class AddUrl extends Dialog<String> {

    public AddUrl() throws IOException {
        AddUrlController controller = new AddUrlController();
        DialogPane dialogPane = this.getDialogPane();
        ButtonType ok = ButtonType.OK;
        dialogPane.getButtonTypes().addAll(ok, ButtonType.CANCEL);

        this.setResultConverter((var1x) -> {
            ButtonBar.ButtonData var2 = var1x == null ? null : var1x.getButtonData();
            return var2 == ButtonBar.ButtonData.OK_DONE ? controller.searchVideo() : null;
        });

        String addLink = "Add Link";
        Stage window = (Stage) dialogPane.getScene().getWindow();
        window.setTitle(addLink);
        dialogPane.setHeaderText(addLink);
        dialogPane.setContent(controller);
        dialogPane.setGraphic(new ImageView(new Image(new FileInputStream("data/Youtube.48.png"))));

        Button button = (Button) getDialogPane().lookupButton(ok);
        button.disableProperty().bind(controller.urlsProperty().isEmpty());
    }

}
