package practice10.homework;

import java.util.Locale;

final class ImmutableMatrix2D {
    private final int rows;
    private final int cols;
    private final double[][] data;

    public ImmutableMatrix2D(double[][] source) {
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
            System.arraycopy(source[i], 0, data[i], 0, cols); // глибока копія
        }
    }

    public int getRows() { return rows; }

    public int getCols() { return cols; }

    public double get(int r, int c) { return data[r][c]; }
    public double[][] getDataCopy() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    public double getTrace() {
        requireSquare();
        double sum = 0;
        for (int i = 0; i < rows; i++) {
            sum += data[i][i];
        }
        return sum;
    }

    public double getFrobeniusNorm() {
        double sum = 0;
        for (double[] row : data) {
            for (double v : row) {
                sum += v * v;
            }
        }
        return Math.sqrt(sum);
    }

    public double getDeterminant() {
        requireSquare();
        if (rows == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        if (rows == 3) {
            return data[0][0] * data[1][1] * data[2][2]
                    + data[0][1] * data[1][2] * data[2][0]
                    + data[0][2] * data[1][0] * data[2][1]
                    - data[0][2] * data[1][1] * data[2][0]
                    - data[0][0] * data[1][2] * data[2][1]
                    - data[0][1] * data[1][0] * data[2][2];
        }
        throw new UnsupportedOperationException("Визначник підтримується лише для 2x2 та 3x3");
    }

    public double maxAbsDifference(ImmutableMatrix2D other) {
        if (other == null || other.rows != rows || other.cols != cols) {
            throw new IllegalArgumentException("Розмірності матриць не збігаються");
        }
        double max = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                max = Math.max(max, Math.abs(data[i][j] - other.data[i][j]));
            }
        }
        return max;
    }

    private void requireSquare() {
        if (rows != cols) {
            throw new UnsupportedOperationException("Операція потребує квадратної матриці");
        }
    }
}

interface InvertibleOperator {
    ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed);
    default String describeInverse() {
        return "inverse";
    }
}

interface OrthogonalOperator extends InvertibleOperator {
    default boolean verifyNormInvariance(ImmutableMatrix2D before, ImmutableMatrix2D after) {
        return Math.abs(before.getFrobeniusNorm() - after.getFrobeniusNorm()) < 1e-6;
    }
}

abstract class AbstractMatrixOperator {
    private final String operatorName;

    protected AbstractMatrixOperator(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorName() { return operatorName; }

    public abstract boolean canApply(ImmutableMatrix2D m);

    protected abstract ImmutableMatrix2D transform(ImmutableMatrix2D m);

    public final ImmutableMatrix2D execute(ImmutableMatrix2D m) {
        if (m == null || !canApply(m)) {
            throw new IllegalArgumentException("Оператор '" + operatorName + "' не застосовний до цієї матриці");
        }
        ImmutableMatrix2D result = transform(m);
        if (this instanceof OrthogonalOperator ort && !ort.verifyNormInvariance(m, result)) {
            throw new IllegalStateException("Оператор '" + operatorName + "' порушив збереження норми Фробеніуса");
        }
        return result;
    }
}

class NonZeroScaleOperator extends AbstractMatrixOperator implements InvertibleOperator {
    private final double factor;

    public NonZeroScaleOperator(double factor) {
        super(String.format(Locale.US, "Scale(x%.1f)", factor));
        if (factor == 0) {
            throw new IllegalArgumentException("Множник має бути ненульовим");
        }
        this.factor = factor;
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) { return m != null; }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        return multiply(m, factor);
    }

    @Override
    public ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed) {
        return multiply(transformed, 1.0 / factor);
    }

    @Override
    public String describeInverse() {
        return String.format(Locale.US, "Scale(x%.1f)", 1.0 / factor);
    }

    private static ImmutableMatrix2D multiply(ImmutableMatrix2D m, double k) {
        double[][] d = m.getDataCopy();
        for (double[] row : d) {
            for (int j = 0; j < row.length; j++) {
                row[j] *= k;
            }
        }
        return new ImmutableMatrix2D(d);
    }
}
class TransposeOperator extends AbstractMatrixOperator implements OrthogonalOperator {
    public TransposeOperator() {
        super("Transpose(A^T)");
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) { return m != null; }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        double[][] t = new double[m.getCols()][m.getRows()];
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                t[j][i] = m.get(i, j);
            }
        }
        return new ImmutableMatrix2D(t);
    }

    @Override
    public ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed) {
        return transform(transformed);
    }

    @Override
    public String describeInverse() {
        return "Transpose((A^T)^T)";
    }
}
class GivensRotation2x2Operator extends AbstractMatrixOperator implements OrthogonalOperator {
    private final double angleRadians;

