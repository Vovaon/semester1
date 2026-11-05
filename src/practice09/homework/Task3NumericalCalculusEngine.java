package practice09.homework;

import java.util.Locale;

abstract class AbstractFunction1D {
    private static final double H = 1e-5;
    private static final int DEFAULT_INTERVALS = 1000;

    private final String expressionName;

    public AbstractFunction1D(String expressionName) {
        this.expressionName = expressionName;
    }

    public String getExpressionName() { return expressionName; }

    public abstract double evaluate(double x);

    public abstract double analyticDerivative(double x);

    public final double numericalDerivative(double x) {
        return (evaluate(x + H) - evaluate(x - H)) / (2 * H);
    }

    public final double getDerivativeError(double x) {
        return Math.abs(analyticDerivative(x) - numericalDerivative(x));
    }

    public final double integrateTrapezoidal(double a, double b, int intervals) {
        int n = (intervals <= 0) ? DEFAULT_INTERVALS : intervals;
        double dx = (b - a) / n;
        double sum = (evaluate(a) + evaluate(b)) / 2.0;
        for (int i = 1; i < n; i++) {
            sum += evaluate(a + i * dx);
        }
        return dx * sum;
    }

    @Override
    public String toString() {
        return expressionName;
    }
}

class PolynomialFunction extends AbstractFunction1D {
    private final double[] coeffs;

    public PolynomialFunction(double[] coeffs) {
        this(coeffs, "g");
    }

    public PolynomialFunction(double[] coeffs, String letter) {
        super(buildName(letter, normalize(coeffs)));

        this.coeffs = normalize(coeffs);
    }

    private static double[] normalize(double[] source) {
        return (source == null || source.length == 0) ? new double[]{0.0} : source.clone();
    }

    private static String buildName(String letter, double[] c) {
        StringBuilder sb = new StringBuilder(letter).append("(x) = ");
        for (int i = 0; i < c.length; i++) {
            if (i > 0) sb.append(" + ");
            if (i == 0) sb.append(String.format(Locale.US, "%.2f", c[i]));
            else if (i == 1) sb.append(String.format(Locale.US, "%.2f*x", c[i]));
            else sb.append(String.format(Locale.US, "%.2f*x^%d", c[i], i));
        }
        return sb.toString();
    }

    public double[] getCoefficients() {
        return coeffs.clone();
    }

    @Override
    public double evaluate(double x) {
        double sum = 0;
        for (int i = 0; i < coeffs.length; i++) {
            sum += coeffs[i] * Math.pow(x, i);
        }
        return sum;
    }

    @Override
    public double analyticDerivative(double x) {
        double sum = 0;
        for (int i = 1; i < coeffs.length; i++) {
            sum += i * coeffs[i] * Math.pow(x, i - 1);
        }
        return sum;
    }
}

class SineWaveFunction extends AbstractFunction1D {
    private final double amplitude;
    private final double omega;
    private final double phase;

    public SineWaveFunction(double amplitude, double omega, double phase) {
        super(String.format(Locale.US, "f(x) = %.2f*sin(%.2f*x + %.2f)", amplitude, omega, phase));
        this.amplitude = amplitude;
        this.omega = omega;
        this.phase = phase;
    }

    @Override
    public double evaluate(double x) {
        return amplitude * Math.sin(omega * x + phase);
    }

    @Override
    public double analyticDerivative(double x) {
        return amplitude * omega * Math.cos(omega * x + phase);
    }
}


class CompositeFunction1D extends AbstractFunction1D {
    private final AbstractFunction1D outer;
    private final AbstractFunction1D inner;

    public CompositeFunction1D(AbstractFunction1D outer, AbstractFunction1D inner) {
        super("(" + requireNonNull(outer).getExpressionName() + " ∘ "
                + requireNonNull(inner).getExpressionName() + ")");
        this.outer = outer;
        this.inner = inner;
    }

    private static AbstractFunction1D requireNonNull(AbstractFunction1D f) {
        if (f == null) throw new IllegalArgumentException("Функція не може бути null");
        return f;
    }

    @Override
    public double evaluate(double x) {
        return outer.evaluate(inner.evaluate(x));
    }

    @Override
    public double analyticDerivative(double x) {
        return outer.analyticDerivative(inner.evaluate(x)) * inner.analyticDerivative(x);
    }
}

public class Task3NumericalCalculusEngine {
    public static void main(String[] args) {
        Locale l = Locale.US;
        System.out.println("================ ЛАБОРАТОРІЯ ЧИСЕЛЬНОГО АНАЛІЗУ ================");

        double[] coeffs = {1.0, 0.0, 1.0};
        PolynomialFunction g = new PolynomialFunction(coeffs, "g");
        coeffs[0] = 999; // перевірка захисного копіювання: на g не впливає

        double x0 = 2.0;
        System.out.println("1. Многочлен " + g.getExpressionName());
        System.out.println(String.format(l, "   Значення g(%.1f) = %.2f", x0, g.evaluate(x0)));
        System.out.println(String.format(l,
                "   Аналітична похідна g'(%.1f) = %.6f | Чисельна = %.6f (Похибка: %.2e)",
                x0, g.analyticDerivative(x0), g.numericalDerivative(x0), g.getDerivativeError(x0)));
        System.out.println(String.format(l,
                "   Інтеграл на [%.2f; %.2f] (метод трапецій, N=1000): %.4f (Точне значення: %.1f)",
                0.0, 3.0, g.integrateTrapezoidal(0, 3, 1000), 12.0));
        System.out.println();

        SineWaveFunction f = new SineWaveFunction(2.0, 1.0, 0.0);
        System.out.println("2. Гармонічна функція " + f.getExpressionName());
        System.out.println(String.format(l, "   Інтеграл на [%.2f; %.4f] (N=1000): %.4f",
                0.0, Math.PI, f.integrateTrapezoidal(0, Math.PI, 1000)));
        System.out.println();

        CompositeFunction1D h = new CompositeFunction1D(f, g);
        double x = 1.5;
        double inner = g.evaluate(x);
        double analytic = h.analyticDerivative(x);
        double numeric = h.numericalDerivative(x);
        double error = h.getDerivativeError(x);

        System.out.println("3. Суперпозиція (Композиція об'єктів) H(x) = f(g(x)) = 2*sin(x^2 + 1):");
        System.out.println(String.format(l, "   Точка x = %.2f:", x));
        System.out.println(String.format(l, "   -> H(%.2f) = 2 * sin(%.2f) = %.6f", x, inner, h.evaluate(x)));
        System.out.println(String.format(l, "   -> Аналітична похідна H'(%.2f) за правилом ланцюга: %.6f", x, analytic));
        System.out.println(String.format(l, "   -> Чисельна похідна H'(%.2f) через кінцеву різницю: %.6f", x, numeric));
        System.out.println(String.format(l, "   -> Розбіжність між формулами: %.2e (%s)", error,
                error < 1e-9 ? "ІДЕАЛЬНИЙ ЗБІГ!" : "ЗБІГ З ПОХИБКОЮ"));
        System.out.println("================================================================");
    }
}
