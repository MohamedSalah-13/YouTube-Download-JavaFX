package com.hamza.youtubedownload.other;

import com.hamza.youtubedownload.utils.Choose;
import com.hamza.youtubedownload.utils.TestCommands;
import com.hamza.youtubedownload.utils.TextName;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.io.File;

@Log4j2
public class YoutubeVideoInfoFetcher {
    private static final String YT_DLP_BASE_COMMAND = "yt-dlp";
    private static final String FILE_INFO_TEMPLATE =
            "yt-dlp -P \"%s\" %s --write-info-json " +
                    "-O \"%%(.{filesize,filesize_approx,id,title,url,thumbnails})#j\" %s > fileData.json";
    private static final String SIZE_FIELDS = "filesize,filesize_approx";

    private TestCommands commandExecutor;
    private File dataDirectory;

    public YoutubeVideoInfoFetcher(TestCommands testCommands, File directory) {
        this.commandExecutor = testCommands;
        this.dataDirectory = directory;
    }

    public VideoInfo fetchVideoInfo(String videoUrl) throws VideoInfoFetchException {
        try {
            String command = buildInfoCommand(videoUrl);
            commandExecutor.processSetting(command);
            return parseVideoInfo();
        } catch (Exception e) {
            log.error("Failed to fetch video info: {}", e.getMessage(), e);
            throw new VideoInfoFetchException("Could not fetch video information", e);
        }
    }

    private String buildInfoCommand(String videoUrl) {
        return String.format(FILE_INFO_TEMPLATE,
                Choose.getPlacedSave(),
                TextName.SKIP_DOWNLOAD,
                videoUrl);
    }

    private VideoInfo parseVideoInfo() {
        JSONObject jsonObject = new JSONObject();
        long fileSize = getFileSize(jsonObject);
        return VideoInfo.builder()
                .fileSize(fileSize)
                .title(jsonObject.getString("title"))
                .id(jsonObject.getString("id"))
                .build();
    }

    private long getFileSize(JSONObject jsonObject) {
        // Try exact filesize first, fall back to approximate if not available
        if (jsonObject.has("filesize")) {
            return jsonObject.getLong("filesize");
        } else if (jsonObject.has("filesize_approx")) {
            return Math.round(jsonObject.getDouble("filesize_approx"));
        }
        return -1L; // Indicate size not available
    }

    @Data
    @Builder
    public static class VideoInfo {
        private final String id;
        private final String title;
        private final long fileSize;

        public String getFormattedSize() {
            if (fileSize < 0) {
                return "Unknown size";
            }
            return formatSize(fileSize);
        }

        private String formatSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            String pre = "KMGTPE".charAt(exp - 1) + "";
            return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
        }
    }
}