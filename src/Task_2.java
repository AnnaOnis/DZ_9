import java.io.*;
import java.util.Random;
public class Task_2 {
    public static void main(String[] args) {

        String inputFilename = "numbers.txt";
        String primeOutputFilename = "prime_numbers.txt";
        String factorialOutputFilename = "factorials.txt";
        int numbersCount = 15; // Количество случайных чисел

        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(inputFilename, numbersCount);
        PrimeNumberFinder primeNumberFinder = new PrimeNumberFinder(inputFilename, primeOutputFilename);
        FactorialCalculator factorialCalculator = new FactorialCalculator(inputFilename, factorialOutputFilename);

        randomNumberGenerator.start();

        try {
            randomNumberGenerator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        primeNumberFinder.start();
        factorialCalculator.start();

        try {
            primeNumberFinder.join();
            factorialCalculator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все операции выполнены успешно!");
    }
}

class RandomNumberGenerator extends Thread {
    private final String filename;
    private final int numbersCount;

    public RandomNumberGenerator(String filename, int numbersCount) {
        this.filename = filename;
        this.numbersCount = numbersCount;
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Random random = new Random();
            for (int i = 0; i < numbersCount; i++) {
                int randomNumber = random.nextInt(20); // Генерация случайного числа от 0 до 999
                writer.println(randomNumber);
                Thread.sleep(100); // Пауза для имитации заполнения файла
            }
            System.out.println("Файл " + filename + " был заполнен случайными числами.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PrimeNumberFinder extends Thread {
    private final String inputFilename;
    private final String outputFilename;

    public PrimeNumberFinder(String inputFilename, String outputFilename) {
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int number = Integer.parseInt(line);
                if (isPrime(number)) {
                    writer.println(number);
                }
            }
            System.out.println("Все простые числа записаны в файл " + outputFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}

class FactorialCalculator extends Thread {
    private final String inputFilename;
    private final String outputFilename;

    public FactorialCalculator(String inputFilename, String outputFilename) {
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int number = Integer.parseInt(line);
                long factorial = calculateFactorial(number);
                writer.println("Факториал числа " + number + " : " + factorial);
            }
            System.out.println("Факториалы чисел были записаны в файл " + outputFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long calculateFactorial(int n) {
        if (n == 0) return 1;
        long factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
}