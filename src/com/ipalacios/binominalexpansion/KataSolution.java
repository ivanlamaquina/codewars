package com.ipalacios.binominalexpansion;

// Binomial expansion
// https://www.codewars.com/kata/540d0fdd3b6532e5c3000b5b/train/java
public class KataSolution {

    public static int triangle (int n, int k) {
        if (k == 0 || k == n) {
            return 1;
        }
        return triangle(n - 1 , k - 1) + triangle (n -1, k);
    }



    public static String expand(String expr) {


        int n = 0;
        int a = 1;
        int b = 1;
        String variable = "";

        // TODO: parsear la entrada
        String[] splitted = expr.split("\\^");
        n = Integer.valueOf(splitted[1]).intValue();
        String binomial = splitted[0].replaceAll("[(,)]","");
        int index = 0;
        if (binomial.charAt(index) == '+') {
            index++;
        }
        if (binomial.charAt(index) == '-') {
            a *= -1;
            index++;
        }

        String coeficientStr = "";
        while(String.valueOf(binomial.charAt(index)).matches("[0-9]")) {
            coeficientStr += binomial.charAt(index);
            index++;
        }
        if (!coeficientStr.isEmpty()) {
            a *= Integer.valueOf(coeficientStr).intValue();
        }
        variable += binomial.charAt(index++);

        if (binomial.charAt(index) == '+') {
            index++;
        }
        if (binomial.charAt(index) == '-') {
            b *= -1;
            index++;
        }
        coeficientStr = "";
        while(index < binomial.length() && String.valueOf(binomial.charAt(index)).matches("[0-9]")) {
            coeficientStr += binomial.charAt(index);
            index++;
        }
        b *= Integer.valueOf(coeficientStr).intValue();




        // Calculo
        int[] valores  = new int[n+1];

                // Los coeficientes se van almacenando en un array de n + 1



        for (int i = 0; i <= n; i++) {
            valores[i] = (int)Math.pow(a, n - i) * (int)Math.pow(b, i);
            valores[i] *= triangle(n, i);
        }

        // TODO: crear la salida a string

        StringBuilder str = new StringBuilder();

        for (int i = 0; i <= n; i++) {
            if (valores[i] > 0 && i > 0) {
                str.append("+");
            }
            if (valores[i] > 1) {
                str.append(String.valueOf(valores[i]));
            }
            if (n - i > 0) {
                str.append(variable);
            }
            if (n - i > 1) {
                str.append("^");
                str.append(String.valueOf(n-i));
            }
        }

        String response = str.toString();
       return response;
    }
}
