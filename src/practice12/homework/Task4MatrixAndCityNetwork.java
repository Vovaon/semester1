package practice12.homework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

abstract class Transport {
    private final String name;
    private final double speedKmH;

    public Transport(String name, double speedKmH) {
        this.name = name;
        this.speedKmH = speedKmH;
    }

    public String getName() {
        return name;
    }

    public double getSpeedKmH() {
        return speedKmH;
    }

    public double calculateTimeHours(int distanceKm) {
        return distanceKm / speedKmH;
    }

    public abstract double calculateCostUah(int distanceKm);
}

class Bus extends Transport {
    public Bus() {
        super("Bus", 80.0);
    }

    @Override
    public double calculateCostUah(int distanceKm) {
        return distanceKm * 2.5;
    }
}

class Train extends Transport {
    public Train() {
        super("Train", 100.0);
    }

    @Override
    public double calculateCostUah(int distanceKm) {
        return distanceKm * 1.8 + 150.0;
    }
}

class DeliveryTruck extends Transport {
    public DeliveryTruck() {
        super("DeliveryTruck", 70.0);
    }

    @Override
    public double calculateCostUah(int distanceKm) {
        return distanceKm * 4.0 + 300.0;
    }
}

class TripOrder {
    private final String orderId;
    private final String fromCity;
    private final String toCity;
    private final Transport transport;

    public TripOrder(String orderId, String fromCity, String toCity, Transport transport) {
        this.orderId = orderId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.transport = transport;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public Transport getTransport() {
        return transport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripOrder)) return false;
        TripOrder other = (TripOrder) o;
        return orderId.equals(other.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}

class RouteDispatcher {
    private ArrayList<String> cities;
    private int[][] distances;
    private HashSet<String> closedCities;
    private ArrayList<TripOrder> orders = new ArrayList<>();

    public RouteDispatcher(ArrayList<String> cities, int[][] distances, HashSet<String> closedCities) {
        this.cities = new ArrayList<>(cities);
        this.distances = new int[distances.length][];
        for (int i = 0; i < distances.length; i++) {
            this.distances[i] = distances[i].clone();
        }
        this.closedCities = new HashSet<>(closedCities);
    }

    public void addOrder(TripOrder order) {
        orders.add(order);
    }

    public ArrayList<TripOrder> getOrdersCopy() {
        return new ArrayList<>(orders);
    }

    public int getDistance(String from, String to) {
        int i = cities.indexOf(from);
        int j = cities.indexOf(to);
        if (i < 0 || j < 0) {
            return 0;
        }
        return distances[i][j];
    }

    public HashSet<String> getDirectNeighbors(String city) {
        HashSet<String> result = new HashSet<>();
        int idx = cities.indexOf(city);
        if (idx < 0) {
            return result;
        }
        for (int j = 0; j < distances[idx].length; j++) {
            if (distances[idx][j] > 0) {
                result.add(cities.get(j));
            }
        }
        return result;
    }

    public HashSet<String> getCommonNeighbors(String cityA, String cityB) {
        HashSet<String> result = getDirectNeighbors(cityA);
        result.retainAll(getDirectNeighbors(cityB));
        return result;
    }

    public HashSet<String> getExclusiveNeighbors(String cityA, String cityB) {
        HashSet<String> result = getDirectNeighbors(cityA);
        result.removeAll(getDirectNeighbors(cityB));
        return result;
    }

