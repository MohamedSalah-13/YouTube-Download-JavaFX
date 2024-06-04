package com.hamza.youtubedownload.utils;

import com.hamza.youtubedownload.other.Data_Setting;
import com.hamza.youtubedownload.other.WindowsFile;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.*;

@Log4j2
public class TestCommands {

    private final String system;
    private final String compile;
    @Setter
    private File directory;

    public TestCommands() {
        Data_Setting dataSetting = new WindowsFile();
        system = dataSetting.system();
        compile = dataSetting.compile();
    }

    public void processSetting(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (directory.isDirectory()) {
                // path of file yt-dlp
                processBuilder.directory(directory);
            }

            processBuilder.command(system, compile, command);
            Process process = processBuilder.inheritIO().start();
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("line :- " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }
    }

    // Read the file to check whether there is a subtitle file

    public String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    public void execute(String command) throws InterruptedException, IOException {
        ProcessBuilder builder = new ProcessBuilder(system, compile, command);
        Process process = builder.inheritIO().start();
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String readline;
        while ((readline = reader.readLine()) != null) {
            System.out.println(readline);
        }
    }
}
