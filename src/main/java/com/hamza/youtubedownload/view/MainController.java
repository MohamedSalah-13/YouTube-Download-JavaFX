package com.hamza.youtubedownload.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    @FXML
    private TextField urlField;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private Button downloadBtn;

    // دالة لتحليل الرابط وجلب الجودات
    @FXML
    private void analyzeUrl() {
        String url = urlField.getText();
        if (url.isEmpty()) return;

        Task<List<String>> fetchTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                updateMessage("جاري فحص الرابط...");
                List<String> formats = new ArrayList<>();
                // أمر yt-dlp لجلب الجودات بصيغة نصية
                ProcessBuilder pb = new ProcessBuilder("yt-dlp", "-F", url);
                Process p = pb.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = r.readLine()) != null) {
                    if (line.contains("p") && (line.contains("https") || line.contains("http"))) {
                        formats.add(line); // تخزين أسطر الجودات
                    }
                }
                return formats;
            }
        };

        fetchTask.setOnSucceeded(e -> {
            formatComboBox.getItems().setAll(fetchTask.getValue());
            downloadBtn.setDisable(false);
            statusLabel.setText("تم العثور على الجودات المتاحة.");
        });

        new Thread(fetchTask).start();
    }

    // دالة بدء التحميل
    @FXML
    private void startDownload() {
        String url = urlField.getText();
        String selectedFormat = formatComboBox.getSelectionModel().getSelectedItem();
        if (selectedFormat == null) return;

        // استخراج ID الجودة (أول كلمة في السطر المختار)
        String formatId = selectedFormat.split("\\s+")[0];

        Task<Void> downloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // داخل دالة startDownload في الـ Task
                String outputTemplate = selectedDirectory.getAbsolutePath() + "/%(title)s.%(ext)s";
                // تشغيل yt-dlp مع تحديد الجودة والمسار
                ProcessBuilder pb = new ProcessBuilder(
                        "yt-dlp",
                        "--user-agent", "Mozilla/5.0...",
                        "--referer", "https://larozavideo.net/",
                        "-f", formatId,
                        "-o", outputTemplate, // هنا نمرر المسار المختار
                        "--newline",
                        url
                );
                pb.redirectErrorStream(true); // لدمج الأخطاء مع المخرجات العادية
                Process p = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    String line;
                    // Regex للبحث عن نمط النسبة المئوية (مثل 10.5%)
                    Pattern pattern = Pattern.compile("(\\d+\\.\\d+)%");

                    while ((line = reader.readLine()) != null) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            // تحويل النص المستخرج (مثلاً 10.5) إلى رقم عشري
                            double progress = Double.parseDouble(matcher.group(1));

                            // تحديث شريط التقدم (القيم من 0.0 إلى 1.0)
                            updateProgress(progress, 100.0);
                            updateMessage("جاري التحميل: " + progress + "%");
                        }
                        // طباعة الأسطر في الكونسول للتأكد من سير العملية
                        System.out.println(line);
                    }
                }
                p.waitFor();
                return null;
            }
        };

        // ربط الـ Task بعناصر الواجهة
        progressBar.progressProperty().bind(downloadTask.progressProperty());
        statusLabel.textProperty().bind(downloadTask.messageProperty());

        downloadTask.setOnSucceeded(e -> {
            statusLabel.textProperty().unbind();
            statusLabel.setText("تم التحميل بنجاح!");
        });

        new Thread(downloadTask).start();
    }

    private File selectedDirectory;

    @FXML
    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("اختر مجلد الحفظ");

        // تحديد مجلد افتراضي (مثلاً مجلد المستخدم)
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // فتح النافذة
        Stage stage = (Stage) urlField.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if (file != null) {
            selectedDirectory = file;
            statusLabel.setText("مكان الحفظ: " + file.getAbsolutePath());
        }
    }
}
