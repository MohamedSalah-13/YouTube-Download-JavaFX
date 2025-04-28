module com.hamza.youtubedownload {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires commons.validator;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires lombok;
    requires javafx.swing;
    requires zip4j;
//    requires json.simple;
    requires org.json;


    requires javafx.media;
    exports com.hamza.youtubedownload.other;
    opens com.hamza.youtubedownload.other to javafx.fxml;
    exports com.hamza.youtubedownload.view;
    opens com.hamza.youtubedownload.view to javafx.fxml;
    exports com.hamza.youtubedownload.utils;
    opens com.hamza.youtubedownload.utils to javafx.fxml;
    exports com.hamza.youtubedownload.controller;
    opens com.hamza.youtubedownload.controller to javafx.fxml;
    exports com.hamza.youtubedownload.model;
    opens com.hamza.youtubedownload.model to javafx.fxml;
    exports com.hamza.youtubedownload.youtube;
    opens com.hamza.youtubedownload.youtube to javafx.fxml;
}