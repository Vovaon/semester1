package practice04.homework;
import java.util.Locale;
import java.util.Scanner;
public class Task3FibonacciSeries {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        System.out.println("=== ЧИСЛА ФІБОНАЧЧІ ТА ЗОЛОТИЙ ПЕРЕТИН ===");
        int n;
        do {
            System.out.print("Скільки чисел згенерувати (N >= 3): ");
            n = scanner.nextInt();
            if (n < 3 || n > 30) {
                System.out.println("Помилка: N має бути від 3 до 30!");
            }
        } while (n < 3 || n > 30);
 
        System.out.println();
 
        int f1 = 1;
        int f2 = 1;
        double lastRatio = 0.0;
 
        System.out.printf("№ %-3d| Число: %d%n", 1, f1);
        System.out.printf("№ %-3d| Число: %d%n", 2, f2);
 
        for (int i = 3; i <= n; i++) {
            int next = f1 + f2;
            double ratio = (double) next / f2;
            lastRatio = ratio;
 
            System.out.printf(Locale.US,
                    "№ %-3d| Число: %-11d| Відношення F(n)/F(n-1) = %.6f%n",
                    i, next, ratio);
            f1 = f2;
            f2 = next;
        }
 
        final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;
 
        System.out.println();
        System.out.printf(Locale.US, "Підсумок: знайдено наближення Золотого перетину: %.6f%n", lastRatio);
        System.out.printf(Locale.US, "Еталонне значення:                             %.6f%n", GOLDEN_RATIO);
        System.out.println("=========================================");
 
        scanner.close();
    }

}
