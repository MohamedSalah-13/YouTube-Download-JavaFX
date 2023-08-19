package com.hamza.youtubedownload.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Choose {

    public String chooseFile() {
//        final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        DirectoryChooser fc = new DirectoryChooser();
        fc.setInitialDirectory(new File(getPlacedSave()));
        File dirTo = fc.showDialog(new Stage());
        if (dirTo != null) {
            return dirTo.getAbsolutePath();
        }
        else return getPlacedSave();
    }

    public String getPlacedSave() {
        String userHome = System.getProperty("user.home");
        Configs configs = new Configs();
        String save = configs.getPro("save");
        File file = new File(save);
        if (!file.isDirectory()) {
//            configs.saveProp("save", userHome);
            save = userHome;
        }
        return save;
    }
}
