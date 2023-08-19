package com.hamza.youtubedownload;

import com.hamza.youtubedownload.other.Data_Setting;
import com.hamza.youtubedownload.other.LinuxFile;
import com.hamza.youtubedownload.setting.SettingApplication;
import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Configs;
import com.hamza.youtubedownload.utils.TestCommands;
import com.hamza.youtubedownload.utils.Validate_Url;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class DownloadController implements Initializable {

    @FXML
    private TextField text_url;
    @FXML
    private Label listView;
    @FXML
    private ComboBox<String> subtitle;
    @FXML
    private Button download;
    @FXML
    private MenuItem menuItem_setting;

    private String url;
    private Boolean check_subtitle = true;
    private final File directory = new File("data");
    private String youtubeApp, text;
    private Data_Setting dataSetting;
    private TestCommands testCommands;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        youtubeApp = directory.getAbsolutePath() + "/yt-dlp ";
        text = directory.getAbsolutePath() + "/sub.txt";
// https://www.youtube.com/watch?v=XBu54nfzxAQ
//        System.out.println(directory.getAbsolutePath());
//        System.out.println(text);
//        text_url.setText("https://www.youtube.com/watch?v=UvQI5JprHaQ");
        text_url.setText("https://www.youtube.com/watch?v=YNeYz-ExHMM");  // no sub
        this.url = text_url.getText();

        // combobox
        subtitle.getItems().addAll("Arabic", "English");

        dataSetting = new LinuxFile();
        testCommands = new TestCommands(dataSetting);

        // action
        action();
    }

    private void action() {
        menuItem_setting.setOnAction(actionEvent -> {
            try {
                new SettingApplication().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        download.disableProperty().bind(new BooleanBinding() {
            {
                bind(text_url.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return text_url.getText().isEmpty();
            }
        });
    }

    @FXML
    protected void onHelloButtonClick() {
        try {
            url = text_url.getText();
            if (url.isEmpty()) {
                new AlertSetting().alertError("Error");
                return;
            }
            if (new Validate_Url().isValidURL(url)) {
                testCommands.execute("echo \"#current time\" > " + text);
                testCommands.execute(dataSetting.time() + " >>" + text);

                // get video format to download
                testCommands.processSetting(youtubeApp + "-F " + url);

            } else {
                new AlertSetting().alertError("Invalid Url");
                text_url.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkSubtitle(String file) {
        try {
            String x = testCommands.readFromInputStream(new FileInputStream(directory.getAbsolutePath() + "/" + file));
            if (!x.contains("has no subtitles")) {
                subtitle.setDisable(false);
                check_subtitle = true;
            } else subtitle.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDownloadButtonClick() {
        // do you want to download
        boolean confirm = AlertSetting.confirm();
        if (confirm) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(youtubeApp);
            stringBuilder.append("-P ");
            stringBuilder.append("\"");
            stringBuilder.append(new Configs().getPro("save"));
            stringBuilder.append("\"");
            stringBuilder.append(" -f 22 ");

            // check subtitle
            if (check_subtitle)
                if (!subtitle.getSelectionModel().isEmpty()) {
                    stringBuilder.append("--write-sub --write-auto-sub --sub-lang ");
                    if (subtitle.getSelectionModel().getSelectedIndex() == 0) stringBuilder.append("\"ar.*\"");
                    else stringBuilder.append("\"en.*\"");
                }

            stringBuilder.append(" ").append(url);

            System.out.println(stringBuilder);
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    testCommands.processSetting(stringBuilder.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }
    }

    @FXML
    protected void close() {
        Stage stage = (Stage) text_url.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void remove() {
        listView.setText("");
        text_url.clear();
        subtitle.getSelectionModel().clearSelection();
    }

}