package com.hamza.youtubedownload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configs {

    private final Properties PROPERTIES = new Properties();
    private final File FILE;

    public Configs(File FILE) {
        this.FILE = FILE;
    }

    public void saveProp(String title, String value) throws IOException {
        PROPERTIES.setProperty(title, value);
        PROPERTIES.store(new FileOutputStream(FILE), null);
    }

    public String getPro(String title) throws IOException {
        String value;
        try (FileInputStream input = new FileInputStream(FILE)) {
            PROPERTIES.load(input);
            value = PROPERTIES.getProperty(title);
            if (value == null) {
                value = "false";
            }
        }
        return value;
    }
}