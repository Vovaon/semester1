package practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Task4ArrayListVsLinkedListPerformance {

    private static final int ELEMENTS_COUNT = 20_000;

    private static double toMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    private static double measureAppend(List<Integer> list, int count) {
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        return toMillis(System.nanoTime() - start);
    }

    private static double measureInsertAtHead(List<Integer> list, int count) {
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(0, i);
        }
        return toMillis(System.nanoTime() - start);
    }

    private static double measureInsertInMiddle(List<Integer> list, int count) {
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(list.size() / 2, i);
        }
        return toMillis(System.nanoTime() - start);
    }

    private static void warmUp() {
        for (int round = 0; round < 3; round++) {
            measureInsertAtHead(new ArrayList<>(), 2_000);
            measureInsertAtHead(new LinkedList<>(), 2_000);
            measureInsertInMiddle(new ArrayList<>(), 2_000);
            measureInsertInMiddle(new LinkedList<>(), 2_000);
            measureAppend(new ArrayList<>(), 2_000);
            measureAppend(new LinkedList<>(), 2_000);
        }
    }

    private static void printComparison(String title, double arrayMs, double linkedMs) {
        System.out.println(title);
        System.out.printf(Locale.US, "  - Час для ArrayList:  %.2f мс%n", arrayMs);
        System.out.printf(Locale.US, "  - Час для LinkedList: %.2f мс%n", linkedMs);
        if (linkedMs < arrayMs) {
            System.out.println("  (Висновок: LinkedList виграє у цьому сценарії).");
        } else {
            System.out.println("  (Висновок: ArrayList виграє у цьому сценарії).");
        }
        System.out.println();
    }

    private static void runBenchmark() {
        String formatted = String.format(Locale.US, "%,d", ELEMENTS_COUNT).replace(',', ' ');
        warmUp();

        double arrayAppend = measureAppend(new ArrayList<>(), ELEMENTS_COUNT);
        double linkedAppend = measureAppend(new LinkedList<>(), ELEMENTS_COUNT);

        double arrayHead = measureInsertAtHead(new ArrayList<>(), ELEMENTS_COUNT);
        double linkedHead = measureInsertAtHead(new LinkedList<>(), ELEMENTS_COUNT);

        double arrayMiddle = measureInsertInMiddle(new ArrayList<>(), ELEMENTS_COUNT);
        double linkedMiddle = measureInsertInMiddle(new LinkedList<>(), ELEMENTS_COUNT);

        printComparison("1. БЕНЧМАРК ВСТАВКИ У ПОЧАТОК СПИСКУ (на " + formatted + " елементів):",
                arrayHead, linkedHead);
        printComparison("   БЕНЧМАРК ВСТАВКИ В СЕРЕДИНУ СПИСКУ (на " + formatted + " елементів):",
                arrayMiddle, linkedMiddle);
        printComparison("   БЕНЧМАРК МАСОВОГО ДОДАВАННЯ В КІНЕЦЬ (на " + formatted + " елементів):",
                arrayAppend, linkedAppend);
    }

    private static void runSorting() {
        System.out.println("2. СОРТУВАННЯ СПИСКУ:");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(45, 12, 89, 3, 22, 67));
        System.out.println("   До сортування: " + numbers);
        Collections.sort(numbers);
        System.out.println("   Після сортування: " + numbers);
        System.out.println();
    }

    private static void runSubListDemo() {
        System.out.println("3. РОБОТА З subList():");
        List<Integer> original = new ArrayList<>(Arrays.asList(3, 12, 22, 45, 67, 89));
        System.out.println("   Оригінальний список: " + original);

        List<Integer> window = original.subList(1, 4);
        System.out.println("   Виділений subList (від індексу 1 до 4): " + window);

        System.out.println("   Змінюємо елемент у subList на 999...");
        window.set(0, 999);
        System.out.println("   Оновлений оригінальний список: " + original);
    }

    public static void main(String[] args) {
        System.out.println("=============== БЕНЧМАРК ТА РОБОТА ЗІ СПИСКАМИ ===============");
        runBenchmark();
        runSorting();
        runSubListDemo();
        System.out.println("==============================================================");
    }
}