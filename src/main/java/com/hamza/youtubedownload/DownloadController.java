package com.hamza.youtubedownload;

import com.hamza.youtubedownload.add.AddUrl;
import com.hamza.youtubedownload.setting.SettingApplication;
import com.hamza.youtubedownload.tableSetting.TableColumnAnnotation;
import com.hamza.youtubedownload.utils.AlertSetting;
import com.hamza.youtubedownload.utils.Choose;
import com.hamza.youtubedownload.utils.Config_Data;
import com.hamza.youtubedownload.utils.TestCommands;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.hamza.youtubedownload.Test.getYoutubeId;
import static com.hamza.youtubedownload.add.AddUrlController.SAVE_LINK;

@Log4j2
public class DownloadController implements Initializable {


    @FXML
    private MenuItem menuItem_setting;
    @FXML
    private TableView<LinkModel> tableView;
    @FXML
    private CheckBox check_subtitle;
    @FXML
    private Button addUrl, btnStart;
    @FXML
    private VBox boxTree;

    private final StringProperty url = new SimpleStringProperty();
    private final File directory = new File("data");
    private String youtubeApp;
    private final TestCommands testCommands = new TestCommands();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createDir();
        getTable();
        action();
        addTree();
    }

    private void getTable() {
        tableView.getColumns().clear();
        new TableColumnAnnotation().getTable(tableView, LinkModel.class);

        tableView.getColumns().get(0).setPrefWidth(200);
        tableView.getColumns().get(3).setPrefWidth(100);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDir() {
        File recordsDir = new File(Choose.USER_HOME, ".youtubeDownload");
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        testCommands.setDirectory(directory);
        youtubeApp = directory.getAbsolutePath() + "/yt-dlp ";

    }

    private void addTree() {
        TreeView<String> treeView = new TreeList().getTreeView();
        boxTree.getChildren().add(treeView);
        VBox.setVgrow(treeView, Priority.SOMETIMES);
    }

    private void action() {
        menuItem_setting.setOnAction(actionEvent -> {
            try {
                new SettingApplication().start(new Stage());
            } catch (Exception e) {
                getError(e);
            }
        });

        addUrl.setOnAction(actionEvent -> {
            try {
                AddUrl addUrl1 = new AddUrl();
                Optional<String> s = addUrl1.showAndWait();
                s.ifPresent(s1 -> {
                    url.setValue(s1);
                    addRowToTable();
                });
            } catch (IOException e) {
                getError(e);
            }
        });
    }

    private void addRowToTable() {
        JSONObject jsonObject = new WriteReadFiles().getJsonObject();
        LinkModel model = new LinkModel();
        model.setVideoUrl(jsonObject.get(WriteReadFiles.ID).toString());
        model.setVideoName(jsonObject.get(WriteReadFiles.TITLE).toString());
        double filesizeApprox = Double.parseDouble(jsonObject.get("filesize_approx").toString());
        double pow = Math.pow(1024, 2);
        double size = filesizeApprox / pow;
        model.setLength(Math.round(size) + " MB");
        model.setSaveTo(SAVE_LINK);

        // add image
//        addImageToLink(model);
        tableView.getItems().add(model);
    }

    private void addImageToLink(LinkModel model) {
        String youtubeId = getYoutubeId(url.get());
        ImageView imageView = new ImageView("https://i.ytimg.com/vi/" + youtubeId + "/mqdefault.jpg");
        imageView.setFitHeight(75);
        imageView.setFitWidth(100);
        model.setImageView(imageView);
    }

    protected void onDownloadButtonClick() {
        // do you want to download
        boolean confirm = AlertSetting.confirm();
        if (confirm) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(youtubeApp);
            stringBuilder.append(" -P ");
            stringBuilder.append("\"");
            stringBuilder.append(new Config_Data().getPro("save"));
            stringBuilder.append("\"");
            stringBuilder.append(" -f 22 ");
            stringBuilder.append(" -o \"%(uploader)s/%(title)s.%(ext)s\" ");

            // check subtitle arabic
            if (check_subtitle.isSelected())
                stringBuilder.append(" --write-sub --write-auto-sub --sub-lang \"ar.*\" ");

            stringBuilder.append(" ").append(url.get());
            new Thread(() -> testCommands.processSetting(stringBuilder.toString())).start();
        }
    }

    @FXML
    protected void close() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

    private static void getError(Exception e) {
        log.error(e.getClass().getCanonicalName(), e.getCause());
    }

    // youtube-dl --get-url https://www.youtube.com/watch?v=BaW_jenozKc
    // get url in playlist
    // yt-dlp --flat-playlist -i --print-to-file url file.txt "playlist-url"
    // yt-dlp --flat-playlist -i --print-to-file "%(url)s # %(title)s" batch.txt https://www.youtube.com/playlist?list=PLZV0a2jwt22vMQXKQh-h1vS-Z9XPji0p4
    // write image
    // stringBuilder.append(" -o \"thumbnail:%(uploader)s/image/%(title)s.%(ext)s\" ");
    // stringBuilder.append(TextName.WRITE_THUMBNAIL);
}