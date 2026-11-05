package practice09.homework;

import java.util.Locale;

class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        this(other.x, other.y);
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double distanceTo(Point2D other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.2f; %.2f)", x, y);
    }
}
abstract class AbstractShape2D {
    private final String name;
    private final Point2D anchorPoint;

    public AbstractShape2D(String name, Point2D anchorPoint) {
        this.name = name;
        this.anchorPoint = (anchorPoint != null) ? new Point2D(anchorPoint) : new Point2D(0, 0);
    }

    public String getName() { return name; }

    public Point2D getAnchorPoint() {
        return new Point2D(anchorPoint);
    }

    public void moveBy(double dx, double dy) {
        anchorPoint.translate(dx, dy);
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    protected String describe(String params) {
        return String.format(Locale.US, "%s[name='%s', center=%s%s | S=%.2f, P=%.2f]",
                getClass().getSimpleName(), name, anchorPoint,
                (params == null || params.isEmpty()) ? "" : ", " + params,
                getArea(), getPerimeter());
    }

    @Override
    public String toString() {
        return describe(null);
    }
}

class Circle2D extends AbstractShape2D {
    private double radius;

    public Circle2D(String name, Point2D center, double radius) {
        super(name, center);
        if (radius <= 0) throw new IllegalArgumentException("Радіус має бути > 0");
        this.radius = radius;
    }

    @Override
    public double getArea() { return Math.PI * radius * radius; }

    @Override
    public double getPerimeter() { return 2 * Math.PI * radius; }

    @Override
    public String toString() {
        return describe(String.format(Locale.US, "r=%.2f", radius));
    }
}

class Rectangle2D extends AbstractShape2D {
    private double width;
    private double height;

    public Rectangle2D(String name, Point2D center, double width, double height) {
        super(name, center);
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Сторони мають бути > 0");
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() { return width * height; }

    @Override
    public double getPerimeter() { return 2 * (width + height); }

    @Override
    public String toString() {
        return describe(String.format(Locale.US, "%.2fx%.2f", width, height));
    }
}

class RightTriangle2D extends AbstractShape2D {
    private double legA;
    private double legB;

    public RightTriangle2D(String name, Point2D center, double legA, double legB) {
        super(name, center);
        if (legA <= 0 || legB <= 0) throw new IllegalArgumentException("Катети мають бути > 0");
        this.legA = legA;
        this.legB = legB;
    }

    public double getHypotenuse() { return Math.sqrt(legA * legA + legB * legB); }

    @Override
    public double getArea() { return 0.5 * legA * legB; }

    @Override
    public double getPerimeter() { return legA + legB + getHypotenuse(); }

    @Override
    public String toString() {
        return describe(String.format(Locale.US, "a=%.2f, b=%.2f, c=%.2f",
                legA, legB, getHypotenuse()));
    }
}

class GeometryCanvas {
    private final String title;
    private final AbstractShape2D[] shapes;
    private int count;

    public GeometryCanvas(String title, int capacity) {
        this.title = title;
        this.shapes = new AbstractShape2D[Math.max(capacity, 1)];
        this.count = 0;
    }

    public boolean addShape(AbstractShape2D shape) {
        if (shape == null || count >= shapes.length) return false;
        shapes[count++] = shape;
        return true;
    }

    public double getTotalArea() {
        double sum = 0;
        for (int i = 0; i < count; i++) sum += shapes[i].getArea();
        return sum;
    }

    public double getTotalPerimeter() {
        double sum = 0;
        for (int i = 0; i < count; i++) sum += shapes[i].getPerimeter();
        return sum;
    }

    public AbstractShape2D findLargestShape() {
        if (count == 0) return null;
        AbstractShape2D largest = shapes[0];
        for (int i = 1; i < count; i++) {
            if (shapes[i].getArea() > largest.getArea()) largest = shapes[i];
        }
        return largest;
    }

    public void moveAllShapes(double dx, double dy) {
        for (int i = 0; i < count; i++) shapes[i].moveBy(dx, dy);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("================ ГЕОМЕТРИЧНЕ ПОЛОТНО (КОМПОЗИЦІЯ + ABSTRACT) ================\n");
        sb.append(String.format("Полотно: '%s' (Фігур: %d/%d)%n", title, count, shapes.length));
        for (int i = 0; i < count; i++) {
            sb.append(String.format("  %d. %s%n", i + 1, shapes[i]));
        }
        sb.append("-------------------------------------------------------------------------\n");
        sb.append(String.format(Locale.US, "Сумарна площа креслення:    %.2f кв.од.%n", getTotalArea()));
        sb.append(String.format(Locale.US, "Сумарний периметр:          %.2f од.%n", getTotalPerimeter()));
        AbstractShape2D largest = findLargestShape();
        if (largest != null) {
            sb.append(String.format(Locale.US, "Найбільша фігура:           %s (S = %.2f)",
                    largest.getName(), largest.getArea()));
        }
        return sb.toString();
    }
}

public class Task1CompositeShapesCanvas {
    public static void main(String[] args) {
        GeometryCanvas canvas = new GeometryCanvas("Інженерне креслення №1", 5);

        Point2D circleCenter = new Point2D(0, 0);
        Circle2D circle = new Circle2D("Коло-А", circleCenter, 5.0);
        canvas.addShape(circle);
        canvas.addShape(new Rectangle2D("Плита-Б", new Point2D(10, 5), 4.0, 6.0));
        canvas.addShape(new RightTriangle2D("Клин-В", new Point2D(-3, 2), 3.0, 4.0));

        System.out.println(canvas);

        circleCenter.translate(100, 100);
        circle.getAnchorPoint().translate(50, 50);

        System.out.println("-> Переміщуємо всі фігури полотна на вектор (+5.0; -2.0)...");
        canvas.moveAllShapes(5.0, -2.0);
        System.out.println("Новий центр '" + circle.getName() + "': " + circle.getAnchorPoint());
        System.out.println("=========================================================================");
    }
}
