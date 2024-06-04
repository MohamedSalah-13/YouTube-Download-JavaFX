package com.hamza.youtubedownload;

import com.hamza.youtubedownload.utils.DownloadTask;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DownloadLinks extends Application {
    private Parent createContent() {
        VBox vBox = new VBox();
        vBox.setPrefSize(400, 600);
        TextField textField = new TextField();
        vBox.getChildren().addAll(textField);

        textField.setOnAction(actionEvent -> {
            Task<Void> task = new DownloadTask(textField.getText());

            HBox hBox = new HBox(5);
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(350);
//            progressBar.set
            progressBar.progressProperty().bind(task.progressProperty());
            Label label = new Label("0");
            label.textProperty().bind(task.messageProperty());
            hBox.getChildren().addAll(progressBar, label);
            vBox.getChildren().add(hBox);

            textField.clear();
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        return vBox;
    }


    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(createContent()));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

//    https://rr1---sn-uxaxjvhxbt2u-amge.googlevideo.com/videoplayback?expire=1692967748&ei=5E7oZK7hO5u-mLAPkMqqoAw&ip=156.207.85.39
//    &id=o-AC_1bImkvh1GVHZSzY9R_z5GwxNS04JiGyAwrqTd9tWp&itag=22&source=youtube&requiressl=yes&mh=bb&mm=31%2C29&mn=sn-uxaxjvhxbt2u-amge%2Csn
//    -hgn7ynek&ms=au%2Crdu&mv=m&mvi=1&pl=19&initcwndbps=305000&spc=UWF9fz19CR79NHNcJfDPOS7c4YeWjW4&vprv=1&svpuc=1&mime=video%2Fmp4&cnr=14&ratebypass=
//    yes&dur=962.977&lmt=1676184361727174&mt=1692945662&fvip=5&fexp=24007246%2C24363393&c=ANDROID&txp=6218224&sparams=
//    expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=
//    AOq0QJ8wRQIgCOqQc6be4qmc_O5YL02zG3Oyv8wahzRyVls1RKCBUWoCIQC2jvm5iKVTP4tn1fliQM2Dz3v_sW41RIlohOffPUSvKw%3D%3D&lsparams=
//    mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRgIhALk9S6nATCQ4dri5ItLHbQMVEYgaQ7GC0nlMSv0HKV71AiEA9tMIxTkpfMfNAmhl0-V-zbqjkxmkxVE6Xu-Dr8kCq60%3D

    // https://lumiere-a.akamaihd.net/v1/images/sa_pixar_virtualbg_up_16x9_1f36fba7.jpeg
    // https://archive.org/19/items/win-7-pro-32-64-iso/32-bit/GSP1RMCPRXFRER_EN_DVD.ISO
    // https://lumiere-a.akamaihd.net/v1/images/sa_pixar_virtualbg_toystory_16x9_8461039f.jpeg

//    https://archive.org/download/windows-7-ultimate-32-64bit-iso_20201128/WIN7SP1_NEW.iso

 //   https://www.journeybytes.com/generate-video-download-links-youtube-dl/

}
