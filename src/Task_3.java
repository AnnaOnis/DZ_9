import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Task_3 {

    private static final AtomicInteger filesCopied = new AtomicInteger(0);
    private static final AtomicInteger directoriesCopied = new AtomicInteger(0);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к существующей директории:");
        String sourcePath = scanner.nextLine();

        System.out.println("Введите путь к новой директории:");
        String destinationPath = scanner.nextLine();

        File sourceDir = new File(sourcePath);
        File destinationDir = new File(destinationPath);

        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.out.println("Указанная исходная директория не существует или не является директорией.");
            return;
        }

        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        copyDirectory(sourceDir, destinationDir);

        System.out.println("Копирование завершено:");
        System.out.println("Всего скопировано директорий: " + directoriesCopied.get());
        System.out.println("Всего скопировано файлов: " + filesCopied.get());

        scanner.close();
    }

    private static void copyDirectory(File sourceDir, File destinationDir) {
        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File dest = new File(destinationDir, file.getName());
                if (file.isDirectory()) {
                    dest.mkdirs();
                    directoriesCopied.incrementAndGet();
                    copyDirectory(file, dest);
                } else {
                    try {
                        Files.copy(file.toPath(), dest.toPath());
                        filesCopied.incrementAndGet();
                    } catch (IOException e) {
                        System.out.println("Ошибка при копировании файла: " + e.getMessage());
                    }
                }
            }
        }
    }
}


