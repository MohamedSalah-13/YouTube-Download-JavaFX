package com.hamza.youtubedownload;

import com.hamza.youtubedownload.setting.SettingApplication;
import com.hamza.youtubedownload.tableSetting.TableColumnAnnotation;
import com.hamza.youtubedownload.utils.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static com.hamza.youtubedownload.Test.getYoutubeId;

@Log4j2
public class DownloadController implements Initializable {

    public static final String SKIP_DOWNLOAD = " --skip-download ";
    public static final String GET_TITLE = " --get-title ";
    public static final String WRITE_LINK = " --write-link ";
    public static final String PRINT_TO_FILE = " --print-to-file ";
    public static final String URL_S_TITLE_S = " \"%(url)s # %(title)s\" ";
    public static final String WRITE_THUMBNAIL = "--write-thumbnail ";
    public static final String FLAT_PLAYLIST = " --flat-playlist ";

    @FXML
    private TextField text_url;
    @FXML
    private Label listView;
    @FXML
    private Button download, search;
    @FXML
    private MenuItem menuItem_setting;
    @FXML
    private VBox scrollPane;
    @FXML
    private TableView<LinkModel> tableView;
    @FXML
    private CheckBox check_subtitle, writeImage, skipDownload;


    private final StringProperty url = new SimpleStringProperty();
    private final File directory = new File("data");
    private String youtubeApp;
    private final TestCommands testCommands = new TestCommands();
    private File recordsDir;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // youtube-dl --get-url https://www.youtube.com/watch?v=BaW_jenozKc
        // get url in playlist
        // yt-dlp --flat-playlist -i --print-to-file url file.txt "playlist-url"
        // yt-dlp --flat-playlist -i --print-to-file "%(url)s # %(title)s" batch.txt https://www.youtube.com/playlist?list=PLZV0a2jwt22vMQXKQh-h1vS-Z9XPji0p4

        createDir();
        getTable();
        action();
    }

    private void getTable() {
        tableView.getColumns().clear();
        new TableColumnAnnotation().getTable(tableView, LinkModel.class);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDir() {
        recordsDir = new File(Choose.USER_HOME, ".youtubeDownload");
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }

        testCommands.setDirectory(directory);
        listView.setText("Download Data");
        youtubeApp = directory.getAbsolutePath() + "/yt-dlp ";

        // if windows path file
//        youtubeApp = "yt-dlp ";

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
        download.disableProperty().bind(text_url.textProperty().isEmpty());
        search.disableProperty().bind(text_url.textProperty().isEmpty());
    }

    private void searchVideo() {
        try {
            if (url.get().isEmpty()) {
                new AlertSetting().alertError("Error");
                return;
            }
            if (new Validate_Url().isValidURL(url.get())) {
                Thread thread = new Thread(() -> {
                    try {
//                        String title_text = recordsDir.getAbsolutePath() + "/title.txt";
//                        String links_text = recordsDir.getAbsolutePath() + "/links.txt";
//                        testCommands.processSetting(youtubeApp + " " + SKIP_DOWNLOAD + " " + GET_TITLE + "  " + url + " > " + title_text);
//                        testCommands.processSetting(youtubeApp + " " + SKIP_DOWNLOAD + " " + WRITE_LINK + " " + url + " > " + links_text);
//                        log.info("true");

// https://www.youtube.com/watch?v=DniTlpZ__z8

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(youtubeApp);
                        stringBuilder.append(" -P ");
                        stringBuilder.append("\"");
                        stringBuilder.append(new Config_Data().getPro("save"));
                        stringBuilder.append("\" ");
                        stringBuilder.append(SKIP_DOWNLOAD);
                        stringBuilder.append(FLAT_PLAYLIST);
                        stringBuilder.append(" -i ");
                        stringBuilder.append(PRINT_TO_FILE).append(URL_S_TITLE_S).append(" file.txt");
                        stringBuilder.append(" ").append(url.get());

                        new Thread(() -> testCommands.processSetting(stringBuilder.toString())).start();

                        LinkModel model = new LinkModel();
                        model.setLength(10);
                        model.setVideoUrl("URL");
                        model.setVideoName("Name");
                        String youtubeId = getYoutubeId(url.get());
                        ImageView imageView = new ImageView("https://i.ytimg.com/vi/" + youtubeId + "/mqdefault.jpg");
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(150);
                        model.setImageView(imageView);

                        tableView.getItems().add(model);

                    } catch (Exception e) {
                        getError(e);
                    }
                });
                thread.start();
            } else {
                new AlertSetting().alertError("Invalid Url");
                text_url.requestFocus();
            }
        } catch (Exception e) {
            getError(e);
        }
    }

    @FXML
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

            // skip download
            if (skipDownload.isSelected()) {
                stringBuilder.append(SKIP_DOWNLOAD);
                // add folder of name uploader and title video
            } else {
                stringBuilder.append(" -f 22 ");
                stringBuilder.append(" -o \"%(uploader)s/%(title)s.%(ext)s\" ");
            }

            // write image
            if (writeImage.isSelected()) {
                stringBuilder.append(" -o \"thumbnail:%(uploader)s/image/%(title)s.%(ext)s\" ");
                stringBuilder.append(WRITE_THUMBNAIL);
            }

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

    @FXML
    protected void remove() {
        listView.setText("");
        text_url.clear();
    }


    private void readFromInputStream(String urlPath) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(urlPath)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    scrollPane.getChildren().add(vBox(line, ""));
                }
            }
        } catch (IOException e) {
            getError(e);
        }
    }

    private void checkSubtitle(String file) {
        try {
            String x = testCommands.readFromInputStream(new FileInputStream(directory.getAbsolutePath() + "/" + file));
            if (!x.contains("has no subtitles")) {
                check_subtitle.setSelected(false);
            }
        } catch (Exception e) {
            getError(e);
        }
    }

    private HBox vBox(String text, String imageName) {
        Label label = new Label(text);
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        try {
            imageName = "rQKefpAzq3Q";
            imageView.setImage(new Image("https://i.ytimg.com/vi/" + imageName + "/mqdefault.jpg"));
        } catch (Exception e) {
            getError(e);
        }

        hBox.getChildren().addAll(imageView, label);
        return hBox;
    }

    private static void getError(Exception e) {
        log.error(e.getClass().getCanonicalName(), e.getCause());
    }
}