package practice06.homework;

import java.util.Locale;

class QuadraticEquation {
    private double a;
    private double b;
    private double c;

    public void setCoefficients(double a, double b, double c) {
        if (a == 0) {
            System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0 (це лінійне рівняння)!");
            a = 1.0;
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getDiscriminant() {
        return b * b - 4 * a * c;
    }

    public boolean hasRealRoots() {
        return getDiscriminant() >= 0;
    }

    public double getRoot1() {
        double d = getDiscriminant();
        if (d < 0) {
            return Double.NaN;
        }
        return (-b + Math.sqrt(d)) / (2 * a);
    }

    public double getRoot2() {
        double d = getDiscriminant();
        if (d < 0) {
            return Double.NaN;
        }
        return (-b - Math.sqrt(d)) / (2 * a);
    }


    private String formatCoefficient(double value) {
        return value < 0 ? "(" + value + ")" : String.valueOf(value);
    }

    public void printSolution() {
        System.out.println("Рівняння: " + formatCoefficient(a) + "*x^2 + "
                + formatCoefficient(b) + "*x + " + formatCoefficient(c) + " = 0");

        double d = getDiscriminant();
        System.out.println(String.format(Locale.US, "Дискримінант D = %.2f", d));

        if (d > 0) {
            System.out.println("Рівняння має 2 дійсні корені:");
            System.out.println(String.format(Locale.US, "  x1 = %.2f", getRoot1()));
            System.out.println(String.format(Locale.US, "  x2 = %.2f", getRoot2()));
        } else if (d == 0) {
            System.out.println("Рівняння має один дійсний корінь:");
            System.out.println(String.format(Locale.US, "  x = %.2f", -b / (2 * a)));
        } else {
            System.out.println("Рівняння не має дійсних коренів у множині R.");
        }
    }
}

public class Task2QuadraticEquation {
    public static void main(String[] args) {
        System.out.println("=== РОЗВ'ЯЗУВАЧ КВАДРАТНИХ РІВНЯНЬ ===");

        QuadraticEquation eq = new QuadraticEquation();

        System.out.println("Тест 1:");
        eq.setCoefficients(1, -5, 6);
        eq.printSolution();
        System.out.println("--------------------------------------");

        System.out.println("Тест 2:");
        eq.setCoefficients(1, -4, 4);
        eq.printSolution();
        System.out.println("--------------------------------------");

        System.out.println("Тест 3:");
        eq.setCoefficients(1, 2, 5);
        eq.printSolution();
        System.out.println("======================================");
    }
}
