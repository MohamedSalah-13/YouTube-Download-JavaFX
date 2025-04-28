package com.hamza.youtubedownload.utils;

import com.hamza.youtubedownload.other.Data_Setting;
import com.hamza.youtubedownload.other.WindowsFile;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

import static com.hamza.youtubedownload.controller.DownloadController.APP_DIR_NAME;

@Log4j2
public class TestCommands {

    private final String system;
    private final String compile;
    @Setter
    private File directory;
    private TextArea logTextArea;

    public TestCommands() {
        Data_Setting dataSetting = new WindowsFile();
        system = dataSetting.system();
        compile = dataSetting.compile();
    }

    public TestCommands(TextArea logTextArea) {
        this();
        this.logTextArea = logTextArea;
    }

    private void clearTextArea() {
        if (logTextArea != null) {
            Platform.runLater(() -> {
                logTextArea.clear();
            });
        }
    }

    public void processSetting(String command) {
        try {
            clearTextArea();

            ProcessBuilder processBuilder = new ProcessBuilder();
            System.out.println(command);

            if (logTextArea != null) {
                Platform.runLater(() -> {
                    logTextArea.appendText("Command: " + command + "\n");
                    logTextArea.appendText("ProcessBuilder: " + processBuilder.toString() + "\n");
                });
            }

            File workingDir;
            if (directory!=null && directory.exists() && directory.isDirectory()) {
                // path of file yt-dlp
                workingDir = directory;
                processBuilder.directory(workingDir);
            } else {
                File recordsDir = new File(Choose.USER_HOME, APP_DIR_NAME);
                if (!recordsDir.exists()) {
                    recordsDir.mkdirs();
                }
                workingDir = recordsDir;
                processBuilder.directory(workingDir);
            }

            processBuilder.command(system, compile, command);

            if (logTextArea != null) {
                final String workingDirPath = workingDir.getAbsolutePath();
                Platform.runLater(() -> {
                    logTextArea.appendText("Working Directory: " + workingDirPath + "\n");
                    logTextArea.appendText("Command: [" + system + ", " + compile + ", " + command + "]\n");
                });
            }
            if (logTextArea != null) {
                Platform.runLater(() -> {
                    logTextArea.appendText("Starting process...\n");
                });
            }

            Process process = processBuilder.start();

            // Create a thread to read the process output
            Thread outputThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("line :- " + line);
                        final String outputLine = line;
                        if (logTextArea != null) {
                            Platform.runLater(() -> {
                                logTextArea.appendText("Output: " + outputLine + "\n");
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            outputThread.start();

            // Create a thread to read the process error
            Thread errorThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("error :- " + line);
                        final String errorLine = line;
                        if (logTextArea != null) {
                            Platform.runLater(() -> {
                                logTextArea.appendText("Error: " + errorLine + "\n");
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            errorThread.start();

            int exitVal = process.waitFor();

            if (logTextArea != null) {
                Platform.runLater(() -> {
                    logTextArea.appendText("Process completed with exit code: " + exitVal + "\n");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getClass(), e.getCause());
        }
    }

}
