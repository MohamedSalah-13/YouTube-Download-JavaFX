package com.hamza.youtubedownload.add;

import com.hamza.youtubedownload.TextName;
import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Config_Data;
import com.hamza.youtubedownload.utils.TestCommands;
import com.hamza.youtubedownload.utils.Validate_Url;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class AddUrlController implements Initializable {

    @FXML
    private TextField text_url;
    @FXML
    private Button search;

    private final StringProperty urls = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text_url.setPromptText("url");
        urls.bind(text_url.textProperty());
        search.setOnAction(actionEvent -> searchVideo());
        search.disableProperty().bind(text_url.textProperty().isEmpty());
    }

    private void searchVideo() {
        try {
            if (urls.get().isEmpty()) {
                new AlertSetting().alertError("Error");
                return;
            }
            if (new Validate_Url().isValidURL(urls.get())) {

                try {
                    // https://www.youtube.com/watch?v=DniTlpZ__z8
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("data/yt-dlp");
                    stringBuilder.append(" -P ");
                    stringBuilder.append("\"");
                    stringBuilder.append(new Config_Data().getPro("save"));
                    stringBuilder.append("\" ");
                    stringBuilder.append(TextName.SKIP_DOWNLOAD);
//                        stringBuilder.append(FLAT_PLAYLIST).append(" -i ").append(PRINT_TO_FILE).append(URL_S_TITLE_S).append(" file.txt");
                    stringBuilder.append("-O \"%(.{filesize_approx,live_status,id,title,url,thumbnails})#j\" ");
                    stringBuilder.append(" ").append(urls.get());
                    stringBuilder.append(" > fileData.json");

                    new Thread(() -> new TestCommands().processSetting(stringBuilder.toString())).start();
                } catch (Exception e) {
                    log.error(e.getClass().getCanonicalName(), e.getCause());
                }

            } else {
                new AlertSetting().alertError("Invalid Url");
            }
        } catch (Exception e) {
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }
    }
}
