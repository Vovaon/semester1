package semester1.practice02.homework;

public class Task4HeronGeometry {
    public static void main(String[] args) {
        double a = 7.0;
        double b = 8.0;
        double c = 9.0;

        double perimeter = a + b + c;
        double semiPerimeter = perimeter / 2.0;
        double triangleArea = Math.sqrt(semiPerimeter
                * (semiPerimeter - a)
                * (semiPerimeter - b)
                * (semiPerimeter - c));

        double inscribedRadius = triangleArea / semiPerimeter;
        double inscribedCircleArea = Math.PI * Math.pow(inscribedRadius, 2);

        double circumscribedRadius = (a * b * c) / (4.0 * triangleArea);
        double circumscribedCircleArea = Math.PI * Math.pow(circumscribedRadius, 2);

        System.out.println("================ ГЕОМЕТРИЧНИЙ ЗВІТ ===============");
        System.out.println("Сторони: a = " + a + ", b = " + b + ", c = " + c);
        System.out.println("Периметр (P):               " + perimeter);
        System.out.println("Напівпериметр (p):          " + semiPerimeter);
        System.out.println("Площа трикутника (S):       " + triangleArea + " кв. од.");
        System.out.println("Радіус вписаного кола (r):  " + inscribedRadius);
        System.out.println("Площа вписаного круга:      " + inscribedCircleArea + " кв. од.");
        System.out.println("Радіус описаного кола (R):  " + circumscribedRadius);
        System.out.println("Площа описаного круга:      " + circumscribedCircleArea + " кв. од.");
        System.out.println("==================================================");
    }
}
