import java.util.Random;

// При старте приложения запускаются три потока.
// Первый поток заполняет массив случайными числами.
//Два других потока ожидают заполнения. Когда массив
//заполнен оба потока запускаются. Первый поток находит сумму
//элементов массива, второй поток среднеарифметическое значение в массиве.
// Полученный массив, сумма и среднеарифметическое возвращаются в метод main,
// где должны быть отображены.

public class Task_1 {
    public static void main(String[] args) throws InterruptedException {
        int[] array = new int[15];
        Random random = new Random();

        // Создаем первый поток для заполнения массива
        Thread fillThread = new Thread(() -> {
            for (int i = 0; i < array.length; i++) {
                array[i] = random.nextInt(100); // Генерируем случайное число от 0 до 99
            }
            System.out.println("Массив заполнен.");
            synchronized (array) {
                array.notifyAll(); // Оповещаем другие потоки о том, что массив заполнен
            }
        });


        // Создаем второй поток для вычисления суммы элементов массива
        Thread sumThread = new Thread(() -> {
            synchronized (array) {
                try {
                    array.wait(); // Ожидаем, пока массив не будет заполнен
                    int sum = 0;
                    for (int num : array) {
                        sum += num;
                    }
                    System.out.println("Сумма элементов массива: " + sum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        // Создаем третий поток для вычисления среднего арифметического
        Thread avgThread = new Thread(() -> {
            synchronized (array) {
                try {
                    array.wait(); // Ожидаем, пока массив не будет заполнен
                    int sum = 0;
                    for (int num : array) {
                        sum += num;
                    }
                    double average = (double) sum / array.length;
                    System.out.println("Среднее арифметическое элементов массива: " + average);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        fillThread.start();
        sumThread.start();
        avgThread.start();

        fillThread.join();
        sumThread.join();
        avgThread.join();

    }
}

