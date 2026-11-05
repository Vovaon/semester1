package practice09.homework;

import java.util.Scanner;
class BioMonitor {
    private int hp;
    private final int maxHp;
    private int humanity;
    private boolean cyberpsychosis;

    public BioMonitor(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.humanity = 100;
        this.cyberpsychosis = false;
    }

    public void reduceHumanity(int cost) {
        this.humanity = Math.max(0, this.humanity - cost);
        if (this.humanity < 20) {
            this.cyberpsychosis = true;
        }
    }

    public void injectImmunoblockers(int restoreAmount) {
        this.humanity = Math.min(100, this.humanity + restoreAmount);
        if (this.humanity >= 20) {
            this.cyberpsychosis = false;
        }
    }

    public int applyDamage(int rawDamage, int totalArmor) {
        int actualDamage = Math.max(5, rawDamage - totalArmor);
        this.hp = Math.max(0, this.hp - actualDamage);
        return actualDamage;
    }

    public boolean isCyberpsychosis() {
        return cyberpsychosis;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHumanity() {
        return humanity;
    }

    @Override
    public String toString() {
        int totalBlocks = 10;
        double hpRatio = (double) hp / maxHp;
        int filledBlocks = (int) Math.round(hpRatio * totalBlocks);

        StringBuilder hpBar = new StringBuilder("[");
        for (int i = 0; i < totalBlocks; i++) {
            if (i < filledBlocks) {
                hpBar.append("█");
            } else {
                hpBar.append("░");
            }
        }
        hpBar.append("]");

        String status = cyberpsychosis ? "[ CYBERPSYCHOSIS ]" : "(Стабільний)";
        return "BioMonitor: HP " + hpBar + " " + hp + "/" + maxHp
                + " | Humanity: " + humanity + "/100 " + status;
    }
}
        abstract class AbstractCyberImplant {
            private final String name;
            private final String slot;
            private final int humanityCost;

            public AbstractCyberImplant(String name, String slot, int humanityCost) {
                this.name = name;
                this.slot = slot;
                this.humanityCost = humanityCost;
            }

            public String getName() {
                return name;
            }

            public String getSlot() {
                return slot;
            }

            public int getHumanityCost() {
                return humanityCost;
            }

            public abstract int getAttackBonus();
            public abstract int getArmorBonus();
            public abstract String activateSpecialAbility();

            @Override
            public String toString() {
                return "[" + slot + "] " + name + " (+" + getAttackBonus()
                        + " ATK, +" + getArmorBonus() + " DEF, -" + humanityCost + " Hum)";
            }
        }

        class SandevistanOs extends AbstractCyberImplant {
            public SandevistanOs() {
                super("Militech Sandevistan Apogee", "Нейро-ОС", 35);
            }

            @Override
            public int getAttackBonus() {
                return 90;
            }

            @Override
            public int getArmorBonus() {
                return 20;
            }

            @Override
            public String activateSpecialAbility() {
                return "⚡ САНДЕВІСТАН АКТИВОВАНО: Час сповільнено на 85%! Вороги завмерли на місці!";
            }
        }

        class MantisBlades extends AbstractCyberImplant {
            public MantisBlades() {
                super("Термальні Клинки Богомола", "Руки", 25);
            }

            @Override
            public int getAttackBonus() {
                return 110;
            }

            @Override
            public int getArmorBonus() {
                return 5;
            }

            @Override
            public String activateSpecialAbility() {
                return "🔥 Вихор розпечених клинків Богомола розтинає броню ворога!";
            }
        }

        class SubdermalTitaniumArmor extends AbstractCyberImplant {
            public SubdermalTitaniumArmor() {
                super("Підшкірна Титанова Броня", "Шкіра", 25);
            }

            @Override
            public int getAttackBonus() {
                return 10;
            }

            @Override
            public int getArmorBonus() {
                return 65;
            }

            @Override
            public String activateSpecialAbility() {
                return "🛡️ Нано-пластини поглинають кінетичний удар!";
            }
        }

        class CyberMercenary {
            private final String nickname;
            private final int baseAttack;
            private final BioMonitor bioMonitor;
            private final AbstractCyberImplant[] implants;
            private int implantCount;

            public CyberMercenary(String nickname, int baseAttack, int maxHp) {
                this.nickname = nickname;
                this.baseAttack = baseAttack;
                this.bioMonitor = new BioMonitor(maxHp);
                this.implants = new AbstractCyberImplant[4];
                this.implantCount = 0;
            }

            public String getNickname() {
                return nickname;
            }

