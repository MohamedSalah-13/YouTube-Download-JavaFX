package com.hamza.youtubedownload.add;

import com.hamza.youtubedownload.TextName;
import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Config_Data;
import com.hamza.youtubedownload.utils.TestCommands;
import com.hamza.youtubedownload.utils.Validate_Url;
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

    public static final String SAVE_LINK = new Config_Data().getPro("save");
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
        StringBuilder stringBuilder = new StringBuilder();
        if (!getUrls().isEmpty())
            if (new Validate_Url().isValidURL(urls.get())) {
                getData(stringBuilder);
            } else {
                new AlertSetting().alertError("Invalid Url");
            }

        return stringBuilder.toString();
    }

    private void getData(StringBuilder stringBuilder) {
        try {
            // https://www.youtube.com/watch?v=DniTlpZ__z8
            File file = new File("data");
            TestCommands testCommands = new TestCommands();
            testCommands.setDirectory(file);
            stringBuilder.append(file.getAbsolutePath()).append("/yt-dlp");
            stringBuilder.append(" -P ");
            stringBuilder.append("\"");
            stringBuilder.append(SAVE_LINK);
            stringBuilder.append("\" ");
            stringBuilder.append(TextName.SKIP_DOWNLOAD);
//                        stringBuilder.append(FLAT_PLAYLIST).append(" -i ").append(PRINT_TO_FILE).append(URL_S_TITLE_S).append(" file.txt");
            stringBuilder.append("-O \"%(.{filesize_approx,live_status,id,title,url,thumbnails})#j\" ");
            stringBuilder.append(" ").append(getUrls());
            stringBuilder.append(" > fileData.json");
            testCommands.processSetting(stringBuilder.toString());

        } catch (Exception e) {
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }
    }

    public String getUrls() {
        return urls.get();
    }

    public StringProperty urlsProperty() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls.set(urls);
    }
}
