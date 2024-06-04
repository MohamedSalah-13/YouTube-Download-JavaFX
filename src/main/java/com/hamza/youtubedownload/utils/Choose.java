package com.hamza.youtubedownload.utils;

import java.io.File;

public class Choose {

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String OPERATE_SYSTEM = System.getProperty("os.name");
    public static final String USER_NAME = System.getProperty("user.name");
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String PROPERTY_TEMP = System.getProperty("java.io.tmpdir");

    public String getPlacedSave() {
        System.out.println(USER_HOME);
        System.out.println(USER_DIR);
        Config_Data configData = new Config_Data();
        String save = configData.getPro("save");
        File file = new File(save);
        if (!file.isDirectory()) {
//            configs.saveProp("save", userHome);
            save = USER_HOME;
        }
        return save;
    }
}