            public BioMonitor getBioMonitor() {
                return bioMonitor;
            }

            public boolean installImplant(AbstractCyberImplant implant) {
                for (int i = 0; i < implantCount; i++) {
                    if (implants[i].getSlot().equalsIgnoreCase(implant.getSlot())) {
                        System.out.println("❌ Помилка: Слот [" + implant.getSlot()
                                + "] вже зайнятий імплантом '" + implants[i].getName() + "'!");
                        return false;
                    }
                }

                if (implantCount >= implants.length) {
                    System.out.println("❌ Помилка: Всі слоти для імплантів заповнені!");
                    return false;
                }

                implants[implantCount++] = implant;
                bioMonitor.reduceHumanity(implant.getHumanityCost());

                System.out.println("⚡ Встановлено [" + implant.getSlot() + "]: " + implant.getName()
                        + " (-" + implant.getHumanityCost() + " Людяності -> залишок: " + bioMonitor.getHumanity() + ")");

                if (bioMonitor.isCyberpsychosis()) {
                    System.out.println("⚠️ [КРИТИЧНЕ ПОПЕРЕДЖЕННЯ BIO-MONITOR]: Людяність впала до "
                            + bioMonitor.getHumanity() + "/100! Стан: КІБЕРПСИХОЗ (Атака x1.5, самоушкодження!)");
                }

                return true;
            }

            public int getTotalAttack() {
                int bonusAttack = 0;
                for (int i = 0; i < implantCount; i++) {
                    bonusAttack += implants[i].getAttackBonus();
                }
                int total = baseAttack + bonusAttack;

                if (bioMonitor.isCyberpsychosis()) {
                    total = (int) (total * 1.5);
                }
                return total;
            }

            public int getTotalArmor() {
                int armor = 0;
                for (int i = 0; i < implantCount; i++) {
                    armor += implants[i].getArmorBonus();
                }
                return armor;
            }

            public void triggerSpecialAbilities() {
                if (implantCount == 0) {
                    System.out.println("⚠️ Немає встановлених імплантів для активації!");
                    return;
                }
                for (int i = 0; i < implantCount; i++) {
                    System.out.println(implants[i].activateSpecialAbility());
                }
            }

            public void endTurnProcessing() {
                if (bioMonitor.isCyberpsychosis()) {
                    int selfDamage = (int) (bioMonitor.getMaxHp() * 0.10);
                    bioMonitor.applyDamage(selfDamage, 0);
                    System.out.println("⚠️ [КІБЕРПСИХОЗ]: " + nickname + " отримує "
                            + selfDamage + " шкоди нервовій системі (10% від HP)!");
                }
            }

            @Override
            public String toString() {
                int implantBonusAtk = 0;
                for (int i = 0; i < implantCount; i++) {
                    implantBonusAtk += implants[i].getAttackBonus();
                }

                StringBuilder sb = new StringBuilder();
                sb.append("CyberMercenary{nick='").append(nickname)
                        .append("', TotalATK=").append(getTotalAttack())
                        .append(" (База: ").append(baseAttack).append(" + Імпланти: ").append(implantBonusAtk);
                if (bioMonitor.isCyberpsychosis()) {
                    sb.append(", лють x1.5");
                }
                sb.append("), TotalDEF=").append(getTotalArmor()).append("}\n");
                sb.append(" ├── ").append(bioMonitor.toString()).append("\n");
                sb.append(" └── Встановлений Хром (").append(implantCount).append("/").append(implants.length).append("):\n");

                for (int i = 0; i < implantCount; i++) {
                    sb.append("     ").append(i + 1).append(". ").append(implants[i].toString());
                    if (i < implantCount - 1) {
                        sb.append("\n");
                    }
                }
                return sb.toString();
            }
        }

        public class Task5CyberpunkRipperdocSim {

            public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("======================= CYBERPUNK 2077: КЛІНІКА РІППЕРДОКА 💉 =======================");
                System.out.print("Введіть позивний найманця (за замовчуванням 'Девід Мартінес'): ");
                String nameInput = scanner.nextLine().trim();
                String name = nameInput.isEmpty() ? "Девід Мартінес" : nameInput;

