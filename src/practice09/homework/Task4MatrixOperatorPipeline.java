package practice09.homework;

import java.util.Locale;
class EncapsulatedMatrix {
    private final int rows;
    private final int cols;
    private final double[][] grid;

    public EncapsulatedMatrix(double[][] source) {
        if (source == null || source.length == 0 || source[0] == null || source[0].length == 0)
            throw new IllegalArgumentException("Матриця не може бути порожньою");
        this.rows = source.length;
        this.cols = source[0].length;
        this.grid = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (source[i] == null || source[i].length != cols)
                throw new IllegalArgumentException("Рядки матриці мають однакову довжину");
            System.arraycopy(source[i], 0, this.grid[i], 0, cols);
        }
    }

    public int getRows() { return rows; }

    public int getCols() { return cols; }
    public double[][] getGridCopy() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    public double getElement(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols)
            throw new IndexOutOfBoundsException("Індекс поза межами матриці: (" + r + ", " + c + ")");
        return grid[r][c];
    }

    public boolean isSquare() { return rows == cols; }

    public double getTrace() {
        if (!isSquare()) throw new IllegalStateException("Слід визначений лише для квадратних матриць");
        double sum = 0;
        for (int i = 0; i < rows; i++) sum += grid[i][i];
        return sum;
    }

    public double getFrobeniusNorm() {
        double sum = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                sum += grid[i][j] * grid[i][j];
        return Math.sqrt(sum);
    }
    public double getDeterminant() {
        if (!isSquare()) throw new IllegalStateException("Визначник визначений лише для квадратних матриць");
        if (rows == 2) {
            return grid[0][0] * grid[1][1] - grid[0][1] * grid[1][0];
        }
        if (rows == 3) {
            return grid[0][0] * grid[1][1] * grid[2][2]
                    + grid[0][1] * grid[1][2] * grid[2][0]
                    + grid[0][2] * grid[1][0] * grid[2][1]
                    - grid[0][2] * grid[1][1] * grid[2][0]
                    - grid[0][0] * grid[1][2] * grid[2][1]
                    - grid[0][1] * grid[1][0] * grid[2][2];
        }
        throw new UnsupportedOperationException("Підтримуються лише матриці 2x2 та 3x3");
    }
    public String getSummary() {
        String trace = isSquare() ? String.format(Locale.US, "%.2f", getTrace()) : "н/д";
        String det = (isSquare() && rows <= 3) ? String.format(Locale.US, "%.2f", getDeterminant()) : "н/д";
        return String.format(Locale.US, "%dx%d | Норма Фробеніуса: %.2f | Слід: %s | det: %s",
                rows, cols, getFrobeniusNorm(), trace, det);
    }

    public String getRowsAsTable() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("  [ ");
            for (int j = 0; j < cols; j++) {
                if (j > 0) sb.append("  ");
                sb.append(String.format(Locale.US, "%6.2f", grid[i][j]));
            }
            sb.append(" ]");
            if (i < rows - 1) sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Матриця (" + getSummary() + "):" + System.lineSeparator() + getRowsAsTable();
    }
}
abstract class AbstractMatrixOperator {
    private final String operatorName;

    public AbstractMatrixOperator(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorName() { return operatorName; }

    public abstract boolean isApplicable(EncapsulatedMatrix input);

    public abstract EncapsulatedMatrix apply(EncapsulatedMatrix input);

    public final EncapsulatedMatrix executeSafely(EncapsulatedMatrix input) {
        if (input != null && isApplicable(input)) {
            return apply(input);
        }
        System.out.println("⚠️ Попередження: оператор '" + operatorName
                + "' неможливо застосувати до цієї матриці. Повертаємо вхідну без змін.");
        return input;
    }
}

class ScaleMatrixOperator extends AbstractMatrixOperator {
    private final double scaleFactor;

    public ScaleMatrixOperator(double scaleFactor) {
        super(String.format(Locale.US, "Scale(x%.1f)", scaleFactor));
        this.scaleFactor = scaleFactor;
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) { return true; }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        double[][] g = input.getGridCopy();
        for (double[] row : g)
            for (int j = 0; j < row.length; j++) row[j] *= scaleFactor;
        return new EncapsulatedMatrix(g);
    }
}

class TransposeMatrixOperator extends AbstractMatrixOperator {
    public TransposeMatrixOperator() {
        super("Transpose(A^T)");
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) { return true; }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        double[][] g = input.getGridCopy();
        double[][] t = new double[input.getCols()][input.getRows()];
        for (int i = 0; i < input.getRows(); i++)
            for (int j = 0; j < input.getCols(); j++)
                t[j][i] = g[i][j];
        return new EncapsulatedMatrix(t);
    }
}

