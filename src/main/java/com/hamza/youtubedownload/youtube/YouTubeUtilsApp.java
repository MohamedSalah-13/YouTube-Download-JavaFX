package com.hamza.youtubedownload.youtube;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import static com.hamza.youtubedownload.youtube.YouTubeUtils.downloadFromUrl;
import static com.hamza.youtubedownload.youtube.YouTubeUtils.extractVideoId;

@Log4j2
public class YouTubeUtilsApp {

    /**
     * Gets the actual video URL using youtube-dl
     * @param videoId the YouTube video ID
     * @return Optional containing the direct video URL if successful, empty otherwise
     */
    public static Optional<String> getActualVideoUrl(String videoId) {
        try {
            // Create process builder for youtube-dl command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "youtube-dl",
                    "--get-url",
                    "--format", "best",
                    "https://www.youtube.com/watch?v=" + videoId
            );

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output (URL)
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String url = reader.readLine();

                // Wait for the process to complete
                int exitCode = process.waitFor();

                if (exitCode == 0 && url != null && !url.isEmpty()) {
                    return Optional.of(url);
                }
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error getting video URL: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    /**
     * Example usage of getting and downloading a video
     * @param youtubeUrl the YouTube video URL
     * @param outputDirectory the directory to save the video
     * @param filename the desired filename (without extension)
     * @return Optional<File> containing the downloaded file if successful
     */
    public static Optional<File> downloadYoutubeVideo(String youtubeUrl,
                                                      String outputDirectory, String filename) {
        // First extract the video ID
        Optional<String> videoId = extractVideoId(youtubeUrl);

        if (videoId.isPresent()) {
            // Get the actual video URL
            Optional<String> actualUrl = getActualVideoUrl(videoId.get());

            if (actualUrl.isPresent()) {
                try {
                    File outputFile = new File(outputDirectory, filename + ".mp4");
                    downloadFromUrl(actualUrl.get(), outputFile);
                    return Optional.of(outputFile);
                } catch (IOException e) {
                    log.error("Error downloading video: " + e.getMessage(), e);
                }
            }
        }

        return Optional.empty();
    }
}
