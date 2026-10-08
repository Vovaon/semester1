package practice04.homework;
import java.util.Scanner;
public class Task5BrawlStarsArena {
        private static final int BOSS_MAX_HP = 10000;
    private static final int BOSS_BASE_DAMAGE = 700;
    private static final int MAX_AMMO = 3;
    private static final int CHARGE_PER_ATTACK = 35;
    private static final int CUBE_BONUS_PERCENT = 15;
 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        System.out.println("======================================================");
        System.out.println("            BRAWL STARS: BOSS FIGHT ");
        System.out.println("======================================================");
        System.out.println("Оберіть свого бійця:");
        System.out.println("1. Шеллі (Shelly)     - HP: 4000 | Атака: 1200 | Супер: 2800");
        System.out.println("2. Кольт (Colt)       - HP: 3000 | Атака: 1600 | Супер: 3600");
        System.out.println("3. Ель Прімо (Primo)  - HP: 6000 | Атака: 1100 | Супер: 2200");
 
        int brawlerChoice;
        do {
            System.out.print("Ваш вибір (1-3): ");
            brawlerChoice = scanner.nextInt();
            if (brawlerChoice < 1 || brawlerChoice > 3) {
                System.out.println("Помилка: оберіть число від 1 до 3!");
            }
        } while (brawlerChoice < 1 || brawlerChoice > 3);
 
        String brawlerName;
        String attackName;      
        String attackVerb;      
        String superName;
        String superEffectText;
        int maxPlayerHp;
        int baseAttack;
        int baseSuper;
 
        switch (brawlerChoice) {
            case 1 -> {
                brawlerName = "Шеллі";
                attackName = "шротом";
                attackVerb = "влучно стріляє шротом";
                superName = "СУПЕР-ДРІБ";
                superEffectText = "Боса відкинуто вибухом!";
                maxPlayerHp = 4000;
                baseAttack = 1200;
                baseSuper = 2800;
            }
            case 2 -> {
                brawlerName = "Кольт";
                attackName = "чергою куль";
                attackVerb = "випускає чергу куль";
                superName = "КУЛЬОВА БУРЯ";
                superEffectText = "Бос під шквальним вогнем!";
                maxPlayerHp = 3000;
                baseAttack = 1600;
                baseSuper = 3600;
            }
            default -> { 
                brawlerName = "Ель Прімо";
                attackName = "кулаками";
                attackVerb = "обрушує удар кулаками";
                superName = "МЕТЕОРНИЙ СТРИБОК";
                superEffectText = "Боса оглушено - він пропустить наступний хід!";
                maxPlayerHp = 6000;
                baseAttack = 1100;
                baseSuper = 2200;
            }
        }
 
        System.out.println("Ви обрали бійця: " + brawlerName + "! Бій починається!");
 
        int playerHp = maxPlayerHp;
        int bossHp = BOSS_MAX_HP;
        int ammo = MAX_AMMO;
        int superCharge = 0;
        int cubes = 0;
        boolean bossStunned = false;
        boolean bossWeakened = false;
        int round = 0;
 
