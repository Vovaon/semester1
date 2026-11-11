package practice10.homework;

import java.util.Locale;
interface Evaluable1D {
    double EPSILON = 1e-7; // неявно public static final

    double evaluate(double x);
}

interface Integrable1D extends Evaluable1D {
    default double integrateSimpson(double a, double b, int n) {
        if (n < 2) {
            n = 100;
        }
        if (n % 2 != 0) {
            n++;
        }
        double h = (b - a) / n;
        double sumOdd = 0;
        double sumEven = 0;
        for (int i = 1; i < n; i++) {
            double fx = evaluate(a + i * h);
            if (i % 2 == 1) {
                sumOdd += fx;
            } else {
                sumEven += fx;
            }
        }
        return h / 3.0 * (evaluate(a) + evaluate(b) + 4 * sumOdd + 2 * sumEven);
    }
}

interface Differentiable1D extends Evaluable1D {
    double derivative(double x);

    default double numericalDerivative(double x) {
        double h = 1e-5;
        return (evaluate(x + h) - evaluate(x - h)) / (2 * h);
    }
}

interface NewtonRootFindable extends Differentiable1D {
    default double findRootNewton(double x0, int maxIter) {
        double x = x0;
        for (int i = 0; i < maxIter && Math.abs(evaluate(x)) > EPSILON; i++) {
            double d = derivative(x);
            if (Math.abs(d) < 1e-12) {
                break;
            }
            x = x - evaluate(x) / d;
        }
        return x;
    }
}

abstract class AbstractMathFunction implements Integrable1D {
    private final String formulaTitle;

    protected AbstractMathFunction(String formulaTitle) {
        this.formulaTitle = formulaTitle;
    }

    public final String getFormulaTitle() {
        return formulaTitle;
    }
}

final class PolynomialFunction extends AbstractMathFunction implements NewtonRootFindable {
    private final double[] coeffs;

    public PolynomialFunction(double... coeffs) {
        super(buildTitle(coeffs.clone()));
        this.coeffs = coeffs.clone();
    }

    private static String buildTitle(double[] c) {
        StringBuilder sb = new StringBuilder("P(x) = ");
        for (int i = 0; i < c.length; i++) {
            double abs = Math.abs(c[i]);
            if (i == 0) {
                sb.append(String.format(Locale.US, "%.2f", c[i]));
            } else {
                sb.append(c[i] < 0 ? " - " : " + ");
                sb.append(String.format(Locale.US, "%.2f", abs));
                sb.append(i == 1 ? "*x" : "*x^" + i);
            }
        }
        return sb.toString();
    }

    @Override
    public double evaluate(double x) {
        double result = 0;
        for (int i = coeffs.length - 1; i >= 0; i--) {
            result = result * x + coeffs[i];
        }
        return result;
    }

    @Override
    public double derivative(double x) {
        double result = 0;
        for (int i = coeffs.length - 1; i >= 1; i--) {
            result = result * x + i * coeffs[i];
        }
        return result;
    }
    public double exactIntegral(double a, double b) {
        double sum = 0;
        for (int i = 0; i < coeffs.length; i++) {
            sum += coeffs[i] / (i + 1) * (Math.pow(b, i + 1) - Math.pow(a, i + 1));
        }
        return sum;
    }
}

class DampedHarmonyFunction extends AbstractMathFunction implements NewtonRootFindable {
    private final double amplitude;
    private final double decayRate;
    private final double omega;

    public DampedHarmonyFunction(double amplitude, double decayRate, double omega) {
        super(String.format(Locale.US, "D(x) = %.2f * exp(-%.2f*x) * cos(%.2f*x)",
                amplitude, decayRate, omega));
        this.amplitude = amplitude;
        this.decayRate = decayRate;
        this.omega = omega;
    }

    @Override
    public double evaluate(double x) {
        return amplitude * Math.exp(-decayRate * x) * Math.cos(omega * x);
    }

    @Override
    public double derivative(double x) {
        return -amplitude * Math.exp(-decayRate * x)
                * (decayRate * Math.cos(omega * x) + omega * Math.sin(omega * x));
    }
}
class StepHeavisideFunction extends AbstractMathFunction {
    private final double threshold;
    private final double lowValue;
    private final double highValue;

    public StepHeavisideFunction(double threshold, double lowValue, double highValue) {
        super(String.format(Locale.US, "Step(x) = {%.1f при x < %.1f; %.1f при x >= %.1f}",
                lowValue, threshold, highValue, threshold));
        this.threshold = threshold;
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    @Override
    public double evaluate(double x) {
        return x < threshold ? lowValue : highValue;
    }
}

public class Task3NumericalCalculusAndRootPolymorphism {

    private static String fmt(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    public static void main(String[] args) {
        AbstractMathFunction[] functions = {
                new PolynomialFunction(-5.0, -2.0, 0.0, 1.0),   // x^3 - 2x - 5
                new DampedHarmonyFunction(4.0, 0.5, 2.0),
                new StepHeavisideFunction(1.5, 2.0, 5.0)
        };
        int[] steps = {100, 100, 1000};
        double[] derivPoints = {2.0, 1.0, 0.0};
        double[] newtonStarts = {2.0, 0.7, 0.0};

        double a = 0.0;
        double b = 3.0;

        String title = " ЧИСЕЛЬНИЙ АНАЛІЗ: ІНТЕРФЕЙСИ ТА ПОЛІМОРФІЗМ ";
        System.out.println("================" + title + "================");
        System.out.println(fmt("Дослідження поліморфного масиву AbstractMathFunction[%d] на відрізку [%.2f; %.2f]:",
                functions.length, a, b));

        for (int i = 0; i < functions.length; i++) {
            AbstractMathFunction fn = functions[i];
            String title0 = fn.getFormulaTitle();
            String symbol = title0.substring(0, title0.indexOf('('));

            System.out.println();
            String tag = (fn instanceof PolynomialFunction) ? " [final PolynomialFunction]"
                    : " [" + fn.getClass().getSimpleName() + "]";
            System.out.println(fmt("%d. Функція: %s%s", i + 1, title0, tag));
            double integral = fn.integrateSimpson(a, b, steps[i]);
            String exact = (fn instanceof PolynomialFunction p)
                    ? fmt(" (Точне значення: %.6f)", p.exactIntegral(a, b)) : "";
            System.out.println(fmt("   -> Інтеграл Сімпсона на [%.1f; %.1f] (N=%d): %.6f%s",
                    a, b, steps[i], integral, exact));
            if (fn instanceof NewtonRootFindable rf) {
                double x = derivPoints[i];
                System.out.println(fmt("   -> [Differentiable1D] Похідна %s'(%.1f): аналітична = %.6f | чисельна = %.6f",
                        symbol, x, rf.derivative(x), rf.numericalDerivative(x)));

                double root = rf.findRootNewton(newtonStarts[i], 50);
                String note = (fn instanceof DampedHarmonyFunction)
                        ? fmt("pi/4 = %.7f", Math.PI / 4)
                        : fmt("Перевірка %s(x*) = %.2e", symbol, rf.evaluate(root));
                System.out.println(fmt("   -> [NewtonRootFindable] Корінь %s(x) = 0 (старт x0=%.1f): x* = %.7f (%s)",
                        symbol, newtonStarts[i], root, note));
            } else {
                System.out.println("   -> [Info] Функція розривна (не реалізує Differentiable1D / NewtonRootFindable)"
                        + " — пошук дотичних пропущено.");
            }
        }
        System.out.println("=".repeat(32 + title.length()));
    }
}
