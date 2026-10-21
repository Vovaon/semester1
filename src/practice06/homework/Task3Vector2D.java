package practice06.homework;

import java.util.Locale;

class Vector2D {
    private double x;
    private double y;

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void multiplyByScalar(double k) {
        x = x * k;
        y = y * k;
    }

    public double dotProduct(double otherX, double otherY) {
        return x * otherX + y * otherY;
    }

    public boolean isPerpendicularTo(double otherX, double otherY) {
        double dot = dotProduct(otherX, otherY);
        return Math.abs(dot) < 1e-9;
    }

    public void normalize() {
        double length = getLength();
        if (length > 0) {
            x = x / length;
            y = y / length;
        }
    }

    public void printVector() {
        System.out.println(String.format(Locale.US, "(%.2f; %.2f), довжина |v| = %.2f",
                x, y, getLength()));
    }
}

public class Task3Vector2D {
    public static void main(String[] args) {
        System.out.println("=== ВЕКТОРНИЙ АНАЛІЗАТОР (2D GEOMETRY) ===");

        Vector2D a = new Vector2D();
        a.set(3, 4);
        System.out.println(String.format(Locale.US,
                "Вектор A: (%.2f; %.2f), довжина |A| = %.2f", a.getX(), a.getY(), a.getLength()));

        Vector2D b = new Vector2D();
        b.set(-4, 3);
        System.out.println(String.format(Locale.US,
                "Вектор B: (%.2f; %.2f), довжина |B| = %.2f", b.getX(), b.getY(), b.getLength()));

        System.out.println();
        double dot = a.dotProduct(b.getX(), b.getY());
        System.out.println(String.format(Locale.US, "Скалярний добуток A · B: %.2f", dot));
        boolean perpendicular = a.isPerpendicularTo(b.getX(), b.getY());
        System.out.println("Чи є вектори перпендикулярними (кут 90°)? -> "
                + (perpendicular ? "ТАК" : "НІ") + " (" + perpendicular + ")");

        System.out.println();
        System.out.println("Нормалізація вектора A...");
        a.normalize();
        System.out.println(String.format(Locale.US, "Одиничний вектор A: (%.2f; %.2f)",
                a.getX(), a.getY()));
        System.out.println(String.format(Locale.US, "Нова довжина вектора A: %.2f", a.getLength()));
        System.out.println("==========================================");
    }
}

