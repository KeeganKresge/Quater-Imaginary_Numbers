package Tests;

import Number_Systems.Complex;
import Number_Systems.QImag;

import java.util.Arrays;

public class QComplexTest {
    public static void genericDivmodTest(int a, int b, int[] expect) {
        int[] result = Complex.divmod(a, b);
        if (Arrays.equals(result, expect)) {
            System.out.print(" PASS");
        } else {
            System.out.print("\nTEST FAILED: divmod(" + a + "," + b + ")=" + Arrays.toString(result) + ", but expected " + Arrays.toString(expect));
        }
    }

    public static void toQComplexTest(double qnum) {
        QImag start = new QImag(qnum);
        QImag end = start.toComplex().toQComplex().round(11);
        if (start.equals(end)) {
            System.out.print(" PASS");
        } else {
            System.out.print("\nTEST FAILED: qnum=" + start + ", cnum=" + start.toComplex() + ", result=" + end);
        }
    }

    public static String baseConversion(String number,
                                        int sBase, int dBase) {
        // Parse the number with source radix
        // and return in specified radix(base)
        return Integer.toString(
                Integer.parseInt(number, sBase), dBase);
    }


    public static void main(String[] args) {
        QComplexTest.genericDivmodTest(35, -4, new int[]{-8, 3});
        QComplexTest.genericDivmodTest(-8, -4, new int[]{2, 0});
        QComplexTest.genericDivmodTest(2, -4, new int[]{0, 2});
        QComplexTest.genericDivmodTest(12, -4, new int[]{-3, 0});
        QComplexTest.genericDivmodTest(-3, -4, new int[]{1, 1});
        QComplexTest.genericDivmodTest(1, -4, new int[]{0, 1});
        QComplexTest.genericDivmodTest(0, -4, new int[]{0, 0});
        QComplexTest.genericDivmodTest(7, -4, new int[]{-1, 3});
        QComplexTest.genericDivmodTest(-7, -4, new int[]{2, 1});
        System.out.println("\ntoQComplex Tests");
        toQComplexTest(201);
        for (int i = 0; i < 1000; i++) {
            toQComplexTest(Integer.parseInt(baseConversion(String.valueOf(i), 10, 3)));
        }


    }
}
