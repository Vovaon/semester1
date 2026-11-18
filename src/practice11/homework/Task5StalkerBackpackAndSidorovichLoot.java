package practice11.homework;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Task5StalkerBackpackAndSidorovichLoot {

    private static final double MAX_SAFE_RADIATION = 50.0;
    private static final int LEAD_CONTAINER_THRESHOLD = 15_000;

    static class Artifact {
        private String name;
        private int costKarbovanets;
        private double radiationLevel;

        public Artifact(String name, int costKarbovanets, double radiationLevel) {
            this.name = name;
            this.costKarbovanets = costKarbovanets;
            this.radiationLevel = radiationLevel;
        }

        public String getName() {
            return name;
        }

        public int getCostKarbovanets() {
            return costKarbovanets;
        }

        public double getRadiationLevel() {
            return radiationLevel;
        }

        public boolean isRadioactiveDud(double maxSafeRadiation) {
            return radiationLevel > maxSafeRadiation || costKarbovanets <= 0;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%s [%d крб, %.1f мкЗв/год]",
                    name, costKarbovanets, radiationLevel);
        }
    }

    public static void demonstrateFailFastWithForEach(List<Artifact> backpack) {
        System.out.println("  ❌ Спроба викликати backpack.remove(artifact) всередині for-each...");
        try {
            for (Artifact artifact : backpack) {
                if (artifact.isRadioactiveDud(MAX_SAFE_RADIATION)) {
                    backpack.remove(artifact);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ⚡ Перехоплено ConcurrentModificationException! КПК зависає: "
                    + "прямий remove() порушив modCount ітератора рюкзака.");
        }
    }

    public static int purgeRadioactiveDudsWithIterator(List<Artifact> backpack, double maxSafeRadiation) {
        Iterator<Artifact> iterator = backpack.iterator();
        int removedDuds = 0;
        while (iterator.hasNext()) {
            Artifact current = iterator.next();
            if (current.getRadiationLevel() > maxSafeRadiation || current.getCostKarbovanets() <= 0) {
                System.out.println("  ☢ [Iterator.remove] Викинуто небезпечну пустушку: "
                        + current.getName() + " (Радіація: " + current.getRadiationLevel() + " мкЗв/год)");
                iterator.remove();
                removedDuds++;
            }
        }
        return removedDuds;
    }

    private static double totalRadiation(List<Artifact> items) {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum += items.get(i).getRadiationLevel();
        }
        return sum;
    }

    private static void printBackpackState(List<Artifact> backpack) {
        for (int i = 0; i < backpack.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + backpack.get(i));
        }
        System.out.println("  Усього елементів: " + backpack.size());
        System.out.printf(Locale.US, "  Сумарний радіаційний фон рюкзака: %.1f мкЗв/год%n",
                totalRadiation(backpack));
    }

    private static void sortByCostDescending(List<Artifact> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            int bestIndex = i;
            for (int j = i + 1; j < items.size(); j++) {
                if (items.get(j).getCostKarbovanets() > items.get(bestIndex).getCostKarbovanets()) {
                    bestIndex = j;
                }
            }
            if (bestIndex != i) {
                Artifact temp = items.get(i);
                items.set(i, items.get(bestIndex));
                items.set(bestIndex, temp);
            }
        }
    }

    private static void printContainer(String title, List<Artifact> container) {
        System.out.println("  " + title + " (" + container.size() + " шт.):");
        if (container.isEmpty()) {
            System.out.println("     порожньо");
        }
        for (int i = 0; i < container.size(); i++) {
            System.out.println("     - " + container.get(i));
        }
    }

    public static void main(String[] args) {
        System.out.println("=============== S.T.A.L.K.E.R. 2: РЮКЗАК ТА ХАБАР ДЛЯ СИДОРОВИЧА ===============");

        var backpack = new ArrayList<Artifact>();
        backpack.add(new Artifact("Крівник", 8_500, 12.5));
        backpack.add(new Artifact("Іржава Пустушка", 0, 8.0));
        backpack.add(new Artifact("Бенгальський вогонь", 21_000, 35.0));
        backpack.add(new Artifact("Фонячa Пустушка", 3_000, 120.5));
        backpack.add(new Artifact("Виверт", 18_500, 28.4));
        backpack.add(new Artifact("Граві", 11_200, 45.0));
        backpack.add(new Artifact("Аномальний Шлак", 250, 310.0));
        backpack.add(new Artifact("Компас", 15_000, 20.0));

        System.out.println("\n[1. СТАН РЮКЗАКА ДО ЧИСТКИ]:");
        printBackpackState(backpack);

        System.out.println("\n[2. ПАСТКА FAIL-FAST]:");
        demonstrateFailFastWithForEach(new ArrayList<>(backpack));

        System.out.println("\n[3. БЕЗПЕЧНА ДЕЗАКТИВАЦІЯ ПУСТУШОК ЧЕРЕЗ ITERATOR]:");
        int removed = purgeRadioactiveDudsWithIterator(backpack, MAX_SAFE_RADIATION);
        System.out.println("  -> Видалено небезпечних пустушок: " + removed + " шт.");
        System.out.println("  -> Стан рюкзака після чистки:");
        printBackpackState(backpack);

        System.out.println("\n[4. РОЗПОДІЛ ПО КОНТЕЙНЕРАХ ТА РАНЖУВАННЯ]:");
        ArrayList<Artifact> leadSpecialContainer = new ArrayList<>();
        ArrayList<Artifact> standardBeltContainer = new ArrayList<>();
        for (int i = 0; i < backpack.size(); i++) {
            Artifact artifact = backpack.get(i);
            if (artifact.getCostKarbovanets() >= LEAD_CONTAINER_THRESHOLD) {
                leadSpecialContainer.add(artifact);
            } else {
                standardBeltContainer.add(artifact);
            }
        }
        sortByCostDescending(leadSpecialContainer);
        sortByCostDescending(standardBeltContainer);
        printContainer("Свинцевий спецконтейнер (від 15 000 крб)", leadSpecialContainer);
        printContainer("Стандартний поясний контейнер (до 15 000 крб)", standardBeltContainer);

        ArrayList<Artifact> rankedLoot = new ArrayList<>();
        for (int i = 0; i < leadSpecialContainer.size(); i++) {
            rankedLoot.add(leadSpecialContainer.get(i));
        }
        for (int i = 0; i < standardBeltContainer.size(); i++) {
            rankedLoot.add(standardBeltContainer.get(i));
        }
        sortByCostDescending(rankedLoot);

        System.out.println("\n  Ранжований хабар для викладки на стіл Сидоровича (від найдорожчого):");
        for (int i = 0; i < rankedLoot.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + rankedLoot.get(i));
        }

        int maxCost = Integer.MIN_VALUE;
        String bestName = "";
        int totalValue = 0;
        for (int i = 0; i < rankedLoot.size(); i++) {
            Artifact artifact = rankedLoot.get(i);
            totalValue += artifact.getCostKarbovanets();
            if (artifact.getCostKarbovanets() > maxCost) {
                maxCost = artifact.getCostKarbovanets();
                bestName = artifact.getName();
            }
        }

        System.out.println("\n[5. ФІНАЛЬНИЙ ЧЕК ВІД СИДОРОВИЧА]:");
        System.out.println("  Найцінніший артефакт вилазки: " + bestName + " (" + maxCost + " крб)");
        System.out.println("  Загальна вартість чистого хабару: " + totalValue + " крб");
        System.out.println("  Сидорович: «Годиться, сталкере. Тримай свої карбованці. Але наступного разу "
                + "пустушки лишай у Зоні!»");
        System.out.println("=================================================================================");
    }
}