    public GivensRotation2x2Operator(double angleDegrees) {
        super(String.format(Locale.US, "GivensRotation(%.1f°)", angleDegrees));
        this.angleRadians = Math.toRadians(angleDegrees);
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) {
        return m != null && m.getRows() == 2;
    }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        return rotate(m, angleRadians);
    }

    @Override
    public ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed) {
        return rotate(transformed, -angleRadians);
    }

    @Override
    public String describeInverse() {
        return String.format(Locale.US, "GivensRotation(%.1f°)", -Math.toDegrees(angleRadians));
    }

    private static ImmutableMatrix2D rotate(ImmutableMatrix2D m, double theta) {
        double c = Math.cos(theta);
        double s = Math.sin(theta);
        double[][] r = new double[2][m.getCols()];
        for (int j = 0; j < m.getCols(); j++) {
            r[0][j] = c * m.get(0, j) - s * m.get(1, j);
            r[1][j] = s * m.get(0, j) + c * m.get(1, j);
        }
        return new ImmutableMatrix2D(r);
    }
}
class UpperTriangularProjectionOperator extends AbstractMatrixOperator {
    public UpperTriangularProjectionOperator() {
        super("UpperTriangularProjection");
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) { return m != null; }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        double[][] d = m.getDataCopy();
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                if (i > j) {
                    d[i][j] = 0.0;
                }
            }
        }
        return new ImmutableMatrix2D(d);
    }
}

public class Task4PolymorphicMatrixSpectralEngine {

    private static String fmt(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    private static void printMatrix(ImmutableMatrix2D m) {
        for (int i = 0; i < m.getRows(); i++) {
            StringBuilder sb = new StringBuilder("  [ ");
            for (int j = 0; j < m.getCols(); j++) {
                if (j > 0) {
                    sb.append("  ");
                }
                sb.append(fmt("%8.4f", m.get(i, j) + 0.0)); // +0.0 прибирає "-0.0000"
            }
            System.out.println(sb.append(" ]"));
        }
    }

    public static void main(String[] args) {
        ImmutableMatrix2D x0 = new ImmutableMatrix2D(new double[][]{{3.0, 4.0}, {0.0, 5.0}});

        String title = " СПЕКТРАЛЬНИЙ КОНВЕЄР МАТРИЧНИХ ОПЕРАТОРІВ ";
        System.out.println("================" + title + "================");
        System.out.println(fmt("[ПОЧАТКОВА МАТРИЦЯ X0 (%dx%d | Норма: %.4f | Слід: %.2f | det: %.2f)]:",
                x0.getRows(), x0.getCols(), x0.getFrobeniusNorm(), x0.getTrace(), x0.getDeterminant()));
        printMatrix(x0);

        AbstractMatrixOperator[] pipeline = {
                new NonZeroScaleOperator(2.0),
                new TransposeOperator(),
                new GivensRotation2x2Operator(60.0)
        };
        System.out.println();
        System.out.println("--- ПРЯМИЙ ХІД КОНВЕЄРА (Forward Pass через final execute()) ---");
        ImmutableMatrix2D current = x0;
        for (int i = 0; i < pipeline.length; i++) {
            AbstractMatrixOperator op = pipeline[i];
            ImmutableMatrix2D next = op.execute(current);
            if (op instanceof OrthogonalOperator ort) {
                String mark = ort.verifyNormInvariance(current, next) ? "✅" : "❌";
                System.out.println(fmt("%d. Оператор '%s' [Orthogonal: Норма %.4f збережена %s] -> det: %.2f",
                        i + 1, op.getOperatorName(), next.getFrobeniusNorm(), mark, next.getDeterminant()));
            } else {
                System.out.println(fmt("%d. Оператор '%s' -> Норма: %.4f | det: %.2f",
                        i + 1, op.getOperatorName(), next.getFrobeniusNorm(), next.getDeterminant()));
            }
            current = next;
        }
        System.out.println(fmt("[ЗАШИФРОВАНА МАТРИЦЯ Y ПІСЛЯ %d ЕТАПІВ]:", pipeline.length));
        printMatrix(current);
        System.out.println();
        System.out.println("--- ЗВОРОТНИЙ ХІД ВІДНОВЛЕННЯ (Inverse Pass через instanceof InvertibleOperator) ---");
        for (int i = pipeline.length - 1; i >= 0; i--) {
            AbstractMatrixOperator op = pipeline[i];
            if (op instanceof InvertibleOperator inv) {
                current = inv.applyInverse(current);
                System.out.println("<- Декодування '" + inv.describeInverse() + "' виконано...");
            } else {
                System.out.println("<- Оператор '" + op.getOperatorName() + "' необоротний — відновлення неможливе!");
            }
        }

        System.out.println();
        System.out.println("[ВІДНОВЛЕНА МАТРИЦЯ X_restored]:");
        printMatrix(current);
        double err = x0.maxAbsDifference(current);
        System.out.println(fmt("Максимальна похибка відновлення |X0 - X_restored|: %.2e %s", err,
                err < 1e-14 ? "(БЕЗВТРАТНА ОБОРОТНІСТЬ ДОТРИМАНА!)" : "(УВАГА: перевищено допуск 1e-14)"));
        System.out.println("=".repeat(32 + title.length()));
    }
}
