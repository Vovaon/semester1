package practice05.homework;
import java.util.Locale;
public class Task2WarehouseInventory {
        public static void main(String[] args) {
        String[] items = {"Ноутбук", "Смартфон", "Бездротові навушники", "Механічна клавіатура", "Монітор 27\""};
        int[] quantities = {8, 3, 25, 4, 12};
        double[] prices = {28500.0, 19200.0, 1850.0, 3100.0, 9400.0};
 
        final int LOW_STOCK_LIMIT = 5;
 
        double[] totalValues = new double[items.length];
        double warehouseTotal = 0;
        int maxValueIndex = 0;
        for (int i = 0; i < items.length; i++) {
            totalValues[i] = quantities[i] * prices[i];
            warehouseTotal += totalValues[i];
            if (totalValues[i] > totalValues[maxValueIndex]) {
                maxValueIndex = i;
            }
        }
 
        System.out.println("=================== ЗВІТ СКЛАДУ ЕЛЕКТРОНІКИ ===================");
        System.out.printf(Locale.US, "%-2s| %-23s | %-6s | %-10s | %s%n",
                "№", "Назва товару", "К-сть", "Ціна (грн)", "Загальна вартість");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < items.length; i++) {
            System.out.printf(Locale.US, "%-2d| %-23s | %-6s | %-10.2f | %.2f грн%n",
                    i + 1, items[i], quantities[i] + " шт.", prices[i], totalValues[i]);
        }
        System.out.println("----------------------------------------------------------------");
        System.out.printf(Locale.US, "Загальна вартість складу: %.2f грн%n", warehouseTotal);
        System.out.printf(Locale.US, "Найбільший капітал у товарі: %s (%.2f грн)%n",
                items[maxValueIndex], totalValues[maxValueIndex]);
 
        System.out.println();
        System.out.println(" ДЕФІЦИТНІ ПОЗИЦІЇ (менше " + LOW_STOCK_LIMIT + " шт.):");
        boolean hasShortage = false;
        for (int i = 0; i < items.length; i++) {
            if (quantities[i] < LOW_STOCK_LIMIT) {
                System.out.println("- " + items[i] + " (залишилось: " + quantities[i]
                        + " шт.) ->  Терміново дозамовити!");
                hasShortage = true;
            }
        }
        if (!hasShortage) {
            System.out.println("Дефіциту немає - усі позиції в нормі.");
        }
        System.out.println("================================================================");
    }

}
