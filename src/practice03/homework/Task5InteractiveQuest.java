package practice03.homework;

import java.util.Locale;
import java.util.Scanner;

public class Task5InteractiveQuest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        String border = "************************************************************";
        System.out.println("/" + border + "\\");
        System.out.println("|               ПРИГОДА: ВТЕЧА З ТЕМНИЦІ ЧАКЛУНА             |");
        System.out.println("\\" + border + "/");

        System.out.print("Введіть ім'я вашого героя: ");
        String name = scanner.nextLine().trim();

        System.out.print("Оберіть клас героя (1 - Воїн, 2 - Маг, 3 - Розбійник): ");
        int classChoice = scanner.nextInt();
        scanner.nextLine(); 

        String heroClass = switch (classChoice) {
            case 1 -> "Воїн";
            case 2 -> "Маг";
            case 3 -> "Розбійник";
            default -> "Волоцюга";
        };
        int playerHp = switch (classChoice) {
            case 1 -> 120;
            case 2 -> 75;
            case 3 -> 90;
            default -> 60;
        };
        int strength = switch (classChoice) {
            case 1 -> 15;
            case 2 -> 6;
            case 3 -> 10;
            default -> 8;
        };
        int mana = switch (classChoice) {
            case 1 -> 10;
            case 2 -> 50;
            case 3 -> 15;
            default -> 5;
        };
        int agility = switch (classChoice) {
            case 1 -> 8;
            case 2 -> 10;
            case 3 -> 18;
            default -> 8;
        };
        boolean hasSpellBook = (classChoice == 2);

        System.out.printf("[Створено персонажа]: %s %s (HP: %d, STR: %d, MP: %d, AGI: %d)%n",
                heroClass, name, playerHp, strength, mana, agility);

        boolean obstacleCleared = false;
        String artifacts = "немає";

        System.out.println();
        System.out.println("Ви прокидаєтеся в сирій камері. Перед вами 3 проходи.");
        System.out.print("Куди ви прямуєте? (північ / схід / захід): ");
        String direction = scanner.nextLine().trim();

        System.out.println();

        switch (direction.toLowerCase()) {
            case "північ" -> {
                System.out.println("Ви входите до Кімнати Стародавньої Загадки.");
                System.out.println("Магічний кристал шепоче: «Скільки буде 7 * 6?»");
                System.out.print("Ваша відповідь: ");
                String answer = scanner.nextLine().trim();

                boolean isCorrect = answer.equals("42");
                playerHp = isCorrect ? (playerHp + 20) : (playerHp - 30);
                obstacleCleared = isCorrect;

                if (isCorrect) {
                    System.out.println("Кристал спалахує теплим світлом! (+20 HP)");
                    artifacts = "Благословення кристала";
                } else {
                    System.out.println("Кристал б'є блискавкою! (-30 HP)");
                }
            }
            case "схід" -> {
                System.out.println("Ви зайшли до арсеналу вартового. Тут стоїть кована скриня!");
                System.out.println("Спроба відкрити скриню...");

                if (agility >= 16 || (mana >= 25 && hasSpellBook)) {
                    String reason = (agility >= 16)
                            ? "Завдяки спритності (" + agility + " >= 16) ви підібрали замок!"
                            : "Завдяки мані (" + mana + " >= 25) скриня відчинилася без ключа!";
                    System.out.println("Успіх! " + reason);

                    if (agility >= 16) {
                        strength += 10;
                        artifacts = "Легендарний меч (+10 сили)";
                        System.out.println("Знайдено: Легендарний меч (+10 сили). Сила: " + strength);
                    } else {
                        playerHp += 20;
                        artifacts = "Амулет Світла (+20 HP)";
                        System.out.println("Знайдено: Амулет Світла (+20 HP). Поточне здоров'я: " + playerHp + " HP.");
                    }
                    obstacleCleared = true;
                } else {
                    playerHp -= 25;
                    System.out.println("Клац! Спрацювала пастка зі стрілами! (-25 HP)");
                }
            }
            case "захід" -> {
                System.out.println("Ви входите до печери, де прокидається кам'яний голем!");
                System.out.print("Тактика (1 - Лобова атака, 2 - Магічний бар'єр, 3 - Прослизнути повз): ");
                int tactic = scanner.nextInt();
                scanner.nextLine();

                switch (tactic) {
                    case 1 -> {
                        if (strength >= 12) {
                            System.out.println("Потужний удар руйнує голема! (сила " + strength + " >= 12)");
                            obstacleCleared = true;
                        } else {
                            playerHp -= 40;
                            System.out.println("Вам не вистачає сили (" + strength + " < 12). Голем б'є у відповідь! (-40 HP)");
                        }
                    }
                    case 2 -> {
                        if (mana >= 30) {
                            System.out.println("Магічний бар'єр витримав удари голема! (мана " + mana + " >= 30)");
                            obstacleCleared = true;
                        } else {
                            playerHp -= 30;
                            System.out.println("Мани замало (" + mana + " < 30), бар'єр розсипається! (-30 HP)");
                        }
                    }
                    case 3 -> {
                        if (agility >= 14) {
                            System.out.println("Ви спритно прослизаєте повз голема! (спритність " + agility + " >= 14)");
                            obstacleCleared = true;
                        } else {
                            playerHp -= 35;
                            System.out.println("Ви надто повільні (" + agility + " < 14). Голем вас помітив! (-35 HP)");
                        }
                    }
                    default -> {
                        playerHp -= 20;
                        System.out.println("Ви вагаєтесь, і голем завдає удару! (-20 HP)");
                    }
                }
            }
            default -> {
                playerHp -= 10;
                System.out.println("Ви заблукали в темряві й вдарились об стіну. (-10 HP)");
            }
        }

        System.out.println();
        String rank;
        if (playerHp <= 0) {
            rank = "Загиблий";
            System.out.println("Кінцівка: Поразка. Герой загинув у темряві підземелля...");
        } else if (obstacleCleared) {
            rank = (playerHp >= 90) ? "Легенда" : (playerHp >= 50) ? "Герой" : "Вцілілий";
            System.out.println("Фінал: Ви знайшли таємний важіль і відкрили головні ворота!");
            System.out.println("🏆 ВІТАЄМО! ВИ УСПІШНО ВИБРАЛИСЯ З ТЕМНИЦІ!");
            System.out.println("Ворота розчиняються, і герой виходить на світло сонця!");
        } else {
            rank = "Бранець";
            System.out.println("Ви вціліли, але не подолали перешкоду. Ворота залишаються зачиненими...");
        }

        System.out.println();
        System.out.println("--------------- ФІНАЛЬНА СТАТИСТИКА ---------------");
        System.out.printf("Герой:               %s (%s)%n", name, heroClass);
        System.out.printf("Залишок здоров'я:    %d HP%n", Math.max(playerHp, 0));
        System.out.printf("Здобуті артефакти:   %s%n", artifacts);
        System.out.printf("Ранг проходження:    %s%n", rank);
        System.out.println("---------------------------------------------------");
        System.out.println("\\" + border + "/");

        scanner.close();
    }
}