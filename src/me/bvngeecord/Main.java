package me.bvngeecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.DoubleStream;

public class Main {

    public static List<Double> factors = new ArrayList<>();
    public static double[] coefficients;

    public static void main(String[] args) {
        //1x+2x-9x-18
        //2x-12x+8x-0
        //1x+0x+1x+0
        System.out.println(solveCubic());
    }

    public static List<String> solveCubic(){
        getTerms();
        if (coefficients[coefficients.length-1] == 0){
            return quadraticFormula(coefficients[0], coefficients[1], coefficients[2]);
        }
        final List<String> possibleFactors = findPossibleFactors(Math.abs(coefficients[0]), Math.abs(coefficients[3]));
        System.out.println(possibleFactors);
        List<String> factors = new ArrayList<>();
        for (String possibleFactor : possibleFactors){
            double j = Double.parseDouble(possibleFactor);
            if (testPossibleFactor(j, coefficients)) factors.add(possibleFactor);
        }
        if (factors.toArray().length < coefficients.length-1){
            for (String possibleFactor : possibleFactors){
                double h = Math.sqrt(Double.parseDouble(possibleFactor));
                System.out.println(h);
                if (h<0) continue;
                if (testPossibleFactor(h, coefficients)) factors.add("±" + possibleFactor);
            }
        }
        return factors;
    }

    public static List<String> findPossibleFactors(double a, double d){
        int[] aFactors = combinationsOfFactors(factorsOf((int)a));
        int[] dFactors = combinationsOfFactors(factorsOf((int)d));
        List<String> possibleFactors = new ArrayList<>();
        for (int dFactor : dFactors) {
            for (int aFactor : aFactors) {
                double positive = (double) dFactor / aFactor;
                double negative = -1 * positive;
                if (!possibleFactors.contains(String.valueOf(positive))) possibleFactors.add(String.valueOf(positive));
                if (!possibleFactors.contains(String.valueOf(negative))) possibleFactors.add(String.valueOf(negative));
            }
        }
        return possibleFactors;
    }

    public static boolean testPossibleFactor(double factor, double[] coefficients){
        double[] newCoefficients = new double[4];
        double temp = 0;
        for (int i = 0; i < 4; i++) {
            newCoefficients[i] = coefficients[i] + temp;
            temp = factor * (newCoefficients[i]);
        }
                //if (!(temp < 0.01 && temp > -0.01)) System.out.println("hi " + Arrays.toString(newCoefficients));
                //if (newCoefficients[coefficients.length-1] < 0.01 && newCoefficients[coefficients.length-1] > -0.01) System.out.println("New Coeffs found");
        return (temp < 0.01 && temp > -0.01);
    }

    public static List<String> quadraticFormula(double a, double b, double c){
        List<String> output = new ArrayList<>();
        output.add("0");
        double fourAC = (b*b) - (4*a*c);
        double rootedTerm = Math.sqrt(fourAC);

        if (Double.isNaN(rootedTerm)) {
            // NaN
            fourAC = Math.abs(fourAC);
            output.add("( " + (-1*b) + " ± (i)sqrt(" + fourAC + ") ) / " + 2*a);
        }else if (rootedTerm - Math.floor(rootedTerm) == 0){
            // Perfect Square
            for (int i = 0; i < 2; i++){
                output.add(String.valueOf(((-1 * b) + (Math.pow(-1, i) * (rootedTerm))) / (2*a)));
            }
        }else {
            // Normal
            output.add("( " + (-1*b) + " ± sqrt(" + fourAC + ") ) / " + 2*a);
        }
        return output;

    }

    public static int[] factorsOf(int n) {
        List<Integer> factors = new ArrayList<>();
        factors.add(1);
        int original = n;
        while (n % 2 == 0) {
            factors.add(2);
            n /= 2;
        }
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 2)
           factors.add(n);
        factors.add(original);
        return factors.stream().mapToInt(j -> j).toArray();
    }

    public static int[] combinationsOfFactors(int[] list){
        List<Integer> combos = new ArrayList<>();
        for (int n : list) combos.add(n);
        for (int i = 1; i < list.length-1; i++){
            for (int j = 0; j < list.length-1; j++) {
                if (j != i && !combos.contains(list[i]*list[j])) combos.add(list[i]*list[j]);
            }
        }
        return combos.stream().mapToInt(n -> n).toArray();
    }

    public static void getTerms(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input a cubic equation in the form of ax+bx+cx+d, in order of decending powers. Include all coefficients including 0s and 1s. Do not type the powers.");
        String input = scanner.nextLine();
        coefficients = Arrays.stream(input.split("x+")).mapToDouble(Double::parseDouble).toArray();
    }
}