package com.hamza.youtubedownload.utils;

import javafx.concurrent.Task;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadTask extends Task<Void> {

    private final String url;

    public DownloadTask(String url) {
        this.url = url;
    }


    @SuppressWarnings({"deprecation"})
    @Override
    protected Void call() throws Exception {
        String[] strings = url.split("/");
        String ext = strings[strings.length - 1];

//            String ext = url.substring(url.lastIndexOf("/"));
        System.out.println(ext);
        URLConnection link = new URL(url).openConnection();
        long fileLength = link.getContentLengthLong();
        double d = fileLength * (1 * Math.pow(10, -9));

        System.out.println(fileLength + "  " + d);
        System.out.println(new URL(url).getFile());
        try (InputStream inputStream = link.getInputStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             OutputStream os = Files.newOutputStream(Paths.get("Expression.mp4"))) {
            long nread = 0L;
            byte[] bytes = new byte[4 * 1024];
            int n;
            while ((n = bufferedInputStream.read(bytes, 0, bytes.length)) != -1) {
                os.write(bytes, 0, n);
                nread += n;
                updateProgress(nread, fileLength);
                double v = ((double) nread / fileLength) * 100;
                updateMessage(String.valueOf(v).concat("%"));
            }

        }
        return null;
    }

    @Override
    protected void succeeded() {
        System.out.println("downloaded");
    }

    @Override
    protected void failed() {
        System.out.println("failed");
    }
}
