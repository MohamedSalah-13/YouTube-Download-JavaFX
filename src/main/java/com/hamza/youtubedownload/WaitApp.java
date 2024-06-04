package com.hamza.youtubedownload;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WaitApp extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        String pleaseWait = "Please Wait";
        Text text = new Text(pleaseWait);
        text.setFont(Font.font(20));
        text.setFill(Color.DARKSEAGREEN);

        VBox vBox = new VBox(text);
        vBox.setAlignment(Pos.CENTER);
//        vBox.setStyle("-fx-border-color: #777");
        vBox.getChildren().add(getProgressIndicator());
        vBox.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        Scene scene = new Scene(vBox, 320, 240);
        stage.setScene(scene);
//        scene.setFill(Color.TRANSPARENT);
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private static ProgressIndicator getProgressIndicator() {
        ProgressIndicator loadProgress = new ProgressIndicator();
        loadProgress.setSkin(null);
        loadProgress.setPrefWidth(50);
        return loadProgress;
    }


    public void closeStage() {
        stage.close();
    }
}
