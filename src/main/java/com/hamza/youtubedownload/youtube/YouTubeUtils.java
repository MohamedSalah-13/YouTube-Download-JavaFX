package com.hamza.youtubedownload.youtube;

import javafx.concurrent.Task;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for YouTube operations like extracting video URLs and downloading videos.
 *
 * <p>This class provides functionality to:</p>
 * <ul>
 *   <li>Extract video IDs from YouTube URLs</li>
 *   <li>Download videos from YouTube</li>
 *   <li>Execute youtube-dl commands if the tool is installed</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 * // Extract video ID from a YouTube URL
 * String youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
 * Optional&lt;String&gt; videoIdOpt = YouTubeUtilsApp.extractVideoId(youtubeUrl);
 *
 * if (videoIdOpt.isPresent()) {
 *     String videoId = videoIdOpt.get();
 *
 *     // Download the video
 *     String outputDirectory = "C:\\Downloads";
 *     String filename = "my_video";
 *
 *     Task&lt;File&gt; downloadTask = YouTubeUtilsApp.downloadVideo(videoId, outputDirectory, filename);
 *
 *     // Handle download completion
 *     downloadTask.setOnSucceeded(event -> {
 *         File downloadedFile = downloadTask.getValue();
 *         System.out.println("Download completed: " + downloadedFile.getAbsolutePath());
 *     });
 *
 *     // Handle download failure
 *     downloadTask.setOnFailed(event -> {
 *         Throwable exception = downloadTask.getException();
 *         System.err.println("Download failed: " + exception.getMessage());
 *     });
 * }
 * </pre>
 *
 * <p>Note: Downloading videos from YouTube may be subject to YouTube's terms of service.
 * Make sure to use this functionality in compliance with those terms.</p>
 */
@Log4j2
public class YouTubeUtils {

    // Regular expression to extract YouTube video ID from various YouTube URL formats
    private static final String YOUTUBE_VIDEO_ID_REGEX =
            "(?:youtube\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/)([^\"&?/ ]{11})";
    private static final Pattern YOUTUBE_VIDEO_ID_PATTERN = Pattern.compile(YOUTUBE_VIDEO_ID_REGEX);

    /**
     * Extracts the YouTube video ID from a given URL.
     *
     * @param youtubeUrl the YouTube URL to extract the video ID from
     * @return an Optional containing the video ID if found, or empty if not found
     */
    public static Optional<String> extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) {
            return Optional.empty();
        }

        Matcher matcher = YOUTUBE_VIDEO_ID_PATTERN.matcher(youtubeUrl);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    /**
     * Constructs a direct video URL for a YouTube video.
     * Note: This is a simplified approach and may not work for all videos due to YouTube's policies.
     * For a more robust solution, consider using a library like youtube-dl.
     *
     * @param videoId the YouTube video ID
     * @return the direct video URL
     */
    public static String getDirectVideoUrl(String videoId) {
        // This is a simplified approach - in a real implementation, you would use youtube-dl or YouTube API
        // to get the actual video URL, which requires handling authentication, formats, etc.
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    /**
     * Downloads a video from YouTube using the video ID.
     * This method creates a background task to download the video.
     *
     * @param videoId the YouTube video ID
     * @param outputDirectory the directory to save the downloaded video
     * @param filename the name of the downloaded file (without extension)
     * @return a Task that can be used to track the download progress
     */
    public static Task<File> downloadVideo(String videoId, String outputDirectory, String filename) {
        Task<File> task = new Task<>() {
            @Override
            protected File call() throws Exception {
                // Create output directory if it doesn't exist
                File directory = new File(outputDirectory);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // In a real implementation, you would use youtube-dl or a similar tool here
                // This is a simplified example that won't actually download the video
                String videoUrl = getDirectVideoUrl(videoId);
                File outputFile = new File(directory, filename + ".mp4");

                try {
                    // This is a placeholder for the actual download logic
                    // In a real implementation, you would use youtube-dl or a similar tool
                    downloadFromUrl(videoUrl, outputFile);
                    return outputFile;
                } catch (IOException e) {
                    log.error("Error downloading video: " + e.getMessage(), e);
                    throw e;
                }
            }
        };

        // Start the download task in a new thread
        new Thread(task).start();
        return task;
    }

    /**
     * Downloads a file from a URL to a local file.
     * This is a simplified implementation and may not work for YouTube videos directly.
     *
     * @param fileUrl the URL of the file to download
     * @param outputFile the local file to save the downloaded content
     * @throws IOException if an I/O error occurs
     */
    public static void downloadFromUrl(String fileUrl, File outputFile) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Creates a simple command to download a YouTube video using youtube-dl.
     * Note: This requires youtube-dl to be installed on the system.
     *
     * @param videoId the YouTube video ID
     * @param outputDirectory the directory to save the downloaded video
     * @param filename the name of the downloaded file (without extension)
     * @return the command to execute
     */
    public static String createYoutubeDlCommand(String videoId, String outputDirectory, String filename) {
        return "youtube-dl -f best -o \"" + outputDirectory + File.separator + filename + ".%(ext)s\" https://www.youtube.com/watch?v=" + videoId;
    }

    /**
     * Executes a command in the system's command line.
     *
     * @param command the command to execute
     * @return true if the command executed successfully, false otherwise
     */
    public static boolean executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            log.error("Error executing command: " + e.getMessage(), e);
            return false;
        }
    }
}
