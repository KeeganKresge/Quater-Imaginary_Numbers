package Number_Systems;
/******************************************************************************
 *  Compilation:  javac Number_Systems.Complex.java
 *  Execution:    java Number_Systems.Complex
 *
 *  Data type for complex numbers.
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Number_Systems.Complex object, you cannot change it. The "final" keyword
 *  when declaring re and im enforces this rule, making it a
 *  compile-time error to change the .re or .im instance variables after
 *  they've been initialized.
 *
 *  % java Number_Systems.Complex
 *  a            = 5.0 + 6.0i
 *  b            = -3.0 + 4.0i
 *  Re(a)        = 5.0
 *  Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *  (a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 ******************************************************************************/

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

import static Tests.QComplexTest.toQComplexTest;

public class Complex {
    private final double re;   // the real part
    private final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Number_Systems.Complex object
    public String toString() {
        DecimalFormat df2=new DecimalFormat("#.##");
        //if (im == 0) return df2.format(re) + "";
        //if (re == 0) return df2.format(im) + "i";
        if (im < 0) return df2.format(re) + " - " + df2.format(-im) + "i";
        return df2.format(re) + " + " + df2.format(im) + "i";
    }

    // return abs/modulus/magnitude
    public double abs() {
        return Math.hypot(re, im);
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    public double phase() {
        return Math.atan2(im, re);
    }

    // return a new Number_Systems.Complex object whose value is (this + b)
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // return a new Number_Systems.Complex object whose value is (this - b)
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // return a new Number_Systems.Complex object whose value is (this * b)
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    public Complex toThe(float b) {
        Complex input;
        if (b < 0) {
            input= this.reciprocal();
            b = -b;
        } else {
            input = this;
        }
        Complex imag = new Complex(0, input.phase() * b).exp();
        return imag.scale(Math.pow(input.abs(), b));
    }

    public Complex round(int places){
        BigDecimal bdr = new BigDecimal(Double.toString(this.re));
        bdr = bdr.setScale(places, RoundingMode.HALF_UP);
        BigDecimal bdi = new BigDecimal(Double.toString(this.im));
        bdi = bdi.setScale(places, RoundingMode.HALF_UP);
        return new Complex(bdr.doubleValue(),bdi.doubleValue());
    }


    public static int[] divmod(int a, int b) {
        int[] result = new int[2];
        result[0] = Math.floorDiv(a, b);
        result[1] = Math.abs(a % b);
        if (a > 0 && b < 0&& result[1]!=0) {
            result[0] += 1;
        }
        if(a<0 &&b<0){
            result[0]+=1;
            result[1]=Math.abs(result[1]+b);
        }
        if(result[1]==4){
            result[1]=0;
            result[0]-=1;
        }

        return result;
    }

    //Change Number_Systems.Complex Number to Number_Systems.QComplex
    public QImag toQComplex(){
        Complex temp =new Complex(this.re,this.im/2);
        temp=temp.round(14);
        double multi=1;
        int[] DivMod;
        double result=0;
        double rDec=temp.re-Math.ceil(temp.re);
        double iDec=temp.im-Math.ceil(temp.im);
        //Note: doubles can have up to 15 decimal places
        if(rDec!=0){
            temp=new Complex(Math.ceil(this.re),temp.im);
            multi=.01;
            for (int i = 0; i <7; i++) {
                rDec*=-4;
                result+=Math.ceil(rDec)*multi;
                rDec=rDec-Math.ceil(rDec);
                if(rDec==0){break;}
                multi/=100;
            }
            multi=1;
        }
        while(!(Arrays.equals(DivMod=divmod((int) temp.re,-4), new int[]{0, (int) temp.re}))&&multi<=1000000000){
            result+=DivMod[1]*multi;
            temp=new Complex(DivMod[0],temp.im);
            multi*=100;
        }
        result+=temp.re*multi;
        //Now handle Imaginary Component
        if(iDec!=0){
            temp=new Complex(this.re,Math.ceil(temp.im));
            multi=.1;
            for (int i = 0; i <8; i++) {
                iDec*=-4;
                result+=Math.ceil(iDec)*multi;
                iDec=iDec-Math.ceil(iDec);
                if(iDec==0){break;}
                multi/=100;
            }
        }
        DivMod=divmod((int) temp.im,-4);
        multi= 10;
        while(!(Arrays.equals(DivMod, new int[]{0, (int) temp.im}))&&multi<=1000000000){
            DivMod=divmod((int) temp.im,-4);
            result+=DivMod[1]*multi;
            temp=new Complex(0,DivMod[0]);
            multi*=100;
        }
        result+=temp.im*multi;
        //Shift Imag part right by one place
        return new QImag(result);
    }

    // return a new object whose value is (this * alpha)
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new Number_Systems.Complex object whose value is the conjugate of this
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    // return a new Number_Systems.Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = re * re + im * im;
        return new Complex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() {
        return re;
    }

    public double im() {
        return im;
    }

    // return a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Number_Systems.Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new Number_Systems.Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new Number_Systems.Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new Number_Systems.Complex object whose value is the complex tangent of this
    public Complex tan() {
        return sin().divides(cos());
    }


    // a static version of plus
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

    // See Section 3.3.
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    // See Section 3.3.
    public int hashCode() {
        return Objects.hash(re, im);
    }

    // sample client for testing
    public static void main(String[] args) {
        String qnum ="22222.33333"; //baseConversion(String.valueOf(2187), 10, 3);
        System.out.println(qnum);
        toQComplexTest(Double.parseDouble(qnum));
        /*
        Complex a = new Complex(5, 6);// 5+6i
        Complex b = new Complex(-3.0, 4.0);
        System.out.println("a            = " + a);
        System.out.println("b            = " + b);
        System.out.println("Re(a)        = " + a.re());
        System.out.println("Im(a)        = " + a.im());
        System.out.println("b + a        = " + b.plus(a));
        System.out.println("a - b        = " + a.minus(b));
        System.out.println("a * b        = " + a.times(b));
        System.out.println("b * a        = " + b.times(a));
        System.out.println("a / b        = " + a.divides(b));
        System.out.println("(a / b) * b  = " + a.divides(b).times(b));
        System.out.println("conj(a)      = " + a.conjugate());
        System.out.println("|a|          = " + a.abs());
        System.out.println("tan(a)       = " + a.tan());
        System.out.println("a^(-1/4)     = " + a.toThe((float) -.25));*/
    }

}

//Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
//Last updated: Fri Oct 20 14:12:12 EDT 2017.
//Edits made by Keegan Kresge