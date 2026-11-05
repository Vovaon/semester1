package practice09.homework;

import java.util.Arrays;
import java.util.Locale;

class Motherboard {
    private final String model;
    private final String socketType;
    private final int maxExpansionSlots;

    public Motherboard(String model, String socketType, int maxExpansionSlots) {
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Модель не може бути порожньою");
        if (socketType == null || socketType.isBlank()) throw new IllegalArgumentException("Сокет не може бути порожнім");
        if (maxExpansionSlots < 1 || maxExpansionSlots > 8)
            throw new IllegalArgumentException("Кількість слотів має бути в межах 1..8");
        this.model = model;
        this.socketType = socketType;
        this.maxExpansionSlots = maxExpansionSlots;
    }

    public String getModel() { return model; }

    public String getSocketType() { return socketType; }

    public int getMaxExpansionSlots() { return maxExpansionSlots; }

    public boolean isSocketCompatible(String cpuSocket) {
        return socketType.equalsIgnoreCase(cpuSocket);
    }
}

class PowerSupplyUnit {
    private final String model;
    private final int maxOutputWatts;
    private final double efficiencyCoefficient;

    public PowerSupplyUnit(String model, int maxOutputWatts, double efficiencyCoefficient) {
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Модель не може бути порожньою");
        if (maxOutputWatts <= 0) throw new IllegalArgumentException("Потужність БЖ має бути > 0");
        if (efficiencyCoefficient < 0.80 || efficiencyCoefficient > 0.96)
            throw new IllegalArgumentException("КПД має бути в межах [0.80; 0.96]");
        this.model = model;
        this.maxOutputWatts = maxOutputWatts;
        this.efficiencyCoefficient = efficiencyCoefficient;
    }

    public String getModel() { return model; }

    public int getMaxOutputWatts() { return maxOutputWatts; }

    public double getEfficiencyCoefficient() { return efficiencyCoefficient; }

    public int getSafeContinuousWatts() {
        return (int) (maxOutputWatts * 0.85);
    }

    public double calculateWallPowerDraw(int internalLoadWatts) {
        return internalLoadWatts / efficiencyCoefficient;
    }
}

abstract class AbstractHardwareComponent {
    private final String modelName;
    private final double priceUah;

    public AbstractHardwareComponent(String modelName, double priceUah) {
        if (modelName == null || modelName.isBlank()) throw new IllegalArgumentException("Назва моделі порожня");
        if (priceUah < 0) throw new IllegalArgumentException("Ціна не може бути від'ємною");
        this.modelName = modelName;
        this.priceUah = priceUah;
    }

    public String getModelName() { return modelName; }

    public double getPriceUah() { return priceUah; }

    public abstract int getPowerWatts();

    public abstract double getBenchmarkScore();

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format(Locale.US, "[%s] %s (%d Вт | Score: %.1f)",
                getCategory(), modelName, getPowerWatts(), getBenchmarkScore());
    }
}

class ProcessorUnit extends AbstractHardwareComponent {
    private final String socket;
    private final int cores;
    private final double boostClockGhz;
    private final int tdpWatts;

    public ProcessorUnit(String modelName, double priceUah, String socket,
                         int cores, double boostClockGhz, int tdpWatts) {
        super(modelName, priceUah);
        if (socket == null || socket.isBlank()) throw new IllegalArgumentException("Сокет не може бути порожнім");
        if (cores <= 0 || boostClockGhz <= 0 || tdpWatts <= 0)
            throw new IllegalArgumentException("Параметри процесора мають бути > 0");
        this.socket = socket;
        this.cores = cores;
        this.boostClockGhz = boostClockGhz;
        this.tdpWatts = tdpWatts;
    }

    public String getSocket() { return socket; }

    @Override
    public int getPowerWatts() { return tdpWatts; }

    @Override
    public double getBenchmarkScore() { return cores * boostClockGhz * 120.0; }

    @Override
    public String getCategory() { return "CPU"; }
}