class DiagonalShiftOperator extends AbstractMatrixOperator {
    private final double lambda;

    public DiagonalShiftOperator(double lambda) {
        super(String.format(Locale.US, "DiagonalShift(%+.1f*I)", lambda));
        this.lambda = lambda;
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) { return input.isSquare(); }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        double[][] g = input.getGridCopy();
        for (int i = 0; i < g.length; i++) g[i][i] += lambda;
        return new EncapsulatedMatrix(g);
    }
}
class MultiplyByMatrixOperator extends AbstractMatrixOperator {
    private final EncapsulatedMatrix rightMatrix;

    public MultiplyByMatrixOperator(EncapsulatedMatrix rightMatrix) {
        super("MultiplyBy(B " + rightMatrix.getRows() + "x" + rightMatrix.getCols() + ")");
        this.rightMatrix = new EncapsulatedMatrix(rightMatrix.getGridCopy());
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) {
        return input.getCols() == rightMatrix.getRows();
    }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        double[][] a = input.getGridCopy();
        double[][] b = rightMatrix.getGridCopy();
        int m = input.getRows();
        int n = rightMatrix.getCols();
        int common = input.getCols();
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < common; k++) sum += a[i][k] * b[k][j];
                c[i][j] = sum;
            }
        return new EncapsulatedMatrix(c);
    }
}
class MatrixPipeline {
    private final String pipelineName;
    private final AbstractMatrixOperator[] stages;
    private int stageCount;

    public MatrixPipeline(String pipelineName, int capacity) {
        this.pipelineName = pipelineName;
        this.stages = new AbstractMatrixOperator[Math.max(capacity, 1)];
        this.stageCount = 0;
    }

    public String getPipelineName() { return pipelineName; }

    public void addStage(AbstractMatrixOperator op) {
        if (op == null) {
            System.out.println("⚠️ Попередження: порожній оператор не додано.");
            return;
        }
        if (stageCount >= stages.length) {
            System.out.println("⚠️ Попередження: конвеєр заповнений, оператор '" + op.getOperatorName() + "' не додано.");
            return;
        }
        stages[stageCount++] = op;
    }

    public EncapsulatedMatrix runPipeline(EncapsulatedMatrix initial) {
        System.out.println("[ВХІДНА МАТРИЦЯ X0 (" + initial.getSummary() + ")]:");
        System.out.println(initial.getRowsAsTable());

        EncapsulatedMatrix current = initial;
        for (int i = 0; i < stageCount; i++) {
            System.out.println();
            EncapsulatedMatrix next = stages[i].executeSafely(current);
            if (next != current) {
                System.out.println("---> Етап " + (i + 1) + ": Оператор '" + stages[i].getOperatorName()
                        + "' виконано успішно:");
                current = next;
            } else {
                System.out.println("---> Етап " + (i + 1) + ": Оператор '" + stages[i].getOperatorName()
                        + "' пропущено. Поточний стан:");
            }
            System.out.println(current);
        }
        return current;
    }
}

public class Task4MatrixOperatorPipeline {
    public static void main(String[] args) {
        System.out.println("================ КОНВЕЄР МАТРИЧНИХ ОПЕРАТОРІВ ================");

        double[][] source = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        EncapsulatedMatrix x0 = new EncapsulatedMatrix(source);
        source[0][0] = 999.0;
        x0.getGridCopy()[0][1] = -5.0;

        MatrixPipeline pipeline = new MatrixPipeline("Pipeline-1", 4);
        pipeline.addStage(new ScaleMatrixOperator(2.0));
        pipeline.addStage(new TransposeMatrixOperator());
        pipeline.addStage(new DiagonalShiftOperator(3.0));
        pipeline.addStage(new MultiplyByMatrixOperator(new EncapsulatedMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        })));

        pipeline.runPipeline(x0);
        System.out.println("==============================================================");
        System.out.println();
        System.out.println("[ПЕРЕВІРКА ІНКАПСУЛЯЦІЇ]: x0[0][0] = "
                + String.format(Locale.US, "%.2f", x0.getElement(0, 0)) + " (очікується 1.00)");

        System.out.println();
        System.out.println("[ТЕСТ НЕСУМІСНОГО ОПЕРАТОРА]:");
        EncapsulatedMatrix rect = new EncapsulatedMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        EncapsulatedMatrix result = new DiagonalShiftOperator(1.0).executeSafely(rect);
        System.out.println("Матрицю повернуто без змін: " + (result == rect));
    }
}