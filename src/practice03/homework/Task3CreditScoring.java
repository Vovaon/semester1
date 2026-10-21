package practice03.homework;
import java.util.Locale;
import java.util.Scanner;
public class Task3CreditScoring {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); 

        System.out.println("=== СИСТЕМА БАНКІВСЬКОГО СКОРИНГУ ===");

        System.out.print("Введіть вік позичальника: ");
        int age = scanner.nextInt();

        System.out.print("Введіть офіційний місячний дохід (грн): ");
        double monthlyIncome = scanner.nextDouble();

        System.out.print("Чи є негативна кредитна історія? (true/false): ");
        boolean hasBadCreditHistory = scanner.nextBoolean();

        System.out.print("Введіть запитувану суму кредиту (грн): ");
        double loanAmount = scanner.nextDouble();

        System.out.print("Введіть бажаний термін (місяців): ");
        int loanMonths = scanner.nextInt();

        boolean isAgeValid = (age >= 21 && age <= 65);
        boolean isHistoryClean = !hasBadCreditHistory;

        boolean isPaymentAffordable = (loanMonths > 0)
                && ((loanAmount / loanMonths) <= (monthlyIncome * 0.5));
        double annualRate = (monthlyIncome >= 35000) ? 14.5 : 21.0;

        System.out.println();
        System.out.println("------------- РІШЕННЯ СКОРИНГУ -------------");

        if (isAgeValid && isHistoryClean && isPaymentAffordable) {
            double monthlyPayment = loanAmount / loanMonths;
            double percentOfIncome = monthlyPayment / monthlyIncome * 100;
            double maxPayment = monthlyIncome * 0.5;

            System.out.println("Статус заявки:  СХВАЛЕНО");
            System.out.printf(Locale.US, "Орієнтовний платіж/міс:   %.2f грн (%.1f%% від доходу)%n",
                    monthlyPayment, percentOfIncome);
            System.out.printf(Locale.US, "Персональна ставка:   %.1f%% річних%n", annualRate);
            System.out.printf(Locale.US, "Максимально допустимий платіж:    %.2f грн/міс%n", maxPayment);
        } else {
            System.out.println("Статус заявки:  ВІДХИЛЕНО ");
            if (!isAgeValid) {
                System.out.println("Відмова: вік не відповідає критеріям (21-65 років)");
            } else if (!isHistoryClean) {
                System.out.println("Відмова: виявлено негативну кредитну історію");
            } else {
                System.out.println("Відмова: термін кредиту некоректний або щомісячний платіж перевищує 50% доходу");
            }
        }

        System.out.println("=============================================");

        scanner.close();
    }
}
