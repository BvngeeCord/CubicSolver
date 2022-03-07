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
        /*
2x-12x+8x-0
1x+0x-12x-3
24x+12x-95x+34
3x-14x+7x+96
1x-5x+10x-23
2x-14x+3x-0
1x+0x+0x+1
1x+0x+0x-1
         */
        System.out.println(solveCubic());
        //System.out.println(Arrays.toString(quadraticFormula(1, 4, -16)));
        //System.out.println(testPossibleFactor((double) Math.sqrt(1.5), new int[] {2, 4, -3, -6}));
    }

    public static List<String> solveCubic(){
        getTerms();
        if (coefficients[coefficients.length-1] == 0){
            List<String> quadraticFormula = DoubleStream.of(quadraticFormula(coefficients[0], coefficients[1], coefficients[2])).boxed().map(String::valueOf).toList();
            List<String> output = new ArrayList<>(quadraticFormula);
            output.add("0");
            return output;
        }
        final List<String> possibleFactors = findPossibleFactors(Math.abs(coefficients[0]), Math.abs(coefficients[3]));
        System.out.println(possibleFactors);
        List<String> factors = new ArrayList<>();
        for (String possibleFactor : possibleFactors){
            double j = Double.parseDouble(possibleFactor);
            if (testPossibleFactor(j, coefficients)) factors.add(String.valueOf(j));
        }
        //System.out.println(factors);
        if (factors.toArray().length < coefficients.length){
            for (String possibleFactor : possibleFactors){
                double h = Math.sqrt(Double.parseDouble(possibleFactor));
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
                //if (!possibleFactors.contains(String.valueOf(positive))) possibleFactors.add(String.valueOf(positive));
                //if (!possibleFactors.contains(String.valueOf(negative))) possibleFactors.add(String.valueOf(negative));
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
                if (temp < 0.01 && temp > -0.01) System.out.println("hi " + Arrays.toString(newCoefficients));
                if (newCoefficients[coefficients.length-1] < 0.01 && newCoefficients[coefficients.length-1] > -0.01) System.out.println("New Coeffs found");
        return (temp < 0.01 && temp > -0.01);
    }

    public static double[] quadraticFormula(double a, double b, double c){
        double[] output = new double[2];  
        for (int i = 0; i < 2; i++){
          output[i] = ((-1 * b) + (Math.pow(-1, i) * (Math.sqrt((b*b) - (4*a*c))))) / (2*a);
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