        while (playerHp > 0 && bossHp > 0) {
            round++;
 

            int phase = getBossPhase(bossHp, BOSS_MAX_HP);
            String phaseText = switch (phase) {
                case 1 -> "СПОКІЙНИЙ (Шкода: " + BOSS_BASE_DAMAGE + ")";
                case 2 -> " ANGRY! (+30% атаки)";
                default -> " ENRAGED! (+70% атаки)";
            };
 
            System.out.println();
            System.out.println("-------------------- РАУНД " + round + " --------------------");
            System.out.printf("%-12sHP: %s %d/%d | Набої: %d/%d | Супер: %s %d%%%s%n",
                    "[" + brawlerName + "]",
                    buildBar(playerHp, maxPlayerHp), playerHp, maxPlayerHp,
                    ammo, MAX_AMMO,
                    buildBar(Math.min(superCharge, 100), 100), superCharge,
                    superCharge >= 100 ? " (ГОТОВИЙ!)" : "");
            System.out.printf("%-12sHP: %s %d/%d | Фаза: %s%n",
                    "[Робо-Бос]",
                    buildBar(bossHp, BOSS_MAX_HP), bossHp, BOSS_MAX_HP,
                    phaseText);
            System.out.println("Куби підсилення: " + cubes + " шт."
                    + (cubes > 0 ? " (+" + (cubes * CUBE_BONUS_PERCENT) + "% до атаки)" : ""));
 
            System.out.println();
            System.out.println("Оберіть дію:");
            System.out.println("1 - Базова атака " + attackName + " (-1 набій)");
            System.out.println("2 - СУПЕР АТАКА " + (superCharge >= 100 ? "(ГОТОВО!)" : "(Не заряджено!)"));
            System.out.println("3 - Захисний маневр (відновлення набоїв та +15% HP)");
            System.out.println("4 - Ризик: пошук Енергетичного куба (Power Cube)");
 
            boolean turnTaken = false;
            while (!turnTaken) {
                System.out.print("Ваш хід: ");
                int action = scanner.nextInt();
                System.out.println();
                int damagePercent = 100 + CUBE_BONUS_PERCENT * cubes;
 
                switch (action) {
                    case 1 -> {
                        if (ammo >= 1) {
                            int damage = baseAttack * damagePercent / 100;
                            ammo--;
                            bossHp -= damage;
                            superCharge += CHARGE_PER_ATTACK;
                            System.out.println("dmg" + brawlerName + " " + attackVerb + "! Завдано " + damage + " шкоди!");
                            System.out.println(" Заряд супера: +" + CHARGE_PER_ATTACK + "% (Поточний: " + superCharge + "%)");
                        } else {
                            System.out.println(" Набої закінчились! " + brawlerName + " перезаряджається - хід втрачено.");
                        }
                        turnTaken = true;
                    }
                    case 2 -> {
                        if (superCharge < 100) {
                            System.out.println(" Супер ще не заряджено! Оберіть іншу дію.");
                        } else {
                            int damage = baseSuper * damagePercent / 100;
                            bossHp -= damage;
                            superCharge = 0;
                            System.out.println(" СУПЕР-АТАКА! «" + superName + "»!");
                            System.out.println(brawlerName + " трощить усе навколо! Завдано " + damage
                                    + " шкоди! " + superEffectText);
                            switch (brawlerChoice) {
                                case 1 -> bossWeakened = true;  
                                case 3 -> bossStunned = true;   
                                default -> { }                  
                            }
                            turnTaken = true;
                        }
                    }
                    case 3 -> {
                        int heal = maxPlayerHp * 15 / 100;
                        ammo = Math.min(MAX_AMMO, ammo + 2);
                        playerHp = Math.min(maxPlayerHp, playerHp + heal);
                        System.out.println("hp " + brawlerName + " ховається, перезаряджається та відновлює здоров'я!");
                        System.out.println("Набої: " + ammo + "/" + MAX_AMMO + " | HP: " + playerHp + "/" + maxPlayerHp);
                        turnTaken = true;
                    }
                    case 4 -> {
                        if (Math.random() < 0.6) {
                            cubes++;
                            System.out.println(" Удача! Знайдено Power Cube! Вся шкода назавжди +" + CUBE_BONUS_PERCENT + "%!");
                        } else {
                            System.out.println(" Куб не знайдено... Час витрачено даремно!");
                        }
                        turnTaken = true;
                    }
                    default -> System.out.println("Помилка: оберіть дію від 1 до 4!");
                }
            }
 
            if (bossHp < 0) {
                bossHp = 0;
            }
 
            if (bossHp > 0) {
                System.out.println();
                System.out.println(" Хід Боса:");
 
                if (bossStunned) {
                    System.out.println("Робо-Бос оглушений і пропускає хід!");
                    bossStunned = false;
                } else {
                    int bossDamage = BOSS_BASE_DAMAGE;
                    switch (getBossPhase(bossHp, BOSS_MAX_HP)) {
                        case 2 -> bossDamage = (int) (bossDamage * 1.3);
                        case 3 -> bossDamage = (int) (bossDamage * 1.7);
                        default -> { }
                    }
                    if (bossWeakened) {
                        bossDamage = bossDamage / 2;
                        bossWeakened = false;
                        System.out.println("(Бос ослаблений відкиданням - його удар слабший на 50%)");
                    }
 
                    if (Math.random() < 0.15) {
                        System.out.println("везучий " + brawlerName + " ухиляється від удару кувирком! Шкоди немає!");
                    } else {
                        playerHp -= bossDamage;
                        System.out.println("Робо-Бос завдає удару механічним кулаком! "
                                + brawlerName + " отримує " + bossDamage + " шкоди!");
                    }
                }
                if (playerHp < 0) {
                    playerHp = 0;
                }
            }
 
            if (playerHp > 0 && bossHp > 0) {
                ammo = Math.min(MAX_AMMO, ammo + 1);
            }
            System.out.println("-------------------------------------------------");
        }
 
        System.out.println();
        System.out.println("======================================================");
        if (bossHp <= 0) {
            System.out.println(" ПЕРЕМОГА! РОБО-БОСА ЗНИЩЕНО!");
            System.out.println("Раундів зіграно: " + round);
            System.out.println("Зібрано Power Cubes: " + cubes + " шт.");
            System.out.println("Нагорода: +10 Трофеїв , 100 Starr Drops! Ви - Зірковий Гравець!");
        } else {
            System.out.println(" ПОРАЗКА! Сейф зруйновано, " + brawlerName + " полеглий...");
            System.out.println("Раундів зіграно: " + round);
            if (brawlerChoice != 3) {
                System.out.println("Порада: спробуйте витривалішого бійця - Ель Прімо (HP 6000)!");
            } else {
                System.out.println("Порада: частіше використовуйте Супер і шукайте Power Cubes!");
            }
        }
        System.out.println("======================================================");
 
        scanner.close();
    }
 
    private static int getBossPhase(int hp, int maxHp) {
        if (hp * 100 > maxHp * 60) {
            return 1;
        } else if (hp * 100 >= maxHp * 30) {
            return 2;
        } else {
            return 3;
        }
    }
 
    private static String buildBar(int current, int max) {
        int safeCurrent = Math.max(0, Math.min(current, max));
        int bars = (int) (((double) safeCurrent / max) * 10);
        String bar = "[";
        for (int i = 0; i < bars; i++) {
            bar += "█";
        }
        for (int i = 0; i < 10 - bars; i++) {
            bar += "░";
        }
        return bar + "]";
    }

}
