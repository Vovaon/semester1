package practice08.homework;

import java.util.StringJoiner;

class DungeonEntity {
    private String name;
    private int level;
    protected int hp;
    protected int maxHp;
    protected int baseAttack;
    protected int defense;

    public DungeonEntity(String name, int level, int maxHp, int baseAttack, int defense) {
        this.name = (name == null || name.isBlank()) ? "Невідомий" : name;
        this.level = Math.max(1, level);
        this.maxHp = Math.max(1, maxHp);
        this.hp = this.maxHp;
        this.baseAttack = Math.max(0, baseAttack);
        this.defense = Math.max(0, defense);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    protected void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getDefense() {
        return defense;
    }

    public int takeDamage(int rawDamage) {
        int actual = Math.max(5, rawDamage - defense);
        hp = Math.max(0, hp - actual);
        return actual;
    }

    public int performAttack() {
        return baseAttack;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    protected String buildHealthBar() {
        int filled = (int) Math.round(10.0 * hp / maxHp);
        if (hp > 0 && filled == 0) {
            filled = 1;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            sb.append(i < filled ? '█' : '░');
        }
        return sb.append("]").toString();
    }

    @Override
    public String toString() {
        return String.format("DungeonEntity{name='%s', lvl=%d, HP=%s %d/%d, ATK=%d, DEF=%d}",
                name, level, buildHealthBar(), hp, maxHp, baseAttack, defense);
    }
}

class Hunter extends DungeonEntity {
    private char rank;
    protected int mana;
    protected int maxMana;

    public Hunter(String name, int level, int maxHp, int baseAttack, int defense,
                  char rank, int maxMana) {
        super(name, level, maxHp, baseAttack, defense);
        this.rank = "EDCBAS".indexOf(rank) >= 0 ? rank : 'E';
        this.maxMana = Math.max(0, maxMana);
        this.mana = this.maxMana;
    }

    public char getRank() {
        return rank;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }
    public void restoreMana() {
        mana = maxMana;
    }

    public int useManaSkill(String skillName, int manaCost, double multiplier) {
        if (mana >= manaCost) {
            mana -= manaCost;
            int damage = (int) (performAttack() * multiplier);
            System.out.println("🌀 " + getName() + " використовує навичку «" + skillName
                    + "» (x" + multiplier + ", -" + manaCost + " MP): " + damage + " шкоди!");
            return damage;
        }
        System.out.println("❌ Недостатньо мани для навички «" + skillName + "»! Звичайний удар.");
        return performAttack();
    }

    @Override
    public String toString() {
        return "Hunter{" + super.toString() + " | Ранг: [" + rank + "] | MP: "
                + mana + "/" + maxMana + "}";
    }
}

class DungeonBoss extends DungeonEntity {
    private String bossTitle;
    private boolean enraged;

    public DungeonBoss(String name, int level, int maxHp, int baseAttack, int defense,
                       String bossTitle) {
        super(name, level, maxHp, baseAttack, defense);
        this.bossTitle = (bossTitle == null || bossTitle.isBlank()) ? "Безіменний" : bossTitle;
        this.enraged = false;
    }

    public boolean isEnraged() {
        return enraged;
    }

    @Override
    public int takeDamage(int rawDamage) {
        int actual = super.takeDamage(rawDamage);
        if (hp > 0 && hp <= maxHp * 0.4 && !enraged) {
            enraged = true;
            System.out.println("🔥 БОС ВПАДАЄ У ФАЗУ ЛЮТІ! Атака зросла на 50%!");
        }
        return actual;
    }

    @Override
    public int performAttack() {
        if (enraged) {
            return (int) (super.performAttack() * 1.5);
        }
        return super.performAttack();
    }

    @Override
    public String toString() {
        return "DungeonBoss{" + super.toString() + " | Титул: '" + bossTitle
                + "' | Лють: " + enraged + "}";
    }
}

class ShadowMonarch extends Hunter {
    private static final int ARISE_COST = 30;

    private String[] shadowNames = new String[5];
    private int[] shadowBonuses = new int[5];
    private int shadowCount = 0;

    public ShadowMonarch(String name, int level, int maxHp, int maxMana,
                         int baseAttack, int defense) {
        super(name, level, maxHp, baseAttack, defense, 'S', maxMana);
    }

    public int getShadowCount() {
        return shadowCount;
    }

    public boolean arise(String shadowName, int bonusAtk) {
        if (shadowCount >= shadowNames.length) {
            System.out.println("❌ Тіньовий Легіон переповнений (" + shadowCount + "/"
                    + shadowNames.length + ")!");
            return false;
        }
        if (mana < ARISE_COST) {
            System.out.println("❌ Недостатньо мани для призову (потрібно " + ARISE_COST + " MP)!");
            return false;
        }
        mana -= ARISE_COST;
        System.out.println("⚡ " + getName() + " закликає нову тінь: «ПОВСТАНЬ! (ARISE!)» (-"
                + ARISE_COST + " MP)");
        shadowNames[shadowCount] = shadowName;
        shadowBonuses[shadowCount] = Math.max(0, bonusAtk);
        shadowCount++;
        System.out.println("🟣 Тінь [" + shadowName + " (+" + bonusAtk
                + " ATK)] приєдналася до легіону! (Тіней: " + shadowCount + "/"
                + shadowNames.length + ")");
        return true;
    }

    public int getTotalShadowBonus() {
        int sum = 0;
        for (int i = 0; i < shadowCount; i++) {
            sum += shadowBonuses[i];
        }
        return sum;
    }

    @Override
    public int performAttack() {
        return super.performAttack() + getTotalShadowBonus();
    }

    public void levelUp() {
        setLevel(getLevel() + 1);
        maxHp += 150;
        hp = maxHp;
        maxMana += 50;
        mana = maxMana;
        baseAttack += 25;
        System.out.println("🔔 [СИСТЕМА: РІВЕНЬ ПІДВИЩЕНО!] Новий рівень: " + getLevel() + "!");
    }

    @Override
    public String toString() {
        StringJoiner legion = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < shadowCount; i++) {
            legion.add((i + 1) + ". " + shadowNames[i] + " (+" + shadowBonuses[i] + " ATK)");
        }
        return "ShadowMonarch{" + super.toString() + "}\n"
                + "   👥 Тіньовий Легіон (" + shadowCount + "/" + shadowNames.length + "): "
                + (shadowCount == 0 ? "[порожньо]" : legion.toString())
                + " | Сумарний бонус тіней: +" + getTotalShadowBonus() + " ATK";
    }
}

public class Task5ShadowMonarchRpg {
    public static void main(String[] args) {
        System.out.println("=============== ⚔ SOLO LEVELING: ПІДЗЕМЕЛЛЯ S-РАНГУ ⚔ ===============");

        ShadowMonarch monarch = new ShadowMonarch("Сон Джін-Ву", 45, 1200, 300, 180, 45);
        DungeonBoss beru = new DungeonBoss("Король Мурах Беру", 80, 2500, 260, 60,
                "Кошмар Острова Чеджу");

        System.out.println("[ПІДГОТОВКА ДО РЕЙДУ]");
        monarch.arise("Лицар Ігріс", 90);
        monarch.restoreMana();
        System.out.println();

        System.out.println("[ДОСЬЄ МИСЛИВЦЯ]:");
        System.out.println(monarch);
        System.out.println();
        System.out.println("[БОС КАМЕРИ]:");
        System.out.println(beru);
        System.out.println();

        String[] newShadowNames = {"Залізний Танк", "Маг Ігніс", "Лучник Кіба"};
        int[] newShadowBonuses = {70, 80, 60};

        int round = 1;
        while (monarch.isAlive() && beru.isAlive()) {
            System.out.println("--- РАУНД " + round + " ---");

            if (round <= newShadowNames.length) {
                monarch.arise(newShadowNames[round - 1], newShadowBonuses[round - 1]);
            }

            int damage;
            if (round % 2 == 0) {
                damage = monarch.useManaSkill("Імператорська Атака", 60, 2.0);
            } else {
                damage = monarch.performAttack();
                System.out.println("🗡 " + monarch.getName() + " і його Тіньовий Легіон атакують! База: "
                        + monarch.getBaseAttack() + " + Бонус тіней: "
                        + monarch.getTotalShadowBonus() + " = " + damage + "!");
            }

            int dealt = beru.takeDamage(damage);
            System.out.println("💥 " + "Король Мурах Беру отримує " + dealt + " чистої шкоди! (Залишок HP: "
                    + beru.getHp() + "/" + beru.getMaxHp() + ")");

            if (beru.isAlive()) {
                int taken = monarch.takeDamage(beru.performAttack());
                System.out.println("🩸 Беру контратакує! " + monarch.getName() + " отримує " + taken
                        + " шкоди (HP: " + monarch.getHp() + "/" + monarch.getMaxHp() + ")");
            }
            System.out.println("---");
            round++;
        }

        if (monarch.isAlive()) {
            System.out.println("🏆 ПЕРЕМОГА! Боса Король Мурах Беру подолано!");
            monarch.levelUp();
            if (monarch.arise("Генерал Беру", 180)) {
                System.out.println("🟣 «ПОВСТАНЬ!» -> Генерал Беру (+180 ATK) став на коліно перед Монархом Тіней!");
            }
            System.out.println();
            System.out.println("[ПІДСУМКОВЕ ДОСЬЄ]:");
            System.out.println(monarch);
        } else {
            System.out.println("💀 Монарха Тіней переможено... Спробуйте ще раз.");
        }

        System.out.println("=========================================================================");
    }
}

