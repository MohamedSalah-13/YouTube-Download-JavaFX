package com.hamza.youtubedownload.other;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.hamza.youtubedownload.utils.Config_Data.FILE_DOWNLOAD;

@Log4j2
public class WriteReadFiles {

    public static final String ID = "id";
    public static final String TITLE = "title";

    public JSONObject getJsonObject(String id) {
        try (FileReader reader = new FileReader(FILE_DOWNLOAD + File.separator + id + ".json")) {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[1024];
            int charsRead;

            while ((charsRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
                System.out.println(content);
            }
            return new JSONObject(content.toString());
        } catch (IOException e) {
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }
        return null;
    }

}

