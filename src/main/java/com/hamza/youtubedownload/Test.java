package com.hamza.youtubedownload;

import com.hamza.youtubedownload.utils.TestCommands;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hamza.youtubedownload.DownloadController.*;

public class Test {



    private FileWriter myWriter;

    public Test() {
        try {
            myWriter = new FileWriter("emails.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String s = "yt-dlp --flat-playlist -i --print-to-file \"%(url)s # %(title)s\" file.txt https://www.youtube.com/playlist?list=PLSvCAHoiHC_qKVkVsiupTs9fCQBtNyeGz";
//        String s = "yt-dlp " + FLAT_PLAYLIST + " -i --print-to-file \"%(title)s by %(uploader)s on %(upload_date)s in %(playlist)s.%(ext)s\" file.txt https://www.youtube.com/playlist?list=PLSvCAHoiHC_qKVkVsiupTs9fCQBtNyeGz";
        String s = "yt-dlp -i " + SKIP_DOWNLOAD + WRITE_THUMBNAIL + PRINT_TO_FILE + URL_S_TITLE_S + " data/file.txt https://www.youtube.com/watch?v=DniTlpZ__z8";
        TestCommands testCommands = new TestCommands();
        testCommands.setDirectory(new File(""));
        testCommands.processSetting(s);
    }

    private static void extracted() {
        //  String path = System.getProperty("user.home");
        FileSystemView view = FileSystemView.getFileSystemView();
        File file = view.getHomeDirectory();
        String desktopPath = file.getPath();
        System.out.println(desktopPath);
        System.out.println(view.getDefaultDirectory());
        System.out.println("-------------------------");
        // "yt-dlp --skip-download  -f best -g https://www.youtube.com/watch?v=e8wEcemxcfI&list=PLZV0a2jwt22vMQXKQh-h1vS-Z9XPji0p4&index=1"
        String url = "https://www.youtube.com/watch?v=e8wEcemxcfI&list=PLZV0a2jwt22vMQXKQh-h1vS-Z9XPji0p4&index=1";
        try {
            URLConnection link = new URL(url).openConnection();
            System.out.println(Arrays.toString(link.getURL().openStream().readAllBytes()));
            System.out.println(link.getURL().getRef());
            System.out.println(link.getURL().getContent());
            System.out.println(link.getURL().getDefaultPort());
            System.out.println(link.getURL().getProtocol());
            System.out.println(link.getURL().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String youtubeId = getYoutubeId(url);
        System.out.println(youtubeId);
        System.out.println("https://i.ytimg.com/vi/" + youtubeId + "/mqdefault.jpg");
    }

    public static String getYoutubeId(String url) {
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static void cloneGitProjects() {
        try {

            String path = System.getProperty("user.dir");
            Test test = new Test();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/links.txt")))) {
                String line;
                while ((line = br.readLine()) != null) {

                    if (test.urlValidator(line)) {
                        //  clone file
                        test.processSetting(new File(path), "git clone " + line);

                        String[] strings = line.split("/");
                        String nameFile = strings[4];
                        String name = nameFile + "_log.txt";

                        // get log
                        test.processSetting(new File(path + "/" + nameFile), "git log > ../" + name);

                        // write file
                        test.myWriter.write(" \n # --------- " + line + " - " + nameFile + " ---------- \n");
                        test.readFromInputStream(new FileInputStream(name));
                    } else {
                        System.out.println("No Valid Url : " + line);
                    }
                }

                test.myWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processSetting(File directory, String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(directory);
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.inheritIO().start();
        process.waitFor();
    }

    public void readFromInputStream(InputStream inputStream) throws IOException {
        Set<String> lines = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Author: ")) {
                    if (!lines.contains(line)) {
                        lines.add(line);
                        myWriter.write(line + "\n");
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public boolean urlValidator(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException exception) {
            return false;
        }
    }
}