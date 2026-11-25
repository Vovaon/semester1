package practice12.homework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

abstract class Shape {
    private final String id;
    private final String color;

    public Shape(String id, String color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        Shape other = (Shape) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

class Circle extends Shape {
    private final double radius;

    public Circle(String id, String color, double radius) {
        super(id, color);
        if (radius <= 0) {
            throw new IllegalArgumentException("Радіус має бути більшим за 0");
        }
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return "Circle[" + getId() + ", " + getColor() + ", r=" + radius + "]";
    }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(String id, String color, double width, double height) {
        super(id, color);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Розміри мають бути більшими за 0");
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return "Rectangle[" + getId() + ", " + getColor() + ", " + width + "x" + height + "]";
    }
}

class RightTriangle extends Shape {
    private final double legA;
    private final double legB;

    public RightTriangle(String id, String color, double legA, double legB) {
        super(id, color);
        if (legA <= 0 || legB <= 0) {
            throw new IllegalArgumentException("Катети мають бути більшими за 0");
        }
        this.legA = legA;
        this.legB = legB;
    }

    @Override
    public double getArea() {
        return legA * legB / 2;
    }

    @Override
    public double getPerimeter() {
        return legA + legB + Math.sqrt(legA * legA + legB * legB);
    }

    @Override
    public String toString() {
        return "RightTriangle[" + getId() + ", " + getColor() + ", " + legA + "x" + legB + "]";
    }
}

class GeometryCanvas {
    private String title;
    private ArrayList<Shape> shapes = new ArrayList<>();
    private HashSet<String> usedIds = new HashSet<>();

    public GeometryCanvas(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean addShape(Shape s) {
        if (s == null || !usedIds.add(s.getId())) {
            return false;
        }
        shapes.add(s);
        return true;
    }

    public int removeSmallShapes(double minArea) {
        int removed = 0;
        Iterator<Shape> it = shapes.iterator();
        while (it.hasNext()) {
            Shape s = it.next();
            if (s.getArea() < minArea) {
                it.remove();
                usedIds.remove(s.getId());
                removed++;
                System.out.printf(Locale.US, "  🗑 Видалено: %s (площа %.2f)%n", s.getId(), s.getArea());
            }
        }
        return removed;
    }

    public double getTotalArea() {
        double sum = 0;
        for (Shape s : shapes) {
            sum += s.getArea();
        }
        return sum;
    }

    public double getTotalPerimeter() {
        double sum = 0;
        for (Shape s : shapes) {
            sum += s.getPerimeter();
        }
        return sum;
    }

    public HashSet<String> getUniqueColors() {
        HashSet<String> colors = new HashSet<>();
        for (Shape s : shapes) {
            colors.add(s.getColor());
        }
        return colors;
    }

    public ArrayList<Shape> getShapesCopy() {
        return new ArrayList<>(shapes);
    }
}

public class Task3GeometryCanvasAndStats {

    private static void printAdd(GeometryCanvas canvas, Shape s) {
        if (canvas.addShape(s)) {
            System.out.println("  ✅ Додано: " + s);
        } else {
            System.out.println("  ⚠ Відхилено (дублікат ID): " + s.getId());
        }
    }

    private static void printStats(GeometryCanvas canvas) {
        HashSet<String> colors = canvas.getUniqueColors();
        System.out.printf(Locale.US, "  Фігур на полотні: %d | Сумарна площа: %.2f | Сумарний периметр: %.2f%n",
                canvas.getShapesCopy().size(), canvas.getTotalArea(), canvas.getTotalPerimeter());
        System.out.println("  Унікальні кольори [" + colors.size() + "]: " + colors);
    }

    public static void main(String[] args) {
        GeometryCanvas canvas1 = new GeometryCanvas("Головне полотно");

        System.out.println("================ 📐 ГЕОМЕТРИЧНЕ ПОЛОТНО: ООП + КОЛЕКЦІЇ 📐 ================");
        System.out.println("Додавання фігур на '" + canvas1.getTitle() + "':");

        printAdd(canvas1, new Circle("C-1", "Red", 3.0));
        printAdd(canvas1, new Circle("C-1", "Blue", 5.0));
        printAdd(canvas1, new Rectangle("R-1", "Blue", 4.0, 5.0));
        printAdd(canvas1, new Rectangle("R-2", "Green", 2.0, 3.0));
        printAdd(canvas1, new RightTriangle("T-1", "Red", 3.0, 4.0));
        printAdd(canvas1, new RightTriangle("T-2", "Yellow", 6.0, 8.0));

        System.out.println();
        System.out.println("[ДО ОЧИЩЕННЯ]");
        printStats(canvas1);

        System.out.println();
        System.out.println(">>> Видалення фігур із площею < 10.0 через Iterator.remove()...");
        canvas1.removeSmallShapes(10.0);

        System.out.println();
        System.out.println("[ПІСЛЯ ОЧИЩЕННЯ]");
        printStats(canvas1);

        HashSet<String> palette1 = canvas1.getUniqueColors();
        HashSet<String> palette2 = new HashSet<>();
        palette2.add("Blue");
        palette2.add("Yellow");
        palette2.add("Purple");

        HashSet<String> common = new HashSet<>(palette1);
        common.retainAll(palette2);

        HashSet<String> onlyFirst = new HashSet<>(palette1);
        onlyFirst.removeAll(palette2);

        HashSet<String> all = new HashSet<>(palette1);
        all.addAll(palette2);

        System.out.println();
        System.out.println("--- Порівняння палітр (HashSet) ---");
        System.out.println("  Палітра полотна 2 [" + palette2.size() + "]:       " + palette2);
        System.out.println("  Спільні кольори (C1 ∩ C2):   " + common);
        System.out.println("  Тільки на полотні 1 (C1 \\ C2): " + onlyFirst);
        System.out.println("  Усі кольори разом (C1 U C2): " + all);
        System.out.println("==========================================================================");
    }
}
