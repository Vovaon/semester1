package semester1.practice02.homework;

public class Task3TriangleClassifier {
    public static void main(String[] args) {
        double a = 3.0;
        double b = 4.0;
        double c = 5.0;
        final double EPSILON = 0.000001;

        boolean exists = (a + b > c) && (a + c > b) && (b + c > a);
        boolean isEquilateral = exists && (a == b) && (b == c);
        boolean isIsosceles = exists && ((a == b) || (b == c) || (a == c));
        boolean isRight = exists
                && ((Math.abs(Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) < EPSILON)
                || (Math.abs(Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2)) < EPSILON)
                || (Math.abs(Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2)) < EPSILON));

        System.out.println("Сторони трикутника: a = " + a + ", b = " + b + ", c = " + c);
        System.out.println("--------------------------------------------------");
        System.out.println("Чи існує такий трикутник:      " + exists);
        System.out.println("Чи є рівностороннім:           " + isEquilateral);
        System.out.println("Чи є рівнобедреним:            " + isIsosceles);
        System.out.println("Чи є прямокутним:              " + isRight);
    }
}
