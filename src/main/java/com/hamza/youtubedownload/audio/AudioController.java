package com.hamza.youtubedownload.audio;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AudioController implements Initializable {

    @FXML
    private Button play, stop, pause;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label l1, l2;

    private MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        String path = "https://stream.radiojar.com/8s5u5tpdtwzuv";
//        String s = new File(path).toURI().toString();
        String path = "ali.mp3";
        File file = new File(path);
        //Instantiating Media class
        if (file.exists()) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } else {
            System.out.println("file not found");
        }
        action();

    }

    private void action() {

        play.setOnAction(actionEvent -> {
            System.out.println(mediaPlayer.getCurrentTime());
//            mediaPlayer.play();
//            l1.setText(mediaPlayer.getStartTime().toString());
//            l2.setText(mediaPlayer.getStopTime().toString());
            mediaPlayer.play();
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {

                    double currentTime = mediaPlayer.getCurrentTime().toMinutes();
                    double stopTime = mediaPlayer.getStopTime().toMinutes();
                    double minutes = mediaPlayer.currentTimeProperty().getValue().toMinutes();
                    while (!mediaPlayer.isMute()) {
//                        l1.setText(String.valueOf(currentTime));
//                        l2.setText(String.valueOf(stopTime));
                        updateProgress(currentTime, stopTime);
                    }

//                double v = ((double) nread / fileLength) * 100;
//                updateMessage(String.valueOf(getRound(v)).concat("%"));
                    return null;
                }

                @Override
                protected void succeeded() {
                    System.out.println("downloaded");
                }

                @Override
                protected void failed() {
                    System.out.println("failed");
                }
            };

            progressBar.progressProperty().bind(task.progressProperty());
//            progressBar.progressProperty().bind(mediaPlayer.balanceProperty());
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

        });

        stop.setOnAction(actionEvent -> {
            System.out.println(mediaPlayer.getCurrentTime());
            mediaPlayer.stop();
        });

        pause.setOnAction(actionEvent -> {
            System.out.println(mediaPlayer.getCurrentTime());
            mediaPlayer.pause();
            l1.setText(mediaPlayer.getStartTime().toString());
            l2.setText(mediaPlayer.getStopTime().toString());
        });


    }
    /*    public Audio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("D:\\Software Testing Tutorial - Learn Unit Testing and Integration Testing.mp4");
        URL url = new URL("http://radio.garden/listen/miraath-al-anbiyya/wfpJOGxF");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("ali.mp3").getAbsoluteFile());

        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        Clip audioClip = (Clip) AudioSystem.getLine(info);
//        audioClip.addLineListener(event ->);
        audioClip.open(audioStream);
        audioClip.start();
    }*/
}
