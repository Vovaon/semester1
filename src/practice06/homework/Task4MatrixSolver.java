package practice06.homework;

import java.util.Locale;

class MatrixSolver {
    private double[][] data;

    public void setMatrix(double[][] data) {
        this.data = data;
    }

    public void printMatrix() {
        int rows = data.length;
        int cols = data[0].length;
        int[] widths = new int[cols];
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                widths[j] = Math.max(widths[j], fmt(data[i][j]).length());
            }
        }
        for (int i = 0; i < rows; i++) {
            StringBuilder sb = new StringBuilder("[ ");
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    sb.append("  ");
                }
                sb.append(String.format("%" + widths[j] + "s", fmt(data[i][j])));
            }
            sb.append(" ]");
            System.out.println(sb);
        }
    }

    public double getDeterminant2x2() {
        return data[0][0] * data[1][1] - data[0][1] * data[1][0];
    }

    // Правило Саррюса
    public double getDeterminant3x3() {
        return data[0][0] * data[1][1] * data[2][2]
                + data[0][1] * data[1][2] * data[2][0]
                + data[0][2] * data[1][0] * data[2][1]
                - data[0][2] * data[1][1] * data[2][0]
                - data[0][0] * data[1][2] * data[2][1]
                - data[0][1] * data[1][0] * data[2][2];
    }

    public void transpose() {
        int rows = data.length;
        int cols = data[0].length;
        double[][] t = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                t[j][i] = data[i][j];
            }
        }
        data = t;
    }

    public void solveSystem2x2(double c1, double c2) {
        System.out.println("Рівняння:");
        System.out.println("  " + fmt(data[0][0]) + "*x " + sign(data[0][1]) + " "
                + fmt(Math.abs(data[0][1])) + "*y = " + fmt(c1));
        System.out.println("  " + fmt(data[1][0]) + "*x " + sign(data[1][1]) + " "
                + fmt(Math.abs(data[1][1])) + "*y = " + fmt(c2));
        System.out.println();

        double delta = getDeterminant2x2();
        double deltaX = c1 * data[1][1] - c2 * data[0][1];
        double deltaY = data[0][0] * c2 - data[1][0] * c1;

        System.out.println("Головний визначник Delta  = " + fmt(delta));
        System.out.println("Визначник Delta_x        = " + fmt(deltaX));
        System.out.println("Визначник Delta_y        = " + fmt(deltaY));
        System.out.println();

        if (Math.abs(delta) > 1e-9) {
            double x = deltaX / delta;
            double y = deltaY / delta;
            System.out.println("Знайдено єдиний розв'язок:");
            System.out.println("  x = Delta_x / Delta = " + fmt(x));
            System.out.println("  y = Delta_y / Delta = " + fmt(y));

            double p1 = data[0][0] * x;
            double p2 = data[0][1] * y;
            double sum = p1 + p2;
            boolean ok = Math.abs(sum - c1) < 1e-9;
            System.out.println("Перевірка: " + num(data[0][0]) + "*(" + num(x) + ") + "
                    + num(data[0][1]) + "*(" + num(y) + ") = " + num(p1) + " + " + num(p2)
                    + " = " + num(sum) + (ok ? " (ВІРНО!)" : " (ПОМИЛКА!)"));
        } else if (Math.abs(deltaX) < 1e-9 && Math.abs(deltaY) < 1e-9) {
            System.out.println("Система має безліч розв'язків");
        } else {
            System.out.println("Система несумісна (не має розв'язків)");
        }
    }

    private String fmt(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private String num(double value) {
        if (Math.abs(value - Math.rint(value)) < 1e-9) {
            return String.valueOf((long) Math.rint(value));
        }
        return fmt(value);
    }

    private String sign(double value) {
        return value < 0 ? "-" : "+";
    }
}

public class Task4MatrixSolver {
    public static void main(String[] args) {
        System.out.println("================ МАТРИЧНИЙ КАЛЬКУЛЯТОР ТА СЛАР ================");

        MatrixSolver solver = new MatrixSolver();
        double[][] a = {
                {2, 3},
                {4, -1}
        };

        solver.setMatrix(a);
        System.out.println("Початкова матриця 2x2 коефіцієнтів A:");
        solver.printMatrix();
        System.out.println();
        System.out.println(String.format(Locale.US, "Визначник det(A) = %.2f",
                solver.getDeterminant2x2()));
        System.out.println();

        solver.transpose();
        System.out.println("Транспонована матриця A^T:");
        solver.printMatrix();
        System.out.println();

        solver.setMatrix(a);
        System.out.println("--- РОЗВ'ЯЗАННЯ СИСТЕМИ ЛІНІЙНИХ РІВНЯНЬ (МЕТОД КРАМЕРА) ---");
        solver.solveSystem2x2(8, 2);
        System.out.println("===============================================================");

        System.out.println();
        System.out.println("--- ВИЗНАЧНИК МАТРИЦІ 3x3 (ПРАВИЛО САРРЮСА) ---");
        solver.setMatrix(new double[][]{
                {2, 1, 3},
                {0, -1, 4},
                {1, 2, 5}
        });
        solver.printMatrix();
        System.out.println(String.format(Locale.US, "det = %.2f", solver.getDeterminant3x3()));
    }
}
