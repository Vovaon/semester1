package practice03.homework;
import java.util.Locale;
import java.util.Scanner;
public class Task2DeliveryService {
     public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); 

        System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");

        System.out.print("Введіть вагу відправлення (кг): ");
        double weight = scanner.nextDouble();

        System.out.print("Введіть відстань транспортування (км): ");
        int distance = scanner.nextInt();

        System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
        int deliveryType = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        String premiumAnswer = scanner.nextLine().trim();
        if (weight <= 0 || weight > 50.0) {
            System.out.println("Помилка: вага має бути більшою за 0 та не перевищувати 50 кг.");
            scanner.close();
            return;
        }
        if (distance <= 0) {
            System.out.println("Помилка: відстань має бути більшою за 0 км.");
            scanner.close();
            return;
        }

        double baseTariff = switch (deliveryType) {
            case 1 -> 50.0;
            case 2 -> {
                if (weight > 15.0) {
                    System.out.println("Помилка: поштомат не приймає габаритні посилки (понад 15 кг).");
                    yield -1;
                }
                yield 60.0;
            }
            case 3 -> 100.0;
            default -> -1;
        };

        if (baseTariff < 0) {
            if (deliveryType < 1 || deliveryType > 3) {
                System.out.println("Помилка: невідомий тип доставки.");
            }
            scanner.close();
            return;
        }

        String deliveryName = switch (deliveryType) {
            case 1 -> "Відділення";
            case 2 -> "Поштомат";
            default -> "Кур'єр";
        };

        double distanceSurcharge;
        if (distance <= 50) {
            distanceSurcharge = 0.0;
        } else if (distance <= 200) {
            distanceSurcharge = 35.0;
        } else {
            distanceSurcharge = 80.0;
        }

        double subtotal = baseTariff + distanceSurcharge;
        boolean isPremium = premiumAnswer.equalsIgnoreCase("так");
        double total = isPremium ? subtotal * 0.80 : subtotal;
        String status = isPremium ? "Premium (-20%)" : "Стандарт";
        System.out.println();
        System.out.println("------------- НАКЛАДНА ДОСТАВКИ -------------");
        System.out.printf(Locale.US, "Тип доставки:              %s (базовий тариф: %.2f грн)%n", deliveryName, baseTariff);
        System.out.printf(Locale.US, "Доплата за відстань:       %.2f грн (%d км)%n", distanceSurcharge, distance);
        System.out.printf(Locale.US, "Сума до знижки:            %.2f грн%n", subtotal);
        System.out.printf("Статус клієнта:            %s%n", status);
        System.out.println("---------------------------------------------");
        System.out.printf(Locale.US, "РАЗОМ ДО СПЛАТИ:           %.2f грн%n", total);
        System.out.println("=============================================");
        scanner.close();
    }
}