class GraphicsAccelerator extends AbstractHardwareComponent {
    private final int vramGb;
    private final int tensorCores;
    private final int boardPowerWatts;

    public GraphicsAccelerator(String modelName, double priceUah,
                               int vramGb, int tensorCores, int boardPowerWatts) {
        super(modelName, priceUah);
        if (vramGb <= 0 || tensorCores < 0 || boardPowerWatts <= 0)
            throw new IllegalArgumentException("Некоректні параметри відеокарти");
        this.vramGb = vramGb;
        this.tensorCores = tensorCores;
        this.boardPowerWatts = boardPowerWatts;
    }

    @Override
    public int getPowerWatts() { return boardPowerWatts; }

    @Override
    public double getBenchmarkScore() { return vramGb * 250.0 + tensorCores * 1.5; }

    @Override
    public String getCategory() { return "GPU"; }
}

class NvmeSsdDrive extends AbstractHardwareComponent {
    private final int capacityGb;
    private final int readSpeedMbSec;
    private final int[] speedSamples;

    public NvmeSsdDrive(String modelName, double priceUah,
                        int capacityGb, int readSpeedMbSec, int[] speedSamples) {
        super(modelName, priceUah);
        if (capacityGb <= 0 || readSpeedMbSec <= 0)
            throw new IllegalArgumentException("Некоректні параметри накопичувача");
        this.capacityGb = capacityGb;
        this.readSpeedMbSec = readSpeedMbSec;
        // Захисне копіювання у конструкторі
        this.speedSamples = (speedSamples != null) ? speedSamples.clone() : new int[0];
    }

    /** Захисне копіювання у геттері. */
    public int[] getSpeedSamples() {
        return speedSamples.clone();
    }

    @Override
    public int getPowerWatts() { return 12; }

    @Override
    public double getBenchmarkScore() { return readSpeedMbSec * 0.25; }

    @Override
    public String getCategory() { return "NVMe SSD"; }
}

class WorkstationPC {
    private final String buildName;
    private final Motherboard motherboard;
    private final PowerSupplyUnit psu;
    private final AbstractHardwareComponent[] components;
    private int componentCount;

    public WorkstationPC(String buildName, Motherboard motherboard, PowerSupplyUnit psu) {
        if (motherboard == null || psu == null)
            throw new IllegalArgumentException("Плата і БЖ обов'язкові");
        this.buildName = buildName;
        this.motherboard = motherboard;
        this.psu = psu;
        this.components = new AbstractHardwareComponent[motherboard.getMaxExpansionSlots()];
        this.componentCount = 0;
    }

    public boolean installComponent(AbstractHardwareComponent comp) {
        if (comp == null) {
            System.out.println("❌ Відмова: компонент не вказано!");
            return false;
        }
        // 1. Вільний слот
        if (componentCount >= motherboard.getMaxExpansionSlots()) {
            System.out.println("❌ Відмова: немає вільних слотів для " + comp.getModelName() + "!");
            return false;
        }
        // 2. Сумісність сокета для процесора
        if (comp instanceof ProcessorUnit) {
            ProcessorUnit cpu = (ProcessorUnit) comp;
            if (!motherboard.isSocketCompatible(cpu.getSocket())) {
                System.out.println("❌ Відмова: Процесор " + cpu.getModelName() + " (" + cpu.getSocket()
                        + ") несумісний із платою " + motherboard.getModel()
                        + " (Сокет " + motherboard.getSocketType() + ")!");
                return false;
            }
        }
        // 3. Перевірка навантаження на БЖ
        int newPower = getTotalPowerWatts() + comp.getPowerWatts();
        if (newPower > psu.getSafeContinuousWatts()) {
            System.out.println("❌ Відмова: Встановлення " + comp + " підніме споживання до " + newPower
                    + " Вт, що перевищує безпечний ліміт БЖ (" + psu.getSafeContinuousWatts() + " Вт)!");
            return false;
        }
        components[componentCount++] = comp;
        System.out.println("✅ Встановлено: " + comp);
        return true;
    }

