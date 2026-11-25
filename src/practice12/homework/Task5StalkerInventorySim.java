package practice12.homework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

abstract class ZoneItem {
    private final String id;
    private final String name;
    private final double weightKg;

    public ZoneItem(String id, String name, double weightKg) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Вага має бути більшою за 0");
        }
        this.id = id;
        this.name = name;
        this.weightKg = weightKg;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public abstract String getItemType();

    public abstract boolean isUsable();

    public abstract String getUnusableReason();

    public abstract String getDescription();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZoneItem)) return false;
        ZoneItem other = (ZoneItem) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

class Artifact extends ZoneItem {
    private final double radiation;
    private final int protection;

    public Artifact(String id, String name, double weightKg, double radiation, int protection) {
        super(id, name, weightKg);
        this.radiation = radiation;
        this.protection = protection;
    }

    public double getRadiation() {
        return radiation;
    }

    public int getProtection() {
        return protection;
    }

    @Override
    public String getItemType() {
        return "Артефакт";
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public String getUnusableReason() {
        return "";
    }

    @Override
    public String getDescription() {
        return getName();
    }
}

class Medkit extends ZoneItem {
    private int usesLeft;

    public Medkit(String id, String name, double weightKg, int usesLeft) {
        super(id, name, weightKg);
        this.usesLeft = usesLeft;
    }

    public boolean use() {
        if (usesLeft > 0) {
            usesLeft--;
            return true;
        }
        return false;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    @Override
    public String getItemType() {
        return "Аптечка";
    }

    @Override
    public boolean isUsable() {
        return usesLeft > 0;
    }

    @Override
    public String getUnusableReason() {
        return "непридатна: " + usesLeft + " зарядів";
    }

    @Override
    public String getDescription() {
        return getName() + " (" + usesLeft + " заряди)";
    }
}

class Detector extends ZoneItem {
    private final int batteryPercent;

    public Detector(String id, String name, double weightKg, int batteryPercent) {
        super(id, name, weightKg);
        if (batteryPercent < 0 || batteryPercent > 100) {
            throw new IllegalArgumentException("Заряд батареї має бути в межах 0..100");
        }
        this.batteryPercent = batteryPercent;
    }

    public int getBatteryPercent() {
        return batteryPercent;
    }

    @Override
    public String getItemType() {
        return "Детектор";
    }

    @Override
    public boolean isUsable() {
        return batteryPercent > 0;
    }

    @Override
    public String getUnusableReason() {
        return "непридатний: батарея " + batteryPercent + "%";
    }

    @Override
    public String getDescription() {
        return getName() + " (" + batteryPercent + "%)";
    }
}

class Stalker {
    public static final int MAX_BELT_SLOTS = 4;

    private String callSign;
    private HashSet<Artifact> artifactBelt = new HashSet<>();
    private ArrayList<ZoneItem> backpack = new ArrayList<>();
    private HashSet<String> visitedLocations;

    public Stalker(String callSign, HashSet<String> visitedLocations) {
        this.callSign = callSign;
        this.visitedLocations = new HashSet<>(visitedLocations);
    }

    public String getCallSign() {
        return callSign;
    }

    public boolean equipArtifact(Artifact art) {
        if (artifactBelt.size() >= MAX_BELT_SLOTS) {
            return false;
        }
        return artifactBelt.add(art);
    }

    public void addToBackpack(ZoneItem item) {
        backpack.add(item);
    }

