package com.hamza.youtubedownload.utils;

import com.hamza.youtubedownload.other.Data_Setting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestCommands {

    private final String system;
    private final String c;

    public TestCommands(Data_Setting dataSetting) {
        system = dataSetting.system();
        c = dataSetting.compile();
    }

    public void execute(String command) throws InterruptedException, IOException {
        ProcessBuilder builder = new ProcessBuilder(system, c, command);
        Process process = builder.inheritIO().start();
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String readline;
        while ((readline = reader.readLine()) != null) {
            System.out.println(readline);
        }
    }

    public void processSetting(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(system, c, command);
            Process process = processBuilder.inheritIO().start();
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("line :- " + line);
//                    listView.getItems().addAll(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read the file to check whether there is a subtitle file
    public String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void other() {
//        try {
//            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start"});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        execute("touch " + text);
//        execute("chmod a+wrx " + text);
//        execute(youtubeApp + "--list-subs " + url);
//        execute("java -version");
    }
}
