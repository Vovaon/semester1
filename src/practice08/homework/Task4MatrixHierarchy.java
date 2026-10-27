package practice08.homework;

import java.util.Locale;

class Matrix2D {
    protected double[][] data;
    private int rows;
    private int cols;

    public Matrix2D(double[][] source) {
        if (source == null || source.length == 0 || source[0] == null || source[0].length == 0) {
            throw new IllegalArgumentException("Матриця не може бути порожньою");
        }
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (source[i] == null || source[i].length != cols) {
                throw new IllegalArgumentException("Матриця має бути прямокутною");
            }
            System.arraycopy(source[i], 0, data[i], 0, cols);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void checkBounds(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new IndexOutOfBoundsException(
                    "Індекс поза межами матриці: [" + r + "][" + c + "]");
        }
    }

    public double getElement(int r, int c) {
        checkBounds(r, c);
        return data[r][c];
    }

    public void setElement(int r, int c, double val) {
        checkBounds(r, c);
        data[r][c] = val;
    }

    public double getSum() {
        double sum = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum += data[i][j];
            }
        }
        return sum;
    }

    public double getMaxElement() {
        double max = data[0][0];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                }
            }
        }
        return max;
    }
    protected static String fmt(double v) {
        return String.format(Locale.US, "%5.2f", v + 0.0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    sb.append("  ");
                }
                sb.append(fmt(data[i][j]));
            }
            sb.append(" ]");
            if (i < rows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

class SquareMatrix extends Matrix2D {

    public SquareMatrix(double[][] source) {
        super(source);
        if (getRows() != getCols()) {
            System.out.println("Попередження: матриця " + getRows() + "x" + getCols()
                    + " не є квадратною!");
        }
    }

    public int getDimension() {
        return getRows();
    }

    private boolean isSquare() {
        return getRows() == getCols();
    }

    public double getTrace() {
        double trace = 0.0;
        int n = Math.min(getRows(), getCols());
        for (int i = 0; i < n; i++) {
            trace += data[i][i];
        }
        return trace;
    }

    public boolean isSymmetric() {
        if (!isSquare()) {
            return false;
        }
        int n = getDimension();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (data[i][j] != data[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public double getDeterminant() {
        if (!isSquare()) {
            return 0.0;
        }
        int n = getDimension();
        if (n == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        if (n == 3) {
            return data[0][0] * data[1][1] * data[2][2]
                    + data[0][1] * data[1][2] * data[2][0]
                    + data[0][2] * data[1][0] * data[2][1]
                    - data[0][2] * data[1][1] * data[2][0]
                    - data[0][0] * data[1][2] * data[2][1]
                    - data[0][1] * data[1][0] * data[2][2];
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + String.format(Locale.US,
                "Характеристики SquareMatrix: Розмір = %dx%d | Слід (Trace) = %.2f | "
                        + "Симетрична = %b | det(A) = %.2f",
                getRows(), getCols(), getTrace(), isSymmetric(), getDeterminant() + 0.0);
    }
}

class CramerMatrixSolver extends SquareMatrix {
    private static final double EPS = 1e-9;

    private double[] freeTerms;

    public CramerMatrixSolver(double[][] coefficients, double[] freeTerms) {
        super(coefficients);
        if (freeTerms == null || freeTerms.length != getRows()) {
            throw new IllegalArgumentException(
                    "Кількість вільних членів має дорівнювати розмірності матриці");
        }
        this.freeTerms = freeTerms.clone();
    }

    public double[] solve() {
        int n = getDimension();
        if (getRows() != getCols() || (n != 2 && n != 3)) {
            System.out.println("Метод Крамера реалізовано лише для систем 2x2 та 3x3.");
            return new double[0];
        }

        double delta = getDeterminant();
        if (Math.abs(delta) < EPS) {
            System.out.println("Головний визначник дорівнює 0 — єдиного розв'язку немає.");
            return new double[0];
        }

        double[] roots = new double[n];
        for (int k = 0; k < n; k++) {
            double[][] tmp = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tmp[i][j] = (j == k) ? freeTerms[i] : getElement(i, j);
                }
            }
            SquareMatrix replaced = new SquareMatrix(tmp);
            roots[k] = replaced.getDeterminant() / delta;
        }
        return roots;
    }

    private boolean verify(double[] roots) {
        for (int i = 0; i < getRows(); i++) {
            double sum = 0.0;
            for (int j = 0; j < getCols(); j++) {
                sum += getElement(i, j) * roots[j];
            }
            if (Math.abs(sum - freeTerms[i]) > 1e-6) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Розширена матриця системи [A | B]:\n");
        for (int i = 0; i < getRows(); i++) {
            sb.append("[ ");
            for (int j = 0; j < getCols(); j++) {
                if (j > 0) {
                    sb.append("  ");
                }
                sb.append(fmt(getElement(i, j)));
            }
            sb.append("  | ").append(fmt(freeTerms[i])).append(" ]\n");
        }

        double delta = getDeterminant();
        sb.append(String.format(Locale.US, "Головний визначник det(A) = %.2f\n", delta + 0.0));

        double[] roots = solve();
        if (roots.length == 0) {
            sb.append("Розв'язок системи: єдиного розв'язку немає");
            return sb.toString();
        }

        sb.append("Розв'язок системи: ");
        for (int i = 0; i < roots.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format(Locale.US, "x%d = %.2f", i + 1, roots[i] + 0.0));
        }
        sb.append(verify(roots) ? " (Перевірка пройдена!)" : " (Перевірка НЕ пройдена!)");
        return sb.toString();
    }
}

public class Task4MatrixHierarchy {
    public static void main(String[] args) {
        System.out.println("=============== ІЄРАРХІЯ МАТРИЦЬ ТА СЛАР ===============");

        System.out.println("--- 1. Квадратна матриця 3x3 (SquareMatrix) ---");
        double[][] m3 = {
                {2.0, 1.0, 0.0},
                {1.0, 3.0, 1.0},
                {0.0, 1.0, 2.0}
        };
        SquareMatrix square = new SquareMatrix(m3);
        System.out.println(square);

        System.out.println();

        System.out.println("--- 2. Розв'язувач системи Крамера (CramerMatrixSolver 2x2) ---");
        double[][] coefficients = {
                {2.0, 3.0},
                {4.0, -1.0}
        };
        double[] freeTerms = {8.0, 2.0};
        CramerMatrixSolver solver = new CramerMatrixSolver(coefficients, freeTerms);
        System.out.println(solver);

        System.out.println("========================================================");
    }
}