    public int cleanBackpack() {
        int removed = 0;
        Iterator<ZoneItem> it = backpack.iterator();
        while (it.hasNext()) {
            ZoneItem item = it.next();
            if (!item.isUsable()) {
                System.out.printf("  🗑 Викинуто %-11s %s (%s)%n",
                        "[" + item.getItemType() + "]:", item.getName(), item.getUnusableReason());
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    public int getBeltSize() {
        return artifactBelt.size();
    }

    public int getBackpackSize() {
        return backpack.size();
    }

    public String getBackpackDescription() {
        StringBuilder sb = new StringBuilder();
        for (ZoneItem item : backpack) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(item.getDescription());
        }
        return sb.toString();
    }

    public double getTotalWeightKg() {
        double sum = 0;
        for (Artifact a : artifactBelt) {
            sum += a.getWeightKg();
        }
        for (ZoneItem item : backpack) {
            sum += item.getWeightKg();
        }
        return sum;
    }

    public double getNetRadiation() {
        double sum = 0;
        for (Artifact a : artifactBelt) {
            sum += a.getRadiation();
        }
        return sum;
    }

    public int getTotalProtection() {
        int sum = 0;
        for (Artifact a : artifactBelt) {
            sum += a.getProtection();
        }
        return sum;
    }

    public HashSet<String> getVisitedLocationsCopy() {
        return new HashSet<>(visitedLocations);
    }

    public HashSet<String> getCommonLocations(HashSet<String> ally) {
        HashSet<String> result = new HashSet<>(visitedLocations);
        result.retainAll(ally);
        return result;
    }

    public HashSet<String> getNewLocationsFromAlly(HashSet<String> ally) {
        HashSet<String> result = new HashSet<>(ally);
        result.removeAll(visitedLocations);
        return result;
    }
}

public class Task5StalkerInventorySim {

    private static void equip(Stalker stalker, Artifact art) {
        if (stalker.equipArtifact(art)) {
            System.out.printf(Locale.US, "  ✅ Надіто: %-6s [%.1f кг | Радіація: %+.1f мЗв/хв | Захист: %+d%%]%n",
                    art.getName(), art.getWeightKg(), art.getRadiation(), art.getProtection());
        } else {
            System.out.println("  ⚠ Відхилено (дублікат!): " + art.getName() + " (" + art.getId() + ") вже є на поясі!");
        }
    }

    public static void main(String[] args) {
        HashSet<String> skifLocations = new HashSet<>();
        skifLocations.add("Залісся");
        skifLocations.add("Кордон");
        skifLocations.add("Звалище");
        skifLocations.add("Янтар");

        HashSet<String> allyLocations = new HashSet<>();
        allyLocations.add("Залісся");
        allyLocations.add("Янтар");
        allyLocations.add("Росток");
        allyLocations.add("Прип'ять");

        Stalker skif = new Stalker("Скіф", skifLocations);

        System.out.println("================ ☢ S.T.A.L.K.E.R. 2: СПОРЯДЖЕННЯ СТАЛКЕРА ☢ ================");
        System.out.println("Сталкер: " + skif.getCallSign() + " | Максимум слотів на поясі: " + Stalker.MAX_BELT_SLOTS);

        System.out.println();
        System.out.println("--- 1. Пояс артефактів (HashSet) ---");
        equip(skif, new Artifact("ART-1", "Медуза", 0.4, -2.0, 15));
        equip(skif, new Artifact("ART-2", "Душа", 0.5, 3.5, 25));
        equip(skif, new Artifact("ART-1", "Медуза", 0.4, -2.0, 15));
        equip(skif, new Artifact("ART-3", "Компас", 0.6, 2.5, 40));

        Medkit usual = new Medkit("MED-1", "Звичайна аптечка", 0.3, 1);
        usual.use();
        skif.addToBackpack(usual);
        skif.addToBackpack(new Medkit("MED-2", "Армійська аптечка", 0.4, 2));
        skif.addToBackpack(new Detector("DET-1", "Детектор Відгук", 0.5, 0));
        skif.addToBackpack(new Detector("DET-2", "Детектор Велес", 0.7, 85));

        System.out.println();
        System.out.println("--- 2. Ревізія рюкзака через Iterator.remove() ---");
        int removed = skif.cleanBackpack();
        System.out.println("  Підсумок ревізії: видалено " + removed + " предмети, у рюкзаку залишилось "
                + skif.getBackpackSize() + ".");

        System.out.println();
        System.out.println("--- 3. Загальний стан сталкера ---");
        System.out.printf(Locale.US, "  • Пояс: %d артефакти | Сумарний захист: %d%% | Радіація: %+.2f мЗв/хв%n",
                skif.getBeltSize(), skif.getTotalProtection(), skif.getNetRadiation());
        System.out.println("  • Рюкзак: " + skif.getBackpackDescription());
        System.out.printf(Locale.US, "  • Загальна вага спорядження: %.2f кг%n", skif.getTotalWeightKg());

        System.out.println();
        System.out.println("--- 4. Синхронізація КПК (HashSet) ---");
        HashSet<String> mine = skif.getVisitedLocationsCopy();
        System.out.println("  Локації Скіфа [" + mine.size() + "]:          " + mine);
        System.out.println("  Локації союзника [" + allyLocations.size() + "]:       " + allyLocations);
        System.out.println("  Спільні локації (A ∩ B):    " + skif.getCommonLocations(allyLocations));
        System.out.println("  Нові цілі розвідки (B \\ A): " + skif.getNewLocationsFromAlly(allyLocations));
        System.out.println("==========================================================================");
    }
}