    public int filterInvalidOrders() {
        HashSet<String> seenIds = new HashSet<>();
        int removed = 0;
        Iterator<TripOrder> it = orders.iterator();
        while (it.hasNext()) {
            TripOrder o = it.next();
            if (!seenIds.add(o.getOrderId())) {
                printRemoved("[ДУБЛІКАТ]", o);
                it.remove();
                removed++;
            } else if (closedCities.contains(o.getFromCity()) || closedCities.contains(o.getToCity())) {
                printRemoved("[МІСТО ЗАКРИТЕ]", o);
                it.remove();
                removed++;
            } else if (getDistance(o.getFromCity(), o.getToCity()) <= 0) {
                printRemoved("[НЕМАЄ ДОРОГИ]", o);
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    private void printRemoved(String reason, TripOrder o) {
        System.out.printf("  ❌ %-16s %s (%s -> %s) — видалено!%n",
                reason, o.getOrderId(), o.getFromCity(), o.getToCity());
    }

    public int getTotalDistance() {
        int sum = 0;
        for (TripOrder o : orders) {
            sum += getDistance(o.getFromCity(), o.getToCity());
        }
        return sum;
    }

    public double getTotalCost() {
        double sum = 0;
        for (TripOrder o : orders) {
            sum += o.getTransport().calculateCostUah(getDistance(o.getFromCity(), o.getToCity()));
        }
        return sum;
    }

    public void printReport() {
        System.out.println("--- 3. Схвалені рейси (залишилось " + orders.size() + ") ---");
        for (TripOrder o : orders) {
            int km = getDistance(o.getFromCity(), o.getToCity());
            Transport t = o.getTransport();
            String route = o.getFromCity() + " -> " + o.getToCity();
            System.out.printf(Locale.US, "  ✅ %s | %-14s | %d км | %-13s | Час: %.2f год | Вартість: %.2f грн%n",
                    o.getOrderId(), route, km, t.getName(), t.calculateTimeHours(km), t.calculateCostUah(km));
        }
    }
}

public class Task4MatrixAndCityNetwork {

    public static void main(String[] args) {
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Київ");
        cities.add("Львів");
        cities.add("Одеса");
        cities.add("Дніпро");

        int[][] distances = {
                {0, 540, 480, 450},
                {540, 0, 790, 0},
                {480, 790, 0, 460},
                {450, 0, 460, 0}
        };

        HashSet<String> closed = new HashSet<>();
        closed.add("Одеса");

        RouteDispatcher dispatcher = new RouteDispatcher(cities, distances, closed);

        dispatcher.addOrder(new TripOrder("ORD-1", "Київ", "Львів", new Train()));
        dispatcher.addOrder(new TripOrder("ORD-1", "Київ", "Львів", new Bus()));
        dispatcher.addOrder(new TripOrder("ORD-2", "Київ", "Одеса", new Bus()));
        dispatcher.addOrder(new TripOrder("ORD-3", "Львів", "Дніпро", new DeliveryTruck()));
        dispatcher.addOrder(new TripOrder("ORD-4", "Київ", "Дніпро", new DeliveryTruck()));

        System.out.println("================ 🚚 ЛОГІСТИЧНИЙ ДИСПЕТЧЕР: МАТРИЦЯ ТА МЕРЕЖА 🚚 ================");
        System.out.println("--- 1. Аналіз мережі доріг (2D-масив -> HashSet) ---");

        HashSet<String> kyivNeighbors = dispatcher.getDirectNeighbors("Київ");
        HashSet<String> lvivNeighbors = dispatcher.getDirectNeighbors("Львів");
        System.out.println("  Прямі сусіди міста Київ [" + kyivNeighbors.size() + "]:   " + kyivNeighbors);
        System.out.println("  Прямі сусіди міста Львів [" + lvivNeighbors.size() + "]:  " + lvivNeighbors);
        System.out.println("  Спільні сусіди Київ ∩ Львів:   " + dispatcher.getCommonNeighbors("Київ", "Львів"));
        System.out.println("  Тільки з Києва (Київ \\ Львів): " + dispatcher.getExclusiveNeighbors("Київ", "Львів"));

        System.out.println();
        System.out.println("--- 2. Фільтрація заявок через Iterator.remove() (Закриті міста: " + closed + ") ---");
        int removed = dispatcher.filterInvalidOrders();

        System.out.println();
        dispatcher.printReport();

        System.out.println();
        System.out.println("--- Підсумок ---");
        System.out.println("Видалено заявок: " + removed + " | Схвалено: " + dispatcher.getOrdersCopy().size());
        System.out.printf(Locale.US, "Загальна відстань: %d км | Загальна вартість перевезень: %.2f грн%n",
                dispatcher.getTotalDistance(), dispatcher.getTotalCost());
        System.out.println("==========================================================================");
    }
}