package practice05.homework;
import java.util.Locale;
import java.util.Scanner;
public class Task1DailySales {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
 
        String[] days = {"Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя"};
        double[] sales = new double[days.length];
 
        System.out.println("=== ФІНАНСОВИЙ ЗВІТ КАВ'ЯРНІ ЗА ТИЖДЕНЬ ===");
 
    
        for (int i = 0; i < sales.length; i++) {
            do {
                System.out.print("Введіть виторг за " + days[i] + " (грн): ");
                sales[i] = scanner.nextDouble();
                if (sales[i] < 0) {
                    System.out.println("Помилка: виторг не може бути від'ємним!");
                }
            } while (sales[i] < 0);
        }
 
        
        double total = 0;
        int bestIndex = 0; 
        int worstIndex = 0;
        for (int i = 0; i < sales.length; i++) {
            total += sales[i];
            if (sales[i] > sales[bestIndex]) {
                bestIndex = i;
            }
            if (sales[i] < sales[worstIndex]) {
                worstIndex = i;
            }
        }
        double average = total / sales.length;
 
        System.out.println();
        System.out.println("---------------- ПІДСУМКИ ----------------");
        System.out.printf(Locale.US, "%-28s%.2f грн%n", "Загальний тижневий виторг:", total);
        System.out.printf(Locale.US, "%-28s%.2f грн%n", "Середній виторг на день:", average);
        System.out.printf(Locale.US, "%-28s%s (%.2f грн)%n", "Найкращий день:", days[bestIndex], sales[bestIndex]);
        System.out.printf(Locale.US, "%-28s%s (%.2f грн)%n", "Найгірший день:", days[worstIndex], sales[worstIndex]);
 
        System.out.println();
        System.out.println("Дні з виторгом вище середнього:");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i] > average) {
                System.out.printf(Locale.US, "- %-11s%.2f грн%n", days[i] + ":", sales[i]);
            }
        }
        System.out.println("==========================================");
 
        scanner.close();
    }

}
