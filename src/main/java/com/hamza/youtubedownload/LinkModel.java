package com.hamza.youtubedownload;

import com.hamza.youtubedownload.tableSetting.ColumnData;
import javafx.scene.image.ImageView;
import lombok.Data;

@Data
public class LinkModel {

    @ColumnData(titleName = "name")
    private String videoName;
    @ColumnData(titleName = "url")
    private String videoUrl;
    @ColumnData(titleName = "length")
    private String length;
    @ColumnData(titleName = "image")
    private ImageView imageView;
}
