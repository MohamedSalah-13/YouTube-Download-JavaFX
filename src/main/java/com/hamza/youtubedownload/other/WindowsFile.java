package com.hamza.youtubedownload.other;

public class WindowsFile implements Data_Setting{

    @Override
    public String system() {
        return "cmd.exe";
    }

    @Override
    public String compile() {
        return "/c";
    }

    @Override
    public String time() {
        return "%TIME";
    }
}
