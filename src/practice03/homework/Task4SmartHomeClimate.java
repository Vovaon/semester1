package practice03.homework;

import java.util.Locale;
import java.util.Scanner;

public class Task4SmartHomeClimate {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== ПАНЕЛЬ КЛІМАТ-КОНТРОЛЮ SMART HOME ===");

        System.out.print("Поточна температура (°C): ");
        double currentTemp = scanner.nextDouble();

        System.out.print("Бажана температура (°C): ");
        double desiredTemp = scanner.nextDouble();

        System.out.print("Вологість повітря (%): ");
        int humidity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Сезон (зима/літо/міжсезоння): ");
        String season = scanner.nextLine().trim();

        System.out.print("Режим будинку (1-Standby, 2-Eco, 3-Party, 4-Night, 5-Vacation): ");
        int mode = scanner.nextInt();

        System.out.print("Чи відчинені вікна? (true/false): ");
        boolean isWindowOpen = scanner.nextBoolean();

        System.out.println();

        boolean isEmergency = isWindowOpen && (mode != 5);
        if (isEmergency) {
            System.out.println("ПОПЕРЕДЖЕННЯ: Відчинені вікна! Обігрів та кондиціонування примусово заблоковано!");
            System.out.println();
        }

        boolean isWinter = season.equalsIgnoreCase("зима");
        boolean isSummer = season.equalsIgnoreCase("літо");

        double effectiveTargetTemp = switch (mode) {
            case 1 -> desiredTemp;
            case 2 -> {
                double delta;
                if (isWinter) {
                    delta = -2.0;
                } else if (isSummer) {
                    delta = 2.0;
                } else {
                    delta = 0.0;
                }
                yield desiredTemp + delta;
            }
            case 3 -> desiredTemp - 1.5;
            case 4 -> 19.0;
            case 5 -> {
                if (isWinter) {
                    yield 12.0;
                }
                yield 28.0;
            }
            default -> desiredTemp;
        };

        String modeName = switch (mode) {
            case 1 -> "Standby (Звичайний)";
            case 2 -> "Eco (Енергозбереження)";
            case 3 -> "Party (Гості)";
            case 4 -> "Night (Ніч)";
            case 5 -> "Vacation (Відпустка)";
            default -> "Невідомий режим (використано бажану температуру)";
        };

        String energy = switch (mode) {
            case 2, 5 -> "Оптимізоване";
            case 3 -> "Підвищене";
            default -> "Стандартне";
        };

        String adjustment = (effectiveTargetTemp != desiredTemp)
                ? String.format(Locale.US, " (скориговано з %.1f°C", desiredTemp)
                  + (mode == 2 ? " для сезону: " + season : "") + ")"
                : "";

        String climateStatus;
        if (isEmergency) {
            climateStatus = "ЗАБЛОКОВАНО (відчинені вікна)";
            energy = "Мінімальне (кліматичні прилади вимкнено)";
        } else if (currentTemp - effectiveTargetTemp > 0.5) {
            climateStatus = "Кондиціонер УВІМКНЕНО (Охолодження)";
        } else if (effectiveTargetTemp - currentTemp > 0.5) {
            climateStatus = "Опалення УВІМКНЕНО (Обігрів)";
        } else {
            climateStatus = "Температура в зоні комфорту (Кліматичні прилади в очікуванні)";
        }

        String humidityStatus = (humidity < 40)
                ? "Зволожувач УВІМКНЕНО (вологість " + humidity + "% < 40%)"
                : (humidity > 65)
                    ? "Осушувач УВІМКНЕНО (вологість " + humidity + "% > 65%)"
                    : "Вологість оптимальна (" + humidity + "%)";

        System.out.println("------------- СТАТУС СИСТЕМИ -------------");
        System.out.printf("Режим роботи:              %s%n", modeName);
        System.out.printf(Locale.US, "Цільова температура:       %.2f°C%s%n", effectiveTargetTemp, adjustment);
        System.out.printf(Locale.US, "Поточна температура:       %.2f°C%n", currentTemp);
        System.out.printf("Стан терморегуляції:       %s%n", climateStatus);
        System.out.printf("Стан мікроклімату:         %s%n", humidityStatus);
        System.out.printf("Енергоспоживання:          %s%n", energy);
        System.out.println("==========================================");

        scanner.close();
    }
}