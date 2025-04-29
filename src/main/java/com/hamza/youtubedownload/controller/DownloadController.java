package com.hamza.youtubedownload.controller;

import com.hamza.youtubedownload.model.LinkModel;
import com.hamza.youtubedownload.other.WriteReadFiles;
import com.hamza.youtubedownload.tableSetting.TableColumnAnnotation;
import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Choose;
import com.hamza.youtubedownload.utils.TestCommands;
import com.hamza.youtubedownload.view.AddUrl;
import com.hamza.youtubedownload.view.SettingApplication;
import com.hamza.youtubedownload.view.VideoDetailsApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.hamza.youtubedownload.view.Test.getYoutubeId;

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
    // UI Components
    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private TableView<LinkModel> downloadTable;
    @FXML
    private Button addUrlButton;
    @FXML
    private Button startButton;
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

        // Create context menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewDetailsMenuItem = new MenuItem("View Video Details");
        MenuItem openLocationMenuItem = new MenuItem("Open File Location");

        viewDetailsMenuItem.setOnAction(event -> {
            LinkModel selectedItem = downloadTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                try {
                    String command = buildDownloadCommand();
                    VideoDetailsApplication.showVideoDetails(selectedItem, command);
                } catch (IOException e) {
                    logError(e);
                    new AlertSetting().alertError("Could not open video details");
                }
            }
        });

        openLocationMenuItem.setOnAction(event -> {
            LinkModel selectedItem = downloadTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                openFileLocation(new File(selectedItem.getSaveTo()));
            }
        });

        contextMenu.getItems().addAll(viewDetailsMenuItem, openLocationMenuItem);
        downloadTable.setContextMenu(contextMenu);

        // Handle double-click
        downloadTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LinkModel selectedItem = downloadTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        // Open video details window on double-click
                        String command = buildDownloadCommand();
                        VideoDetailsApplication.showVideoDetails(selectedItem, command);
                    } catch (IOException e) {
                        logError(e);
                        new AlertSetting().alertError("Could not open video details");
                    }
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
                try {
                    System.out.println(url);
                    currentUrl.setValue(url);
                    Thread.sleep(1000);
                    addDownloadToTable("fileData");
                } catch (InterruptedException e) {
                    logError(e);
                }
            });
        } catch (IOException e) {
            logError(e);
        }
    }

    private void handleStartAction(ActionEvent event) {
        try {
            openVideoDetailsWindow();
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

    private void openVideoDetailsWindow() throws IOException {
        if (!AlertSetting.confirm()) {
            return;
        }

        LinkModel selectedItem = downloadTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            new AlertSetting().alertError("Please select a video to download");
            return;
        }

        String command = buildDownloadCommand();
        VideoDetailsApplication.showVideoDetails(selectedItem, command);
    }

    private String buildDownloadCommand() {
        var selectedItem = downloadTable.getSelectionModel().getSelectedItem();
        return new StringBuilder()
                .append(YT_DLP_COMMAND)
                .append(" -P \"")
                .append(Choose.getPlacedSave())
                .append("\"")
//                .append(" -o \"%(uploader)s/%(title)s.%(ext)s\"")
                .append(" -f bestvideo+bestaudio ")
//                .append(subtitleCheckbox.isSelected() ? " --write-sub --write-auto-sub --sub-lang \"ar.*\" " : "")
//                .append(" ")
                .append(selectedItem.getVideoUrl())
                .toString();


    }

    private void addDownloadToTable(String videoUrl) {
        JSONObject videoInfo = new WriteReadFiles().getJsonObject(videoUrl);
        LinkModel download = createDownloadModel(videoInfo);
        addThumbnailToDownload(download);
        downloadTable.getItems().add(download);
    }

    private LinkModel createDownloadModel(JSONObject videoInfo) {
        LinkModel model = new LinkModel();
        model.setVideoUrl(videoInfo.getString(WriteReadFiles.ID));
        model.setVideoName(videoInfo.getString(WriteReadFiles.TITLE));
        model.setLength(getFileSize(videoInfo) + " MB");
        model.setSaveTo(Choose.getPlacedSave());
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
