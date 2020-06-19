package com.ipalacios.eqsystem;

import java.util.HashMap;
import java.util.Map;

// Simplifying
// https://www.codewars.com/kata/57f2b753e3b78621da0020e8/train/java
public class EqSystem {

    public static String doSimplify(Map<String, Map<String, Integer>> examplesMap, Map<String, Integer> formula) {

        if (formula.keySet().size() == 1) {
            String variable = formula.keySet().iterator().next();
            if (!examplesMap.containsKey(variable)) {
                String result = "" + formula.get(variable) + variable;
                return result;
            }
        }

        Map<String, Integer> newFormula = new HashMap<>();
        for (Map.Entry<String, Integer> entry : formula.entrySet()) {
            newFormula.put(new String(entry.getKey()), new Integer(entry.getValue()));
        }



        for (Map.Entry<String, Integer> entry: formula.entrySet()) {

            if (examplesMap.containsKey(entry.getKey())) {
                int coeficient = entry.getValue();

                if (newFormula.containsKey(entry.getKey())) {
                    coeficient = newFormula.get(entry.getKey());
                    newFormula.remove(entry.getKey());
                }
                for (Map.Entry<String, Integer> exampleEntry: examplesMap.get(entry.getKey()).entrySet()) {
                    String key = exampleEntry.getKey();
                    int currentValue = 0;
                    if (newFormula.containsKey(key)) {
                        currentValue = newFormula.get(key);
                    }
                    int newValue = currentValue + exampleEntry.getValue() * coeficient;
                    if (newValue == 0) {
                        newFormula.remove(exampleEntry.getKey());
                    } else {
                        newFormula.put(key, newValue);
                    }

                }
            }
        }
        return doSimplify(examplesMap, newFormula);
    }

    public static Map<String, Integer> getFactors(String parse) {

        StringQueue parseString = new StringQueue(parse);

        Map<String, Integer> factors = new HashMap<>();

        while(!parseString.isEmpty()) {

            char ch = parseString.pop();
            Integer coeficient = 1;
            String variable = null;

            // Optional +/- sign
            if (ch == '+') {
                ch = parseString.pop();
            } else if (ch == '-') {
                coeficient = -1;
                ch = parseString.pop();
            }

            // gets optional coeficient
            String number = "";
            while(String.valueOf(ch).matches("[0-9]")) {
                number += ch;
                ch = parseString.pop();
            }
            if (!number.isEmpty()) {
                coeficient *= Integer.valueOf(number);
            }

            // Variable name or expression
            if (String.valueOf(ch).matches("[A-Za-z]")) {
                variable = "" + ch;

                if (factors.containsKey(variable)) {
                    coeficient += factors.get(variable);
                }
                if (coeficient != 0) {
                    factors.put(variable, coeficient);
                } else {
                    factors.remove(variable);
                }
            } else if (ch == '(') {
                String innerStr = "";
                int numPar = 1;
                ch = parseString.pop();
                while (ch != ')' || numPar != 0) {
                    innerStr += ch;
                    ch = parseString.pop();
                    if (ch == '(') {
                        numPar++;
                    } else if (ch == ')') {
                        numPar--;
                    }
                }
                Map<String, Integer> innerFactors = getFactors(innerStr);
                for (Map.Entry<String, Integer> inner: innerFactors.entrySet()) {
                    Integer value = coeficient * inner.getValue();
                    if (factors.containsKey(inner.getKey())) {
                        value += factors.get(inner.getKey());
                    }
                    if (value == 0) {
                        factors.remove(inner.getKey());
                    } else {
                        factors.put(inner.getKey(), value);
                    }
                }
            }
        }
        return factors;
    }

    public static String simplify(String[] examples, String formula) {

        Map<String, Map<String, Integer>> examplesMap = new HashMap<>();
        for(String example: examples) {
            String[] splitString = example.trim().split("=");
            String variable = splitString[1].trim();
            Map<String, Integer> factors = getFactors(splitString[0]);
            examplesMap.put(variable, factors);
        }

        Map<String, Integer> formulaMap = getFactors(formula);


        return doSimplify(examplesMap, formulaMap);
    }
}

class StringQueue {

    private String str;

    public StringQueue(String str) {
        this.str = str.replaceAll("\\s","");
    }
    public char pop() {
        if (str.length() > 0) {
            char ch = str.charAt(0);
            this.str = this.str.substring(1);
            return ch;
        }
        return '\0';
    }

    public boolean isEmpty() {
        return this.str.length() == 0;
    }
}