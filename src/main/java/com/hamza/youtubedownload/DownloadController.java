package com.hamza.youtubedownload;

import com.hamza.youtubedownload.setting.SettingApplication;
import com.hamza.youtubedownload.tableSetting.TableColumnAnnotation;
import com.hamza.youtubedownload.utils.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.hamza.youtubedownload.Test.getYoutubeId;

@Log4j2
public class DownloadController implements Initializable {

    @FXML
    private TextField text_url;
    @FXML
    private Button search;
    @FXML
    private MenuItem menuItem_setting;
    @FXML
    private TableView<LinkModel> tableView;
    @FXML
    private CheckBox check_subtitle;

    private final StringProperty url = new SimpleStringProperty();
    private final File directory = new File("data");
    private String youtubeApp;
    private final TestCommands testCommands = new TestCommands();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createDir();
        getTable();
        action();
    }

    private void getTable() {
        tableView.getColumns().clear();
        new TableColumnAnnotation().getTable(tableView, LinkModel.class);

        tableView.getColumns().get(0).setPrefWidth(200);
        tableView.getColumns().get(1).setPrefWidth(200);
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
        text_url.setPromptText("url");
        url.bind(text_url.textProperty());
    }

    private void action() {
        menuItem_setting.setOnAction(actionEvent -> {
            try {
                new SettingApplication().start(new Stage());
            } catch (Exception e) {
                getError(e);
            }
        });

        search.setOnAction(actionEvent -> searchVideo());
        search.disableProperty().bind(text_url.textProperty().isEmpty());
    }

    private void searchVideo() {
        try {
            if (url.get().isEmpty()) {
                new AlertSetting().alertError("Error");
                return;
            }
            if (new Validate_Url().isValidURL(url.get())) {

                try {
                    // https://www.youtube.com/watch?v=DniTlpZ__z8
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(youtubeApp);
                    stringBuilder.append(" -P ");
                    stringBuilder.append("\"");
                    stringBuilder.append(new Config_Data().getPro("save"));
                    stringBuilder.append("\" ");
                    stringBuilder.append(TextName.SKIP_DOWNLOAD);
//                        stringBuilder.append(FLAT_PLAYLIST).append(" -i ").append(PRINT_TO_FILE).append(URL_S_TITLE_S).append(" file.txt");
                    stringBuilder.append("-O \"%(.{filesize_approx,live_status,id,title,url,thumbnails})#j\" ");
                    stringBuilder.append(" ").append(url.get());
                    stringBuilder.append(" > fileData.json");

                    new Thread(() -> testCommands.processSetting(stringBuilder.toString())).start();
                    addRowToTable();
                } catch (Exception e) {
                    getError(e);
                }

            } else {
                new AlertSetting().alertError("Invalid Url");
                text_url.requestFocus();
            }
        } catch (Exception e) {
            getError(e);
        }
    }

    private void addRowToTable() {
        JSONObject jsonObject = new WriteReadFiles().getJsonObject();
        LinkModel model = new LinkModel();
        model.setVideoUrl(jsonObject.get(WriteReadFiles.ID).toString());
        model.setVideoName(jsonObject.get(WriteReadFiles.TITLE).toString());
        double filesizeApprox = Double.parseDouble(jsonObject.get("filesize_approx").toString());
        double pow = Math.pow(1024, 2);
        double pow1 = Math.pow(2, 20);
        System.out.println(pow + "  " + pow1);
        double size = filesizeApprox / pow;
        model.setLength(Math.round(size) + " MB");

        String youtubeId = getYoutubeId(url.get());
        ImageView imageView = new ImageView("https://i.ytimg.com/vi/" + youtubeId + "/mqdefault.jpg");
        imageView.setFitHeight(75);
        imageView.setFitWidth(100);
        model.setImageView(imageView);
        tableView.getItems().add(model);
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
        Stage stage = (Stage) text_url.getScene().getWindow();
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