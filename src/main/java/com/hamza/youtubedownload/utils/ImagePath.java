package com.hamza.youtubedownload.utils;

import com.hamza.youtubedownload.view.DownloadApplication;

import java.io.InputStream;

public class ImagePath {

    public final InputStream YOUTUBE_ICON_STREAM = DownloadApplication.class.getResourceAsStream("image/Youtube.48.png");
    public final InputStream SETTING_STREAM = DownloadApplication.class.getResourceAsStream("image/Settings.png");
    public static final String STYLE_CSS_STREAM = DownloadApplication.class.getResource("style.css").toExternalForm();
}
