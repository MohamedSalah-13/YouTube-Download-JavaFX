package com.hamza.youtubedownload.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;

@Log4j2
public class Config_Data {

    private final Configs configs;

    public Config_Data() {
        File FILE = new File("data/config.properties");
        configs = new Configs(FILE);
    }

    public void saveProp(String title, String value) {
        try {
            configs.saveProp(title, value);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public String getPro(String title) {
        try {
            return configs.getPro(title);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }
}