package com.hamza.youtubedownload.controller;

import com.hamza.youtubedownload.utils.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class AddUrlController extends HBox {


    private final StringProperty urls = new SimpleStringProperty();

    public AddUrlController() {
        TextField text_url = new TextField();
        text_url.setPrefWidth(300);
        text_url.setPromptText("add link");
        text_url.requestFocus();
        urls.bindBidirectional(text_url.textProperty());

        this.getChildren().addAll(new Label("link"), text_url);
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(text_url, Priority.SOMETIMES);
    }

    public String searchVideo() {
        Thread thread = new Thread(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            if (!getUrls().isEmpty())
                if (new Validate_Url().isValidURL(urls.get())) {
                    getData(stringBuilder);
                } else {
                    new AlertSetting().alertError("Invalid Url");
                }
        });
        thread.start();
        return urls.get();
    }

    private void getData(StringBuilder stringBuilder) {
        try {
            TestCommands testCommands = new TestCommands();
            testCommands.setDirectory(Config_Data.FILE_DOWNLOAD);
            stringBuilder.append("yt-dlp");
            stringBuilder.append(" -P ");
            stringBuilder.append("\"");
            stringBuilder.append(Choose.getPlacedSave());
            stringBuilder.append("\" ");
            stringBuilder.append(TextName.SKIP_DOWNLOAD).append(" ");
            stringBuilder.append(TextName.WRITE_INFO_JSON).append(" ");
            stringBuilder.append("-O \"%(.{filesize,filesize_approx,live_status,id,title,url,thumbnails})#j\" ");
            stringBuilder.append(" ").append(getUrls());
            stringBuilder.append(" > %(id)s.json");
            testCommands.processSetting(stringBuilder.toString());

        } catch (Exception e) {
            log.error(e.getClass(), e.getCause());
        }
    }

    public String getUrls() {
        return urls.get();
    }

    public StringProperty urlsProperty() {
        return urls;
    }

}
