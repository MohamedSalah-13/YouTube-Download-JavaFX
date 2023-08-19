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
//    requires eu.hansolo.tilesfx;

    opens com.hamza.youtubedownload to javafx.fxml;
    exports com.hamza.youtubedownload;
    exports com.hamza.youtubedownload.other;
    opens com.hamza.youtubedownload.other to javafx.fxml;
    exports com.hamza.youtubedownload.setting;
    opens com.hamza.youtubedownload.setting to javafx.fxml;
    exports com.hamza.youtubedownload.utils;
    opens com.hamza.youtubedownload.utils to javafx.fxml;
}