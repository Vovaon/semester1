package practice06.homework;

import java.util.Random;
import java.util.Scanner;

public class Task5AnimeHeroBattle {


    private static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        Asta asta = new Asta();
        String bossName = "Патри";
        int bossMaxHp = 1200;
        int bossHp = bossMaxHp;

        System.out.println("================ ⚔️ АРЕНА КОРОЛІВСТВА КОНЮШИНИ ⚔️ ================");
        System.out.println("Бос: " + bossName + " (Маг Світла) [HP: " + bossHp + " / " + bossMaxHp + "]");
        System.out.println("Герой: " + asta.getName() + " [HP: " + asta.getHp() + " / " + asta.getMaxHp()
                + "] | Антимагія: [ " + asta.getAntiMagicEnergy() + " / 100 ]");

        int round = 0;
        while (asta.isAlive() && bossHp > 0) {
            round++;
            System.out.println();
            System.out.println("--- Раунд " + round + " ---");

            int damage = 0;
            boolean turnUsed = false;


            while (!turnUsed) {
                System.out.println("Оберіть дію Асти:");
                System.out.println("[1] Удар мечем Демоноборця (+20 енергії)");
                System.out.println("[2] Застосувати суперздібність");
                System.out.println("[3] Захисна стійка мечем");
                System.out.println("[4] Переглянути статус");
                System.out.print("Ваш вибір: ");
                int choice = readInt(scanner);
                System.out.println();

                switch (choice) {
                    case 1:
                        damage = asta.basicSwordAttack();
                        turnUsed = true;
                        break;
                    case 2:
                        String[] skills = asta.getSkills();
                        System.out.println("Оберіть навичку:");
                        for (int i = 0; i < skills.length; i++) {
                            System.out.println("[" + (i + 1) + "] " + skills[i]
                                    + " (потрібно " + asta.getSkillCost(i) + " енергії)");
                        }
                        System.out.println("[0] Назад");
                        System.out.print("Ваш вибір: ");
                        int skillChoice = readInt(scanner);
                        System.out.println();
                        if (skillChoice == 0) {
                            break;
                        }
                        int energyBefore = asta.getAntiMagicEnergy();
                        damage = asta.useSkill(skillChoice - 1);
                        // Якщо енергія витрачена, навичка спрацювала і хід завершено
                        if (asta.getAntiMagicEnergy() != energyBefore) {
                            turnUsed = true;
                        }
                        break;
                    case 3:
                        asta.defend();
                        turnUsed = true;
                        break;
                    case 4:
                        asta.showStatus();
                        break;
                    default:
                        System.out.println("⚠️ Невірний вибір, спробуйте ще раз.");
                }
                System.out.println();
            }

            bossHp -= damage;
            if (bossHp < 0) {
                bossHp = 0;
            }

            if (bossHp == 0) {
                break;
            }


            int bossAttack = 100 + random.nextInt(71); // 100..170
            System.out.println("🔥 " + bossName + " атакує «Променями Покарання Світла»!");
            asta.takeDamage(bossAttack);
            asta.pushLimits();

            System.out.println();
            System.out.println("[СТАТУС] " + asta.getName() + ": HP " + asta.getHp() + "/" + asta.getMaxHp()
                    + " | Бос " + bossName + ": HP " + bossHp + "/" + bossMaxHp);
            System.out.println("==================================================================");
        }

        System.out.println();
        if (bossHp == 0) {
            System.out.println("🏆 ПЕРЕМОГА! Аста перемагає " + bossName + " силою антимагії!");
        } else {
            System.out.println("💀 ПОРАЗКА... Аста падає на арені. Але він ніколи не здається — спробуйте ще раз!");
        }

        scanner.close();
    }
}


