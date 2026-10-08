package practice05.homework;
import java.util.Scanner;
public class Task5RobloxPetSimulator {
       public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        String[] petEmojis = {"🐶", "🐱", "🐉", "🦄", "💎"};
        String[] petNames = {"Dog", "Cat", "Dragon", "Unicorn", "Huge Titanic Cat"};
        int[] petPowers = {15, 25, 120, 350, 2000};
        int[] dropChances = {45, 30, 15, 8, 2}; // у сумі 100%
 
        String[] inventoryNames = new String[8];
        int[] inventoryPowers = new int[8];
        int petCount = 0; 
 
        final int EGG_PRICE = 100;
        int coins = 300;
 
        System.out.println("======================================================");
        System.out.println("            ROBLOX: PET SIMULATOR ");
        System.out.println("======================================================");
 
        boolean playing = true;
        while (playing) {
            System.out.println("Баланс: " + coins + " монет | Вихованців у рюкзаку: "
                    + petCount + "/" + inventoryNames.length);
            System.out.println();
            System.out.println("1 - Відкрити чарівне яйце (" + EGG_PRICE + " монет)");
            System.out.println("2 - Відкрити рюкзак-інвентар");
            System.out.println("3 - Порахувати силу команди");
            System.out.println("4 - Фармити монети (+50..150 монет)");
            System.out.println("0 - Вийти з гри");
            System.out.print("Ваш вибір: ");
            int choice = scanner.nextInt();
            System.out.println();
 
            switch (choice) {
                case 1 -> {
                    if (coins < EGG_PRICE) {
                        System.out.println("Не вистачає монет! Потрібно " + EGG_PRICE
                                + ", а у вас " + coins + ". Пофармте монети (пункт 4).");
                    } else if (petCount >= inventoryNames.length) {
                        System.out.println("Рюкзак переповнений! Продайте або видаліть зайвих петів.");
                    } else {
                        System.out.println(" Трісь! Яйце розколюється...");
 
                        int roll = (int) (Math.random() * 100) + 1;
                        int petIndex = 0;
                        int cumulative = 0;
                        for (int i = 0; i < dropChances.length; i++) {
                            cumulative += dropChances[i];
                            if (roll <= cumulative) {
                                petIndex = i;
                                break;
                            }
                        }
 
                        inventoryNames[petCount] = petNames[petIndex];
                        inventoryPowers[petCount] = petPowers[petIndex];
                        petCount++;
                        coins -= EGG_PRICE;
 
                        if (dropChances[petIndex] <= 2) {
                            System.out.println("НЕЙМОВІРНО! СУПЕР-ДЖЕКПОТ!");
                            System.out.println(petEmojis[petIndex] + " " + petNames[petIndex]
                                    + "! (Сила: " + petPowers[petIndex] + ")");
                            System.out.println("Залишок монет: " + coins);
                        } else {
                            System.out.println(" ВАМ ВИПАВ: " + petEmojis[petIndex] + " " + petNames[petIndex]
                                    + "! (Сила: " + petPowers[petIndex] + ")");
                            System.out.println("Вихованця додано в інвентар! Залишок монет: " + coins);
                        }
                    }
                }
                case 2 -> {
                    if (petCount == 0) {
                        System.out.println(" Рюкзак порожній. Відкрийте яйце!");
                    } else {
                        System.out.println("--- ВАШ РЮКЗАК ---");
                        for (int i = 0; i < petCount; i++) {
                            System.out.println("[Слот " + (i + 1) + "] " + inventoryNames[i]
                                    + " (Сила: " + inventoryPowers[i] + ")");
                        }
                    }
                }
                case 3 -> {
                    if (petCount == 0) {
                        System.out.println("У вас ще немає вихованців - команда порожня.");
                    } else {
                        int totalPower = 0;
                        int bestIndex = 0; 
                        for (int i = 0; i < petCount; i++) {
                            totalPower += inventoryPowers[i];
                            if (inventoryPowers[i] > inventoryPowers[bestIndex]) {
                                bestIndex = i;
                            }
                        }
                        System.out.println("--- СТАТИСТИКА ВАШОЇ КОМАНДИ ---");
                        System.out.println("Кількість вихованців: " + petCount + " шт.");
                        System.out.println("Загальна сила команди: " + totalPower + " очок шкоди");
                        System.out.println(" Найсильніший улюбленець: " + inventoryNames[bestIndex]
                                + " (Сила: " + inventoryPowers[bestIndex] + ")");
                    }
                }
                case 4 -> {
                    int earned = (int) (Math.random() * 101) + 50;
                    coins += earned;
                    System.out.println(" Ви розбили купу монет на локації! +" + earned
                            + " монет. Баланс: " + coins);
                }
                case 0 -> {
                    System.out.println("До зустрічі в Pet Simulator! ");
                    playing = false;
                }
                default -> System.out.println("Помилка: оберіть пункт 0-4!");
            }
            System.out.println("======================================================");
        }
 
        scanner.close();
    }

}
