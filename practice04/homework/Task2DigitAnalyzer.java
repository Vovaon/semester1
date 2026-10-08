package practice04.homework;

import java.util.Scanner;
public class Task2DigitAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        System.out.println("=== АНАЛІЗАТОР РОЗРЯДІВ ЧИСЛА ===");
        int number;
        do {
            System.out.print("Введіть додатне ціле число: ");
            number = scanner.nextInt();
            if (number <= 0) {
                System.out.println("Помилка: число має бути більшим за 0!");
            }
        } while (number <= 0);
        int originalNumber = number;
        int temp = number;
 
        int digitCount = 0;
        int digitSum = 0;
        int maxDigit = 0;
        long reversedNumber = 0;
 
        while (temp > 0) {
            int digit = temp % 10;
            digitCount++;
            digitSum += digit;
            if (digit > maxDigit) {
                maxDigit = digit;
            }
            reversedNumber = reversedNumber * 10 + digit;
            temp /= 10;
 
        boolean isPalindrome = originalNumber == reversedNumber;
 
        System.out.println();
        System.out.println("--- СТАТИСТИКА ЧИСЛА ---");
        System.out.printf("%-21s%d%n", "Початкове число:", originalNumber);
        System.out.printf("%-21s%d%n", "Кількість цифр:", digitCount);
        System.out.printf("%-21s%d%n", "Сума цифр:", digitSum);
        System.out.printf("%-21s%d%n", "Найбільша цифра:", maxDigit);
        System.out.printf("%-21s%d%n", "Перевернуте число:", reversedNumber);
 
        if (isPalindrome) {
            System.out.printf("%-21s%s%n", "Результат:", "✨ Це число є ПАЛІНДРОМОМ!");
        } else {
            System.out.printf("%-21s%s%n", "Результат:", "Це звичайне число (не паліндром).");
        }
        System.out.println("================================");
 
        scanner.close();
    }

    
}
}