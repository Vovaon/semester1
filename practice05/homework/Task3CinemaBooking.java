package practice05.homework;
import java.util.Locale;
import java.util.Scanner;
public class Task3CinemaBooking {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        final int ROWS = 5;
        final int COLS = 6;
        final double TICKET_PRICE = 180.0;
 
        char[][] hall = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                hall[r][c] = 'O';
            }
        }
 
        boolean running = true;
        while (running) {
            System.out.println("=== ТЕРМІНАЛ КІНОТЕАТРУ: ЗАЛ №1 ===");
            System.out.println("1 - Показати схему залу");
            System.out.println("2 - Забронювати квиток");
            System.out.println("3 - Фінансова каса залу");
            System.out.println("0 - Завершити роботу");
            System.out.print("Ваш вибір: ");
            int choice = scanner.nextInt();
 
            switch (choice) {
                case 1 -> {
                    System.out.println();
                    System.out.print("      ");
                    for (int c = 0; c < COLS; c++) {
                        System.out.print((c + 1) + "  ");
                    }
                    System.out.println();
                    for (int r = 0; r < ROWS; r++) {
                        System.out.print("Р" + (r + 1) + ":  ");
                        for (int c = 0; c < COLS; c++) {
                            System.out.print("[" + hall[r][c] + "]");
                        }
                        System.out.println();
                    }
                }
                case 2 -> {
                    System.out.print("Введіть номер ряду (1-" + ROWS + "): ");
                    int row = scanner.nextInt();
                    System.out.print("Введіть номер місця (1-" + COLS + "): ");
                    int seat = scanner.nextInt();
 
                    if (row < 1 || row > ROWS || seat < 1 || seat > COLS) {
                        System.out.println(" Помилка: такого місця немає! Ряд 1-" + ROWS + ", місце 1-" + COLS + ".");
                    } else if (hall[row - 1][seat - 1] == 'X') {
                        System.out.println(" Місце [Ряд " + row + ", Місце " + seat + "] уже куплене!");
                    } else {
                        hall[row - 1][seat - 1] = 'X';
                        System.out.println(" Місце [Ряд " + row + ", Місце " + seat + "] успішно заброньовано!");
                    }
                }
                case 3 -> {
                    int sold = 0;
                    for (int r = 0; r < ROWS; r++) {
                        for (int c = 0; c < COLS; c++) {
                            if (hall[r][c] == 'X') {
                                sold++;
                            }
                        }
                    }
                    int total = ROWS * COLS;
                    int free = total - sold;
                    double occupancy = (double) sold / total * 100;
                    double revenue = sold * TICKET_PRICE;
 
                    System.out.println("--- СТАТИСТИКА ЗАЛУ ---");
                    System.out.printf(Locale.US, "%-20s%d%n", "Усього місць:", total);
                    System.out.printf(Locale.US, "%-20s%d%n", "Зайнятих місць:", sold);
                    System.out.printf(Locale.US, "%-20s%d%n", "Вільних місць:", free);
                    System.out.printf(Locale.US, "%-20s%.2f%%%n", "Заповненість залу:", occupancy);
                    System.out.printf(Locale.US, "%-20s%.2f грн%n", "Загальна виручка:", revenue);
                    System.out.println("=======================");
                }
                case 0 -> {
                    System.out.println("Зміну завершено. Гарного вечора!");
                    running = false;
                }
                default -> System.out.println("Помилка: оберіть пункт 0-3!");
            }
            System.out.println();
        }
 
        scanner.close();
    }
}
