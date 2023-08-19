package com.hamza.youtubedownload.other;

public class LinuxFile implements Data_Setting{

    @Override
    public String system() {
        return "bash";
    }

    @Override
    public String compile() {
        return "-c";
    }

    @Override
    public String time() {
        return "date";
    }
}
