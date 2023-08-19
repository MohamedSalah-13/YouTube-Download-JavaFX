package com.hamza.youtubedownload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Configs {

    private final Properties PROPERTIES = new Properties();
    private final File FILE = new File("data/config.properties");
    // for config

    public void saveProp(String title, String value) {
        try {
            PROPERTIES.setProperty(title, value);
            PROPERTIES.store(new FileOutputStream(FILE), null);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public String getPro(String title) {
        String value = null;
        FileInputStream input = null;

        try {
            input = new FileInputStream(FILE);
            PROPERTIES.load(input);
            value = PROPERTIES.getProperty(title);
            if (value == null) {
                value = "false";
            }
        } catch (IOException e) {
            log.error(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }

        return value;
    }
}