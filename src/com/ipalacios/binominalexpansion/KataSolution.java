package com.ipalacios.binominalexpansion;

import java.util.function.BinaryOperator;

// Binomial expansion
// https://www.codewars.com/kata/540d0fdd3b6532e5c3000b5b/train/java
public class KataSolution {

    public static class Binomial {
        // Represents a binomial (ax+b)^n
        private int a = 1, b = 1;
        private int n;
        private char variable;
        private long[] values;

        public Binomial(String expression) {

            String[] splitted = expression.split("\\^");
            this.n = Integer.valueOf(splitted[1]).intValue();
            String binomial = splitted[0].replaceAll("[(,)]","");
            int index = 0;
            if (binomial.charAt(index) == '+') {
                index++;
            }
            if (binomial.charAt(index) == '-') {
                this.a *= -1;
                index++;
            }

            String coeficientStr = "";
            while(String.valueOf(binomial.charAt(index)).matches("[0-9]")) {
                coeficientStr += binomial.charAt(index);
                index++;
            }
            if (!coeficientStr.isEmpty()) {
                this.a *= Integer.valueOf(coeficientStr).intValue();
            }
            this.variable += binomial.charAt(index++);

            if (binomial.charAt(index) == '+') {
                index++;
            }
            if (binomial.charAt(index) == '-') {
                this.b *= -1;
                index++;
            }
            coeficientStr = "";
            while(index < binomial.length() && String.valueOf(binomial.charAt(index)).matches("[0-9]")) {
                coeficientStr += binomial.charAt(index);
                index++;
            }
            this.b *= Integer.valueOf(coeficientStr).intValue();
        }

        public Binomial compute() {
            this.values  = new long[n+1];

            for (int i = 0; i <= n; i++) {
                this.values[i] = (long)Math.pow(a, n - i) * (long)Math.pow(b, i);
                this.values[i] *= triangle(n, i);
            }
            return this;
        }

        public String toString() {
            StringBuilder str = new StringBuilder();


            for (int i = 0; i <= n; i++) {
                if (this.values[i] != 0) {
                    if (this.values[i] > 0 && i > 0) {
                        str.append("+");
                    }
                    if (this.values[i] == -1 && i < n) {
                        str.append("-");
                    }
                    if (this.values[i] > 1 || this.values[i] < -1 || i == n) {
                        str.append(String.valueOf(this.values[i]));
                    }
                    if (n - i > 0) {
                        str.append(variable);
                    }
                    if (n - i > 1) {
                        str.append("^");
                        str.append(String.valueOf(n - i));
                    }
                }
            }

            return str.toString();
        }

        private int triangle (int n, int k) {
            if (k == 0 || k == n) {
                return 1;
            }
            return triangle(n - 1 , k - 1) + triangle (n -1, k);
        }

    }

    public static String expand(String expr) {
        return new Binomial(expr).compute().toString();
    }
}
