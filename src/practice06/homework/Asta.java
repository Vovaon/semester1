package practice06.homework;

public class Asta {
    private String name = "Аста";
    private int hp = 1000;
    private int maxHp = 1000;
    private int antiMagicEnergy = 20;
    private int baseAttack = 120;
    private int defense = 35;
    private boolean isBlackFormActive = false;
    private String[] skills = {
            "Удар Демоноборця",
            "Чорний Ураган",
            "Метеорит Демона",
            "Форма Чорного Демона"
    };


    private final int[] skillCost = {25, 50, 75, 100};
    private final int[] skillDamage = {220, 380, 550, 0};
    private boolean limitsPushed = false;
    private boolean guarding = false;
    public void showStatus() {
        int percent = hp * 100 / maxHp;
        int filled = hp * 10 / maxHp;
        int energyFilled = antiMagicEnergy / 10;

        System.out.println("----------- СТАТУС -----------");
        System.out.println("Ім'я: " + name);
        System.out.println("Здоров'я: [" + "█".repeat(filled) + ".".repeat(10 - filled)
                + "] " + percent + "% (" + hp + "/" + maxHp + ")");
        System.out.println("Антимагія: [" + "█".repeat(energyFilled) + ".".repeat(10 - energyFilled)
                + "] " + antiMagicEnergy + "/100");
        System.out.println("Форма Чорного Демона: " + (isBlackFormActive ? "АКТИВНА" : "неактивна"));
        System.out.println("------------------------------");
    }

    public int basicSwordAttack() {
        int damage = isBlackFormActive ? baseAttack * 2 : baseAttack;
        antiMagicEnergy = Math.min(100, antiMagicEnergy + 20);
        System.out.println("💥 " + name + " розсікає простір мечем Демоноборця! Завдано " + damage + " шкоди.");
        System.out.println("⚡ Шкала антимагії збільшилася: " + antiMagicEnergy + " / 100!");
        return damage;
    }

    public int useSkill(int skillIndex) {
        if (skillIndex < 0 || skillIndex >= skills.length) {
            System.out.println("⚠️ Невірний номер навички!");
            return 0;
        }

        int cost = skillCost[skillIndex];
        if (antiMagicEnergy < cost) {
            System.out.println("⚠️ Недостатньо антимагії для «" + skills[skillIndex] + "»! Потрібно "
                    + cost + ", а є " + antiMagicEnergy + ".");
            return 0;
        }

        if (skillIndex == 3) {
            if (isBlackFormActive) {
                System.out.println("⚠️ Форма Чорного Демона вже активна!");
                return 0;
            }
            antiMagicEnergy -= cost;
            isBlackFormActive = true;
            hp = Math.min(maxHp, hp + 300);
            System.out.println("🖤 " + name + " активує «" + skills[3] + "»! Відновлено 300 HP, "
                    + "сила всіх атак подвоєна!");
            return 0;
        }

        antiMagicEnergy -= cost;
        int damage = isBlackFormActive ? skillDamage[skillIndex] * 2 : skillDamage[skillIndex];
        System.out.println("🌀 " + name + " використовує «" + skills[skillIndex] + "»! Завдано "
                + damage + " шкоди.");
        return damage;
    }

    public void takeDamage(int incomingDmg) {
        int realDamage = incomingDmg - defense;
        if (guarding) {
            realDamage = realDamage / 2;
            guarding = false;
        }
        if (realDamage < 15) {
            realDamage = 15;
        }
        hp -= realDamage;
        if (hp <= 0) {
            hp = 0;
        }
        System.out.println("🛡️ Меч частково блокує магію! " + name + " отримує " + realDamage + " чистої шкоди.");
    }


    public void defend() {
        guarding = true;
        antiMagicEnergy = Math.min(100, antiMagicEnergy + 10);
        System.out.println("🛡️ " + name + " піднімає меч у захисній стійці! Антимагія: "
                + antiMagicEnergy + " / 100.");
    }

    public void pushLimits() {
        if (!limitsPushed && hp > 0 && hp < maxHp * 25 / 100) {
            limitsPushed = true;
            hp = Math.min(maxHp, hp + 150);
            antiMagicEnergy = Math.min(100, antiMagicEnergy + 50);
            System.out.println("🔥 «Моя магія — ніколи не здаватися!» " + name
                    + " відновлює 150 HP та отримує +50 антимагії!");
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAntiMagicEnergy() {
        return antiMagicEnergy;
    }

    public String[] getSkills() {
        return skills;
    }

    public int getSkillCost(int skillIndex) {
        return skillCost[skillIndex];
    }
}
