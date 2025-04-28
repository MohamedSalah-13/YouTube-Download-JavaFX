package com.hamza.youtubedownload.controller;

import com.hamza.youtubedownload.model.LinkModel;
import com.hamza.youtubedownload.other.WriteReadFiles;
import com.hamza.youtubedownload.view.AddUrl;
import com.hamza.youtubedownload.view.SettingApplication;
import com.hamza.youtubedownload.tableSetting.TableColumnAnnotation;
import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.TestCommands;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.hamza.youtubedownload.view.Test.getYoutubeId;
import static com.hamza.youtubedownload.controller.AddUrlController.SAVE_LINK;

@Log4j2
@RequiredArgsConstructor
public class DownloadController implements Initializable {
    public static final String APP_DIR_NAME = ".youtubeDownload";
    private static final String YT_DLP_COMMAND = "yt-dlp";
    private static final String THUMBNAIL_URL_TEMPLATE = "https://i.ytimg.com/vi/%s/mqdefault.jpg";
    private static final double BYTES_TO_MB = Math.pow(1024, 2);
    private static final int THUMBNAIL_HEIGHT = 75;
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int NAME_COLUMN_WIDTH = 200;
    private static final int DATE_COLUMN_WIDTH = 100;
    // State
    private final StringProperty currentUrl = new SimpleStringProperty();
    private final File dataDirectory = new File("data");
    // UI Components
    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private TableView<LinkModel> downloadTable;
    @FXML
    private CheckBox subtitleCheckbox;
    @FXML
    private Button addUrlButton;
    @FXML
    private Button startButton,pushButton;
    @FXML
    private VBox treeContainer;
    @FXML
    private TextArea logTextArea;

    private static void logError(Exception e) {
        log.error(e.getClass().getCanonicalName(), e.getCause());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeEventHandlers();
        initializeTreeView();
    }

    private void initializeTable() {
        downloadTable.getColumns().clear();
        new TableColumnAnnotation().getTable(downloadTable, LinkModel.class);
        downloadTable.getColumns().get(0).setPrefWidth(NAME_COLUMN_WIDTH);
        downloadTable.getColumns().get(3).setPrefWidth(DATE_COLUMN_WIDTH);

        downloadTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LinkModel selectedItem = downloadTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    openFileLocation(new File(selectedItem.getSaveTo()));
                }
            }
        });
    }

    private void initializeTreeView() {
        TreeView<String> treeView = new TreeList().getTreeView();
        treeContainer.getChildren().add(treeView);
        VBox.setVgrow(treeView, Priority.SOMETIMES);
    }

    private void initializeEventHandlers() {
        settingsMenuItem.setOnAction(this::handleSettingsAction);
        addUrlButton.setOnAction(this::handleAddUrlAction);
        startButton.setOnAction(this::handleStartAction);

        pushButton.setOnAction(event -> {
            if (currentUrl.get() != null && !currentUrl.get().isEmpty()) {
                addDownloadToTable();
            } else {
                new AlertSetting().alertError("Please add a url");
            }
        });
    }

    private void handleSettingsAction(ActionEvent event) {
        try {
            new SettingApplication().start(new Stage());
        } catch (Exception e) {
            logError(e);
        }
    }

    private void handleAddUrlAction(ActionEvent event) {
        try {
            new AddUrl().showAndWait().ifPresent(url -> {
                System.out.println(url);
                currentUrl.setValue(url);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                addDownloadToTable();
            });
        } catch (IOException e) {
            logError(e);
        }
    }

    private void handleStartAction(ActionEvent event) {
        try {
            startDownload();
        } catch (Exception e) {
            logError(e);
            new AlertSetting().alertError("Error");
        }
    }

    private void startDownload() {
        if (!AlertSetting.confirm()) {
            return;
        }
        String command = buildDownloadCommand();
        new Thread(() -> new TestCommands(logTextArea).processSetting(command)).start();
    }

    private String buildDownloadCommand() {
        var selectedItem = downloadTable.getSelectionModel().getSelectedItem();
        return new StringBuilder()
                .append(YT_DLP_COMMAND)
                .append(" -P \"")
                .append(SAVE_LINK)
                .append("\"")
//                .append(" -o \"%(uploader)s/%(title)s.%(ext)s\"")
                .append(" -f bestvideo+bestaudio ")
//                .append(subtitleCheckbox.isSelected() ? " --write-sub --write-auto-sub --sub-lang \"ar.*\" " : "")
//                .append(" ")
                .append(selectedItem.getVideoUrl())
                .toString();


    }

    private void addDownloadToTable() {
        JSONObject videoInfo = new WriteReadFiles().getJsonObject();
        LinkModel download = createDownloadModel(videoInfo);
        addThumbnailToDownload(download);
        downloadTable.getItems().add(download);
    }

    private LinkModel createDownloadModel(JSONObject videoInfo) {
        LinkModel model = new LinkModel();
        model.setVideoUrl(videoInfo.getString(WriteReadFiles.ID));
        model.setVideoName(videoInfo.getString(WriteReadFiles.TITLE));
        model.setLength(getFileSize(videoInfo) + " MB");
        model.setSaveTo(SAVE_LINK);
        return model;
    }

    private long getFileSize(JSONObject jsonObject) {
        // Try exact filesize first, fall back to approximate if not available
        if (jsonObject.has("filesize")) {
            return jsonObject.getLong("filesize");
        } else if (jsonObject.has("filesize_approx")) {
            return Math.round(jsonObject.getDouble("filesize_approx") / BYTES_TO_MB);
        }
        return -1L; // Indicate size not available
    }

    private void addThumbnailToDownload(LinkModel download) {
        String youtubeId = getYoutubeId(currentUrl.get());
        ImageView thumbnail = new ImageView(String.format(THUMBNAIL_URL_TEMPLATE, youtubeId));
        thumbnail.setFitHeight(THUMBNAIL_HEIGHT);
        thumbnail.setFitWidth(THUMBNAIL_WIDTH);
        download.setImageView(thumbnail);
    }
    

    @FXML
    protected void close() {
        Stage stage = (Stage) downloadTable.getScene().getWindow();
        stage.close();
    }

    private void openFileLocation(File directory) {
        try {
            if (directory.exists()) {
                Desktop.getDesktop().open(directory);
            } else {
                new AlertSetting().alertError("Directory does not exist");
            }
        } catch (IOException e) {
            logError(e);
            new AlertSetting().alertError("Could not open directory");
        }
    }
}
// youtube-dl --get-url https://www.youtube.com/watch?v=BaW_jenozKc
// get url in playlist
// yt-dlp --flat-playlist -i --print-to-file url file.txt "playlist-url"
// yt-dlp --flat-playlist -i --print-to-file "%(url)s # %(title)s" batch.txt https://www.youtube.com/playlist?list=PLZV0a2jwt22vMQXKQh-h1vS-Z9XPji0p4
// write image
// stringBuilder.append(" -o \"thumbnail:%(uploader)s/image/%(title)s.%(ext)s\" ");
// stringBuilder.append(TextName.WRITE_THUMBNAIL);
