package com.hamza.youtubedownload.view;

import com.hamza.youtubedownload.controller.AddUrlController;
import com.hamza.youtubedownload.utils.ImagePath;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AddUrl extends Dialog<String> {

    public AddUrl() throws IOException {
        AddUrlController controller = new AddUrlController();
        DialogPane dialogPane = this.getDialogPane();
        ButtonType ok = ButtonType.OK;
        dialogPane.getButtonTypes().addAll(ok, ButtonType.CANCEL);
        dialogPane.getStylesheets().add(ImagePath.STYLE_CSS_STREAM);

        this.setResultConverter((var1x) -> {
            ButtonBar.ButtonData var2 = var1x == null ? null : var1x.getButtonData();
            return var2 == ButtonBar.ButtonData.OK_DONE ? controller.searchVideo() : null;
        });

        String addLink = "Add Link";
        Stage window = (Stage) dialogPane.getScene().getWindow();
        window.getIcons().add(new Image(new ImagePath().YOUTUBE_ICON_STREAM));
        window.setTitle(addLink);
        dialogPane.setHeaderText(addLink);
        dialogPane.setContent(controller);
        dialogPane.setGraphic(new ImageView(new Image(new ImagePath().YOUTUBE_ICON_STREAM)));

        Button button = (Button) getDialogPane().lookupButton(ok);
        button.disableProperty().bind(controller.urlsProperty().isEmpty());
    }

}
