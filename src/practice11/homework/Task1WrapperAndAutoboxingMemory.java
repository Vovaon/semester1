package practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1WrapperAndAutoboxingMemory {
    public static void inspectIntegerCache(int[] testValues) {
        System.out.println("=== IntegerCache: == vs equals() ===");
        System.out.printf("%-8s | %-14s | %-20s%n", "Value", "first == second", "first.equals(second)");
        System.out.println("---------+----------------+---------------------");
        for (int val : testValues) {
            Integer first = Integer.valueOf(val);
            Integer second = Integer.valueOf(val);
            System.out.printf("%-8d | %-14b | %-20b%n", val, first == second, first.equals(second));
        }
        System.out.println();
    }
    public static void demonstrateAutoboxingUnderTheHood() {
        System.out.println("=== Autoboxing / Unboxing ===");

        Integer autoInt = 42;
        Integer explicitInt = Integer.valueOf(42);
        int autoUnboxInt = autoInt;
        int explicitUnboxInt = explicitInt.intValue();
        System.out.println("Integer: " + autoInt.equals(explicitInt)
                + ", unboxing збігається: " + (autoUnboxInt == explicitUnboxInt));


        Double autoDouble = 3.14;
        Double explicitDouble = Double.valueOf(3.14);
        double autoUnboxDouble = autoDouble;
        double explicitUnboxDouble = explicitDouble.doubleValue();
        System.out.println("Double: " + autoDouble.equals(explicitDouble)
                + ", unboxing збігається: " + (autoUnboxDouble == explicitUnboxDouble));

        Boolean autoBool = true;
        Boolean explicitBool = Boolean.valueOf(true);
        boolean autoUnboxBool = autoBool;
        boolean explicitUnboxBool = explicitBool.booleanValue();
        System.out.println("Boolean: " + autoBool.equals(explicitBool)
                + ", unboxing збігається: " + (autoUnboxBool == explicitUnboxBool));


        List<Integer> data = Arrays.asList(45, null, -12, 88, null, 102);
        int sum = 0;
        for (Integer item : data) {
            if (item == null) {
                System.out.println("Пропущено null (unboxing спричинив би NullPointerException)");
                continue;
            }
            sum += item;
        }
        System.out.println("Сума без null: " + sum);

        try {
            Integer nullValue = null;
            int boom = nullValue; // NPE
            System.out.println(boom);
        } catch (NullPointerException e) {
            System.out.println("Спіймано NullPointerException при unboxing null");
        }
        System.out.println();
    }

    public static List<Integer> parseTelemetryStream(String[] rawPackets) {
        List<Integer> result = new ArrayList<>();
        for (String raw : rawPackets) {
            if (raw == null) {
                System.out.println("Пропущено: null-пакет");
                continue;
            }
            String cleaned = raw.trim();
            if (cleaned.isEmpty()) {
                System.out.println("Пропущено: порожній пакет");
                continue;
            }
            try {
                result.add(Integer.parseInt(cleaned));
            } catch (NumberFormatException e) {
                System.out.println("Пошкоджений пакет: \"" + cleaned + "\"");
            }
        }
        return result;
    }

    public static void printExtremesAndStats(List<Integer> validData) {
        System.out.println("=== Екстремуми та статистика ===");
        if (validData == null || validData.isEmpty()) {
            System.out.println("Немає даних для аналізу.");
            return;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0;

        for (Integer val : validData) {
            if (val == null) continue;
            if (val < min) min = val;
            if (val > max) max = val;
            sum += val;
        }
        System.out.println("Мінімум: " + min);
        System.out.println("Максимум: " + max);
        System.out.println("Сума: " + sum);

        List<Integer> byIndex = new ArrayList<>(validData);
        List<Integer> byObject = new ArrayList<>(validData);

        int val = byIndex.get(0);
        System.out.println("\nПастка видалення, тестове значення = " + val);

        byIndex.remove(0);
        System.out.println("remove(0)                    -> " + byIndex
                + "  (видалено елемент за індексом)");

        byObject.remove(Integer.valueOf(val));
        System.out.println("remove(Integer.valueOf(" + val + ")) -> " + byObject
                + "  (видалено за значенням)");

        List<Integer> trap = new ArrayList<>(Arrays.asList(10, 20, 30, 0));
        trap.remove(0);
        System.out.println("[10, 20, 30, 0].remove(0)    -> " + trap + "  (індекс, а не значення 0!)");
        trap = new ArrayList<>(Arrays.asList(10, 20, 30, 0));
        trap.remove(Integer.valueOf(0));
        System.out.println("[10, 20, 30, 0].remove(Integer.valueOf(0)) -> " + trap);
        System.out.println();
    }

    public static void main(String[] args) {
        inspectIntegerCache(new int[]{-128, -50, 0, 127, 128, 500, 2026});

        demonstrateAutoboxingUnderTheHood();

        String[] rawPackets = {" 145 ", "-30", "ERR_404", "980", "", null, "2147483647", "-85"};
        List<Integer> valid = parseTelemetryStream(rawPackets);
        System.out.println("Валідні дані: " + valid + "\n");
        printExtremesAndStats(valid);
    }
}