                CyberMercenary hero = new CyberMercenary(name, 120, 1000);
                System.out.println("\nПацієнт на операційному столі: " + hero.getNickname());
                System.out.println(hero.getBioMonitor());
                boolean shopping = true;
                while (shopping) {
                    System.out.println("\n--- ВІТРИНА РІППЕРДОКА ---");
                    System.out.println("1. Встановити [Нейро-ОС] Militech Sandevistan Apogee (-35 Humanity)");
                    System.out.println("2. Встановити [Руки] Термальні Клинки Богомола (-25 Humanity)");
                    System.out.println("3. Встановити [Шкіра] Підшкірна Титанова Броня (-25 Humanity)");
                    System.out.println("4. Спробувати повторно встановити дублікат [Нейро-ОС] (Тест помилки слота)");
                    System.out.println("5. Завершити операцію та вирушити на бій");
                    System.out.print("Оберіть дію (1-5): ");

                    String choice = scanner.nextLine().trim();
                    switch (choice) {
                        case "1":
                            hero.installImplant(new SandevistanOs());
                            break;
                        case "2":
                            hero.installImplant(new MantisBlades());
                            break;
                        case "3":
                            hero.installImplant(new SubdermalTitaniumArmor());
                            break;
                        case "4":
                            hero.installImplant(new SandevistanOs());
                            break;
                        case "5":
                            shopping = false;
                            break;
                        default:
                            System.out.println("⚠️ Невірний вибір. Спробуйте ще раз.");
                    }
                }

                System.out.println("\n=================== ДОСЬЄ НАЙМАНЦЯ ПЕРЕД БОЄМ ===================");
                System.out.println(hero);
                System.out.println("\n⚔️ БІЙ НА ВЕРШИНІ ARASAKA TOWER ПРОТИ АДАМА СМЕШЕРА ⚔️");
                int smasherHp = 1200;
                int smasherMaxHp = 1200;
                int smasherArmor = 80;
                int smasherAttack = 150;

                int round = 1;
                while (hero.getBioMonitor().getHp() > 0 && smasherHp > 0) {
                    System.out.println("\n================ РАУНД " + round + " ================");
                    System.out.println("Стан ворога: Адам Смешер [HP " + smasherHp + "/" + smasherMaxHp + " | DEF " + smasherArmor + "]");
                    System.out.println("Ваш стан: " + hero.getBioMonitor());

                    System.out.println("\nОберіть дію на цей хід:");
                    System.out.println("1. Атакувати Адама Смешера");
                    System.out.println("2. Активувати спец-здібності імплантів");
                    System.out.println("3. Ввести Імуноблокатор (+30 Людяності)");
                    System.out.println("4. Подивитися повне досьє персонажа");
                    System.out.print("Ваш вибір (1-4): ");

                    String combatChoice = scanner.nextLine().trim();
                    System.out.println();

                    switch (combatChoice) {
                        case "1":
                            int totalAttack = hero.getTotalAttack();
                            int damageToSmasher = Math.max(5, totalAttack - smasherArmor);
                            smasherHp = Math.max(0, smasherHp - damageToSmasher);
                            System.out.println("💥 " + hero.getNickname() + " завдає " + totalAttack
                                    + " шкоди! Адам Смешер втрачає " + damageToSmasher + " HP!");
                            break;

                        case "2":
                            hero.triggerSpecialAbilities();
                            int specAttack = hero.getTotalAttack();
                            int specDamage = Math.max(5, specAttack - smasherArmor);
                            smasherHp = Math.max(0, smasherHp - specDamage);
                            System.out.println("💥 Атака після спец-здібностей завдає " + specDamage + " шкоди Смешеру!");
                            break;

                        case "3":
                            hero.getBioMonitor().injectImmunoblockers(30);
                            System.out.println("💉 Введено Імуноблокатор! Людяність відновлено до "
                                    + hero.getBioMonitor().getHumanity() + "/100.");
                            break;

                        case "4":
                            System.out.println(hero);
                            continue;

                        default:
                            System.out.println("⚠️ Невірний вибір. Хід пропущено!");
                    }

                    if (smasherHp > 0) {
                        int damageTaken = hero.getBioMonitor().applyDamage(smasherAttack, hero.getTotalArmor());
                        System.out.println("🤖 Адам Смешер контратакує! Завдано " + damageTaken + " шкоди!");
                    } else {
                        System.out.println("\n🏆 АДАМ СМЕШЕР ПОВАЛЕНИЙ! " + hero.getNickname() + " ПЕРЕМАГАЄ У БОЮ!");
                        break;
                    }

                    hero.endTurnProcessing();

                    if (hero.getBioMonitor().getHp() <= 0) {
                        System.out.println("\n💀 " + hero.getNickname() + " загинув у бою...");
                        break;
                    }

                    round++;
                }

                scanner.close();
            }
        }
