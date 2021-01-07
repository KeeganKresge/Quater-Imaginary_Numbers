package Number_Systems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class QImag {
    private double num;
    private final static Complex base = new Complex(0, 2);

    /**
     * Constructor
     *
     * @param num number in quater-imaginary base
     */
    public QImag(double num) {
        this.num = num;
    }

    /**
     * Convert this Number_Systems.QComplex number into its COmplex counterpart
     */
    public Complex toComplex() {
        String str = String.valueOf( num);
        Complex result = new Complex(0, 0);
        String[] strList = new String[2];
        strList[0] = str;
        if(str.contains("E")){throw new ArithmeticException("QComplex number too big to be converted into a Complex number");}
        if(strList[0].charAt(0)=='-'){
            throw new ArithmeticException("QComplex numbers cannot be negative");
        }
        if (str.contains(".")) {
            strList = str.split("\\.");
            for (int i = 0; i < String.valueOf(strList[1]).length(); i++) {
                result = result.plus(base.toThe(-i - 1).times(new Complex(Long.parseLong(String.valueOf(strList[1].charAt(i))), 0)));
            }
        }

        for (int i = 0; i < String.valueOf(strList[0]).length(); i++) {
            result = result.plus(base.toThe(strList[0].length() - i - 1).times(new Complex(Integer.parseInt(String.valueOf(strList[0].charAt(i))), 0)));
        }
        return result;
    }

    /**
     * Test if two Quater-imaginary numbers are equal
     */
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        QImag that = (QImag) x;
        return (this.num == that.num);
    }

    /**
     * Hash code for Object
     */
    public int hashCode() {
        return Objects.hash(num);
    }

/**
 * Round to specified decimal place
 * @return
 */
public QImag round(int places){
    BigDecimal bigDecimal = new BigDecimal(Double.toString(num));
    bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
    this.num= bigDecimal.doubleValue();
    return this;
}


    /**
     * Number_Systems.QComplex toString
     */
    @Override
    public String toString() {
        DecimalFormat df2 = new DecimalFormat("#.######");
        return "0q" + df2.format(num);
    }

    /**
     * Add one QComplex number to another
     *
     */
    public QImag add(QImag QC){
        String thisString=this.toString();
        thisString=thisString.substring(2);
        StringBuilder otherStringB= new StringBuilder(QC.toString());
        otherStringB = new StringBuilder(otherStringB.substring(2));
        int tDec=thisString.indexOf('.');
        int oDec= otherStringB.toString().indexOf('.');
        thisString = ("0".repeat(6 - tDec)) + thisString;
        otherStringB.insert(0, ("0".repeat(6 - oDec)));
        if(thisString.length()<otherStringB.length()){
          String temp = thisString;
          otherStringB = new StringBuilder(thisString);
          thisString=temp;
        }
        while(otherStringB.length()<thisString.length()){
            otherStringB.append("0");
        }
        //The strings are the same length, now we can add them
        ArrayList<Integer> result=new ArrayList<>();
        for (int i = 0; i < thisString.length(); i++) {
            result.add(0);
        }
        thisString.replace(".","");
        String otherString=otherStringB.toString();
        otherString.replace(".","");
        for (int i = thisString.length()-1; i >0 ; i--) {
            int a1=Integer.parseInt(String.valueOf(thisString.charAt(i)));
            int a2= Integer.parseInt(String.valueOf(otherString.charAt(i)));
            result.set(i, a1 + a2);
        }
        result.add(0,0);
        result.add(0,0);
        for (int i = 2; i < result.size(); i++) {
            if(result.get(i) >=4){
                result.set(i, result.get(i)-4);
                result.set(i - 2, result.get(i-2)-1);}
        }
        result.add(0,0);
        result.add(0,0);
        for (int i = 0; i < result.size(); i++) {
            if(result.get(i) <0){
                result.set(i, result.get(i)+4);
                result.set(i - 2, result.get(i-2)+1);}
        }
        StringBuilder resultS= new StringBuilder();
        for (int num:result) {
            resultS.append(num);
        }
        return new QImag(Double.parseDouble(resultS.toString()));
    }

    public QImag subtract(QImag other){
        return this.add(other.toComplex().times(new Complex(-1,0)).toQComplex());
    }

    public static void main(String[] args) {
        QImag a = new QImag(1);
        QImag b = new QImag(100);
        QImag c= b.subtract(b);
        System.out.println("a (q)        = " + a);
        System.out.println("b (q)        = " + b);
        System.out.println("a (q)        = " + a.toComplex());
        System.out.println("b (c)        = " + b.toComplex());
        System.out.println("c (q)        = " + c);
        System.out.println("c (c)        = " + c.toComplex());
    }
}
