package practice10.homework;

interface FlyingMonster {
    boolean isAirborne();
    void knockDownToGround();
}

interface RegeneratingMonster {
    int regenerateHealth();
    void applyMoonDustBomb();
}

interface SpectralPhantom {
    boolean isIncorporeal();
    void bindWithYrdenSign();
}

abstract class AbstractMonster {
    private final String name;
    private final String bestiaryCategory;
    protected int hp;
    protected final int maxHp;

    public AbstractMonster(String name, String bestiaryCategory, int maxHp) {
        this.name = name;
        this.bestiaryCategory = bestiaryCategory;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public abstract int calculateMonsterAttack();
    public abstract String getBattleCry();

    public final boolean isAlive() {
        return hp > 0;
    }

    public void receiveDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    @Override
    public String toString() {
        return String.format("%s [%s | HP: %d/%d]", name, bestiaryCategory, hp, maxHp);
    }
}


class RoyalGriffin extends AbstractMonster implements FlyingMonster {
    private boolean airborne = true;

    public RoyalGriffin() {
        super("Королівський Грифон", "Гібриди", 400);
    }

    @Override
    public boolean isAirborne() { return airborne; }

    @Override
    public void knockDownToGround() {
        this.airborne = false;
        System.out.println("✦ [instanceof FlyingMonster] Ґеральт складає пальці у Знак ААРД! Грифона збито на землю!");
    }

    @Override
    public void receiveDamage(int dmg) {
        if (airborne) {
            System.out.println("  Грифон ширяє високо в небі! Удар мечем без підготовки завдав би лише 50% шкоди.");
            super.receiveDamage(dmg / 2);
        } else {
            super.receiveDamage(dmg);
        }
    }

    @Override
    public int calculateMonsterAttack() { return 40; }

    @Override
    public String getBattleCry() { return "Крррр-ааа!"; }
}

class Noonwraith extends AbstractMonster implements SpectralPhantom {
    private boolean incorporeal = true;

    public Noonwraith() {
        super("Полудниця з Білого Саду", "Примари", 300);
    }

    @Override
    public boolean isIncorporeal() { return incorporeal; }

    @Override
    public void bindWithYrdenSign() {
        this.incorporeal = false;
        System.out.println("✦ [instanceof SpectralPhantom] Ґеральт креслить на землі магічне коло Знаку ІРДЕН! Примару матеріалізовано!");
    }

    @Override
    public void receiveDamage(int dmg) {
        if (incorporeal) {
            System.out.println("  Примара нематеріальна (поглинає 80% ударів).");
            super.receiveDamage((int)(dmg * 0.2));
        } else {
            super.receiveDamage(dmg);
        }
    }

    @Override
    public int calculateMonsterAttack() { return 30; }

    @Override
    public String getBattleCry() { return "Спееекааа..."; }
}

class HigherVampire extends AbstractMonster implements FlyingMonster, RegeneratingMonster {
    private boolean airborne = true;
    private boolean regenBlocked = false;

    public HigherVampire() {
        super("Вищий Вампір Детлафф", "Вампіри", 650);
    }

    @Override
    public boolean isAirborne() { return airborne; }

    @Override
    public void knockDownToGround() {
        this.airborne = false;
        System.out.println("✦ [instanceof FlyingMonster] Телекінетичний імпульс Аарду перериває піке вампіра!");
    }

    @Override
    public int regenerateHealth() {
        if (!regenBlocked && isAlive()) {
            hp = Math.min(hp + 120, maxHp);
            return 120;
        }
        return 0;
    }

    @Override
    public void applyMoonDustBomb() {
        this.regenBlocked = true;
        System.out.println("✦ [instanceof RegeneratingMonster] Ґеральт підриває бомбу 'Місячний Пил'! Срібні ошурки блокують регенерацію!");
    }

    @Override
    public int calculateMonsterAttack() { return 80; }

    @Override
    public String getBattleCry() { return "Якщо ти віриш у богів, починай молитися!"; }
}

final class AncientLeshen extends AbstractMonster implements RegeneratingMonster {
    private boolean regenBlocked = false;

    public AncientLeshen() {
        super("Прадавній Лісовик", "Релікти", 800);
    }

    @Override
    public int regenerateHealth() {
        if (!regenBlocked && isAlive()) {
            hp = Math.min(hp + 50, maxHp);
            return 50;
        }
        return 0;
    }

    @Override
    public void applyMoonDustBomb() {
        this.regenBlocked = true;
        System.out.println("✦ [instanceof RegeneratingMonster] Бомба 'Місячний Пил' блокує відновлення здоров'я Лісовика від коріння дерев!");
    }

    @Override
    public int calculateMonsterAttack() { return 60; }

    @Override
    public String getBattleCry() { return "*Шум старого лісу та тріск гілок*"; }
}

class WitcherHunter {
    private final String name = "Ґеральт із Рівії";
    private final int silverSwordAtk = 250;

    public void applyBestiaryTactics(AbstractMonster target) {
        if (target instanceof SpectralPhantom sp && sp.isIncorporeal()) {
            sp.bindWithYrdenSign();
        }
        if (target instanceof FlyingMonster fm && fm.isAirborne()) {
            fm.knockDownToGround();
        }
        if (target instanceof RegeneratingMonster rm) {
            rm.applyMoonDustBomb();
        }
    }

    public final int executeSilverStrike(AbstractMonster target) {
        applyBestiaryTactics(target);
        System.out.printf("⚔ [final executeSilverStrike] Вихор срібного меча завдає %d шкоди! ", silverSwordAtk);
        target.receiveDamage(silverSwordAtk);
        System.out.printf("(Залишок HP: %d/%d)\n", target.hp, target.maxHp);
        return silverSwordAtk;
    }
}

public class Task5WitcherBestiaryAndMutagenSim {
    public static void main(String[] args) {
        System.out.println("================ 🐺 ВІДЬМАК 3: БЕСТІАРІЙ ТА ПОЛІМОРФНЕ ПОЛЮВАННЯ 🐺 ================");
        System.out.println("Мисливець: Ґеральт із Рівії (Школа Вовка | Срібний меч: 250 ATK | Інтенсивність Знаків: 150%)\n");

        AbstractMonster[] contracts = {
                new RoyalGriffin(),
                new Noonwraith(),
                new HigherVampire()
        };

        WitcherHunter geralt = new WitcherHunter();

        for (int i = 0; i < contracts.length; i++) {
            AbstractMonster monster = contracts[i];
            System.out.printf("--- КОНТРАКТ #%d: %s ---\n", (i + 1), monster.toString());
            if (monster instanceof RegeneratingMonster && monster instanceof FlyingMonster) {
                System.out.println("🩸 Множинний контракт (FlyingMonster + RegeneratingMonster)!");
            }
            if (monster instanceof SpectralPhantom) {
                monster.receiveDamage(250);
            }
            if (monster instanceof FlyingMonster fm && !(monster instanceof RegeneratingMonster)) {
                monster.receiveDamage(250);
            }

            geralt.executeSilverStrike(monster);
            System.out.println();
        }
    }
}
