package practice04.homework;
import java.util.Locale;
import java.util.Scanner;
public class Task1RangeStatistics {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        System.out.println("=== АНАЛІЗАТОР ДІАПАЗОНУ ЧИСЕЛ ===");
 
        int n;
        do {
            System.out.print("Введіть число N: ");
            n = scanner.nextInt();
            if (n <= 0) {
                System.out.println("Помилка: N має бути більшим за 0!");
            }
        } while (n <= 0);
 
        long totalSum = 0;
        long evenSum = 0;
        long oddSum = 0;
        int evenCount = 0;
        int oddCount = 0;
 
        for (int i = 1; i <= n; i++) {
            totalSum += i;
            if (i % 2 == 0) {
                evenSum += i;
                evenCount++;
            } else {
                oddSum += i;
                oddCount++;
            }
        }
 
        double average = (double) totalSum / n;
        System.out.println();
        System.out.println("--- РЕЗУЛЬТАТИ ДЛЯ ДІАПАЗОНУ [1 .. " + n + "] ---");
        System.out.printf(Locale.US, "%-27s%d%n", "Загальна сума чисел:", totalSum);
        System.out.printf(Locale.US, "%-27s%d (кількість: %d)%n", "Сума парних чисел:", evenSum, evenCount);
        System.out.printf(Locale.US, "%-27s%d (кількість: %d)%n", "Сума непарних чисел:", oddSum, oddCount);
        System.out.printf(Locale.US, "%-27s%.2f%n", "Середнє арифметичне:", average);
        System.out.println("=========================================");
 
        scanner.close();
    }
}

