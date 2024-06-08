package com.hamza.youtubedownload;

import com.hamza.youtubedownload.tableSetting.ColumnData;
import javafx.scene.image.ImageView;
import lombok.Data;

import java.util.Date;

@Data
public class LinkModel {

    @ColumnData(titleName = "name")
    private String videoName;
    @ColumnData(titleName = "length")
    private String length;
    @ColumnData(titleName = "type")
    private String type;
    @ColumnData(titleName = "date")
    private Date date;
    @ColumnData(titleName = "save to")
    private String saveTo;

    private String videoUrl;
    private ImageView imageView;
}
