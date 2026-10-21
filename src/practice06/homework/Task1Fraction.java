package practice06.homework;

class Fraction {
    private int numerator;
    private int denominator;


    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setValues(int num, int den) {
        if (den == 0) {
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            den = 1;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
    }


    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }


    public void simplify() {
        int divisor = gcd(numerator, denominator);
        if (divisor != 0) {
            numerator /= divisor;
            denominator /= divisor;
        }
    }


    public double toDecimal() {
        return (double) numerator / denominator;
    }


    public void add(int otherNum, int otherDen) {
        if (otherDen == 0) {
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            otherDen = 1;
        }
        numerator = numerator * otherDen + otherNum * denominator;
        denominator = denominator * otherDen;
        setValues(numerator, denominator); // нормалізація знака
        simplify();
    }


    public void printFraction() {
        if (denominator == 1) {
            System.out.print(numerator);
        } else {
            System.out.print(numerator + "/" + denominator);
        }
    }
}

public class Task1Fraction {
    public static void main(String[] args) {
        System.out.println("=== КАЛЬКУЛЯТОР ЗВИЧАЙНИХ ДРОБІВ ===");

        Fraction f = new Fraction();
        f.setValues(8, 12);

        System.out.print("Дріб 1: ");
        f.printFraction();
        System.out.println();

        f.simplify();
        System.out.print("Після скорочення: ");
        f.printFraction();
        System.out.printf(" (десятковий вигляд: %.4f)%n", f.toDecimal());

        int addNum = 3, addDen = 4;
        int oldNum = f.getNumerator();
        int oldDen = f.getDenominator();

        System.out.println();
        System.out.println("Додаємо дріб " + addNum + "/" + addDen + " до " + oldNum + "/" + oldDen + "...");
        System.out.println("Формула: " + oldNum + "/" + oldDen + " + " + addNum + "/" + addDen
                + " = (" + oldNum + "*" + addDen + " + " + addNum + "*" + oldDen + ") / ("
                + oldDen + "*" + addDen + ") = "
                + (oldNum * addDen + addNum * oldDen) + "/" + (oldDen * addDen));

        f.add(addNum, addDen);

        System.out.print("Результат додавання: ");
        f.printFraction();
        System.out.printf(" (десятковий вигляд: %.4f)%n", f.toDecimal());
        System.out.println("===================================");
    }
}