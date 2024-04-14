import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Task_4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод пути к директории и слова для поиска
        System.out.println("Введите путь к директории:");
        String directoryPath = scanner.nextLine();

        System.out.println("Введите слово для поиска:");
        String searchWord = scanner.nextLine();

        // Создание и запуск потоков
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Первый поток для поиска и объединения содержимого файлов
        Future<List<String>> futureSearch = executor.submit(() -> {
            List<String> contentList = new ArrayList<>();
            AtomicInteger fileCount = new AtomicInteger();
            try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
                paths
                        .filter(Files::isRegularFile)
                        .filter(path -> {
                            try {
                                boolean containsWord = Files.lines(path).anyMatch(line -> line.contains(searchWord));
                                if (containsWord) {
                                    fileCount.incrementAndGet();
                                }
                                return containsWord;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .forEach(path -> {
                            try {
                                contentList.addAll(Files.readAllLines(path));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Найдено файлов с искомым словом: " + fileCount.get());
            return contentList;
        });

        // Второй поток для обработки найденного содержимого
        Future<Void> futureProcess = executor.submit(() -> {
            try {
                List<String> forbiddenWords = Files.readAllLines(Paths.get("forbidden_words.txt"));
                List<String> contentList = futureSearch.get();
                AtomicInteger forbiddenCount = new AtomicInteger();


                for (String word : forbiddenWords) {
                    int countBefore = contentList.size();
                    contentList.removeIf(line -> line.contains(word));
                    int countAfter = contentList.size();
                    forbiddenCount.addAndGet(countBefore - countAfter);
                }

                // Запись обработанного содержимого в файл
                Files.write(Paths.get("processed_content.txt"), contentList);

                System.out.println("Удалено запрещенных слов: " + forbiddenCount.get());
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });

        // Ожидание завершения работы обоих потоков
        try {
            futureSearch.get();
            futureProcess.get();
            System.out.println("Операции завершены успешно.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Остановка пула потоков
        executor.shutdown();
    }
}
