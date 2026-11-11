package practice10.homework;

import java.util.Locale;

interface Scalable2D {
    double MIN_SCALE_FACTOR = 0.1;

    void scale(double factor);
}

interface Rotatable2D {
    void rotate(double deltaDegrees);

    double getRotationAngle();
}

abstract class AbstractShape2D {
    private final String name;
    private double centerX;
    private double centerY;

    protected AbstractShape2D(String name, double centerX, double centerY) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getName() { return name; }
    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }

    public abstract double getArea();

    public abstract double getPerimeter();

    public final double getIsoperimetricQuotient() {
        double p = getPerimeter();
        return 4 * Math.PI * getArea() / (p * p);
    }

    protected String baseInfo() {
        return String.format(Locale.US, "['%s', center=(%.1f; %.1f)", name, centerX, centerY);
    }
    protected String metricsInfo() {
        return String.format(Locale.US, "S=%.2f, P=%.2f, Q=%.4f]",
                getArea(), getPerimeter(), getIsoperimetricQuotient());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + baseInfo() + " | " + metricsInfo();
    }
}

class CircleShape extends AbstractShape2D implements Scalable2D {
    private double radius;

    public CircleShape(String name, double centerX, double centerY, double radius) {
        super(name, centerX, centerY);
        if (radius <= 0) {
            throw new IllegalArgumentException("Радіус має бути > 0");
        }
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    @Override
    public double getArea() { return Math.PI * radius * radius; }

    @Override
    public double getPerimeter() { return 2 * Math.PI * radius; }

    @Override
    public void scale(double factor) {
        if (factor < MIN_SCALE_FACTOR) {
            factor = MIN_SCALE_FACTOR;
        }
        radius *= factor;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + baseInfo()
                + String.format(Locale.US, ", r=%.2f", radius)
                + " | " + metricsInfo();
    }
}
class RectangleShape extends AbstractShape2D implements Scalable2D, Rotatable2D {
    private double width;
    private double height;
    private double rotationAngle;

    public RectangleShape(String name, double centerX, double centerY, double width, double height) {
        super(name, centerX, centerY);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Сторони мають бути > 0");
        }
        this.width = width;
        this.height = height;
        this.rotationAngle = 0.0;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }

    @Override
    public double getArea() { return width * height; }

    @Override
    public double getPerimeter() { return 2 * (width + height); }

    @Override
    public void scale(double factor) {
        if (factor < MIN_SCALE_FACTOR) {
            factor = MIN_SCALE_FACTOR;
        }
        width *= factor;
        height *= factor;
    }

    @Override
    public void rotate(double deltaDegrees) {
        rotationAngle = ((rotationAngle + deltaDegrees) % 360 + 360) % 360;
    }

    @Override
    public double getRotationAngle() { return rotationAngle; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + baseInfo()
                + String.format(Locale.US, ", %.2fx%.2f, angle=%.1f°", width, height, rotationAngle)
                + " | " + metricsInfo();
    }
}
final class ImmutableReferenceSquare extends AbstractShape2D {
    private final double side;

    public ImmutableReferenceSquare(String name, double centerX, double centerY, double side) {
        super(name, centerX, centerY);
        if (side <= 0) {
            throw new IllegalArgumentException("Сторона має бути > 0");
        }
        this.side = side;
    }

    @Override
    public double getArea() { return side * side; }

    @Override
    public double getPerimeter() { return 4 * side; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + baseInfo()
                + String.format(Locale.US, ", side=%.2f", side)
                + " | " + metricsInfo();
    }
}

public class Task1PolymorphicShapesAndTransforms {
    public static double calculateTotalArea(AbstractShape2D[] shapes) {
        double total = 0;
        for (AbstractShape2D shape : shapes) {
            total += shape.getArea();
        }
        return total;
    }

    public static void transformScene(AbstractShape2D[] shapes, double scaleFactor, double rotateDeg) {
        for (AbstractShape2D shape : shapes) {
            boolean transformed = false;

            if (shape instanceof Scalable2D s) {
                s.scale(scaleFactor);
                transformed = true;
                String size;
                if (shape instanceof CircleShape c) {
                    size = String.format(Locale.US, "новий r=%.2f", c.getRadius());
                } else if (shape instanceof RectangleShape r) {
                    size = String.format(Locale.US, "%.2fx%.2f", r.getWidth(), r.getHeight());
                } else {
                    size = "розмір змінено";
                }
                System.out.printf(Locale.US, "   [Scalable2D] '%s' масштабовано в %.1f разів (%s)%n",
                        shape.getName(), scaleFactor, size);
            }

            if (shape instanceof Rotatable2D r) {
                r.rotate(rotateDeg);
                transformed = true;
                System.out.printf(Locale.US, "   [Rotatable2D] '%s' повернуто на %+.1f° (поточний кут: %.1f°)%n",
                        shape.getName(), rotateDeg, r.getRotationAngle());
            }

            if (!transformed) {
                System.out.printf("   [Info] '%s' (final class) пропущено — не підтримує трансформацію.%n",
                        shape.getName());
            }
        }
    }

    private static void printScene(AbstractShape2D[] shapes) {
        for (int i = 0; i < shapes.length; i++) {
            System.out.println("  " + (i + 1) + ". " + shapes[i]);
        }
    }

    public static void main(String[] args) {
        AbstractShape2D[] shapes = {
                new CircleShape("Коло-Лінза", 0.0, 0.0, 5.0),
                new RectangleShape("Рамка-А", 2.0, 3.0, 4.0, 6.0),
                new ImmutableReferenceSquare("Еталон-Квадрат", 10.0, 10.0, 5.0)
        };

        String line = "================";
        System.out.println(line + " ПОЛІМОРФНА ГЕОМЕТРІЯ ТА ІНТЕРФЕЙСИ ТРАНСФОРМАЦІЙ " + line);

        System.out.println("[СТАН СЦЕНИ ДО ТРАНСФОРМАЦІЇ]:");
        printScene(shapes);
        System.out.printf(Locale.US, "Сумарна площа сцени: %.2f кв.од.%n", calculateTotalArea(shapes));

        System.out.println();
        System.out.println("-> Виконуємо пакетну трансформацію (scale x2.0, rotate +45.0°) через instanceof...");
        transformScene(shapes, 2.0, 45.0);

        System.out.println();
        System.out.println("[СТАН СЦЕНИ ПІСЛЯ ТРАНСФОРМАЦІЇ]:");
        printScene(shapes);
        System.out.printf(Locale.US, "Нова сумарна площа сцени: %.2f кв.од.%n", calculateTotalArea(shapes));
        System.out.println(line + "=".repeat(" ПОЛІМОРФНА ГЕОМЕТРІЯ ТА ІНТЕРФЕЙСИ ТРАНСФОРМАЦІЙ ".length()) + line);
    }
}