    public int getTotalPowerWatts() {
        int sum = 0;
        for (int i = 0; i < componentCount; i++) sum += components[i].getPowerWatts();
        return sum;
    }

    public double getTotalSystemScore() {
        double sum = 0;
        for (int i = 0; i < componentCount; i++) sum += components[i].getBenchmarkScore();
        return sum;
    }

    public double getTotalBuildPrice() {
        double sum = 0;
        for (int i = 0; i < componentCount; i++) sum += components[i].getPriceUah();
        return sum;
    }

    @Override
    public String toString() {
        int internal = getTotalPowerWatts();
        int safe = psu.getSafeContinuousWatts();
        String status = (internal <= safe) ? "СТАБІЛЬНО!" : "ПЕРЕВАНТАЖЕННЯ!";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[ПАСПОРТ ЗБІРКИ '%s']:%n", buildName));
        sb.append(String.format("  Материнська плата: %s (Socket: %s, Слоти: %d/%d)%n",
                motherboard.getModel(), motherboard.getSocketType(),
                componentCount, motherboard.getMaxExpansionSlots()));
        sb.append(String.format(Locale.US, "  Блок живлення:     %s (Безпечний ліміт 85%%: %d Вт, КПД: %.0f%%)%n",
                psu.getModel(), safe, psu.getEfficiencyCoefficient() * 100));
        sb.append(String.format(Locale.US, "  Внутрішнє споживання: %d Вт (З розетки з урахуванням КПД: %.2f Вт) -> %s%n",
                internal, psu.calculateWallPowerDraw(internal), status));
        sb.append(String.format(Locale.US, "  Сумарний індекс продуктивності: %.2f балів%n", getTotalSystemScore()));
        sb.append(String.format(Locale.US, "  Загальна вартість конфігурації: %.2f грн", getTotalBuildPrice()));
        return sb.toString();
    }
}

public class Task2WorkstationConfigurator {
    public static void main(String[] args) {
        System.out.println("================ КОНФІГУРАТОР РОБОЧОЇ СТАНЦІЇ (КОМПОЗИЦІЯ) ================");

        Motherboard board = new Motherboard("ASUS ROG B650", "AM5", 4);
        PowerSupplyUnit psu = new PowerSupplyUnit("Seasonics 750W Gold", 750, 0.90);
        WorkstationPC pc = new WorkstationPC("DeepLearning-Rig", board, psu);

        int[] samples = {7300, 7450, 7400};
        NvmeSsdDrive ssd = new NvmeSsdDrive("Samsung 990 Pro 2TB", 7900, 2000, 7450, samples);

        System.out.println("[ПЕРЕВІРКА СУМІСНОСТІ]:");
        pc.installComponent(new ProcessorUnit("Intel Core i9", 21000, "LGA1700", 24, 5.8, 253));
        pc.installComponent(new ProcessorUnit("AMD Ryzen 9 7950X", 25000, "AM5", 16, 5.7, 170));
        pc.installComponent(new GraphicsAccelerator("NVIDIA RTX 4080 Super", 46000, 16, 552, 320));
        pc.installComponent(ssd);

        System.out.println();
        System.out.println(pc);
        System.out.println("=========================================================================");

        // --- Додаткові демонстрації (понад зразок з умови) ---
        System.out.println();
        System.out.println("[ТЕСТ ПЕРЕВАНТАЖЕННЯ БЖ]:");
        pc.installComponent(new GraphicsAccelerator("NVIDIA RTX 4090 Ti Extreme", 95000, 24, 680, 650));

        System.out.println();
        System.out.println("[ТЕСТ ЗАХИСНОГО КОПІЮВАННЯ NvmeSsdDrive]:");
        samples[0] = -1;                    // змінюємо оригінальний масив
        ssd.getSpeedSamples()[1] = -1;      // змінюємо копію з геттера
        System.out.println("Внутрішні дані накопичувача: " + Arrays.toString(ssd.getSpeedSamples()));
    }
}
