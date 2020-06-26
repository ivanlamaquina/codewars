package com.ipalacios.tinycompiler;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Tiny Three-Pass Compiler
// https://www.codewars.com/kata/5265b0885fda8eac5900093b/train/java
public class Compiler {
    
    public final static String IMM = "imm";
    public final static String ARG = "arg";

    public List<String> compile(String prog) {
        return pass3(pass2(pass1(prog)));
    }


    /**
     * Returns an un-optimized AST
     */
    public Ast pass1(String prog) {
        Deque<String> tokens = tokenize(prog);
        List<String> args = new ArrayList<>();

        // Gets args
        tokens.pop();
        String token = tokens.pop();
        while (!token.equals("]")) {
            args.add(token);
            token = tokens.pop();
        }

        // Gets expression
        Ast ast = toAst(tokens, args);


        return ast;
    }

    /**
     * Returns an AST with constant expressions reduced
     */
    public Ast pass2(Ast ast) {
        return reduce(ast);
    }

    /**
     * Returns assembly instructions
     */
    public List<String> pass3(Ast ast) {
        return toAssembly(ast);
    }

    private static Deque<String> tokenize(String prog) {
        Deque<String> tokens = new LinkedList<>();
        Pattern pattern = Pattern.compile("[-+*/()\\[\\]]|[a-zA-Z]+|\\d+");
        Matcher m = pattern.matcher(prog);
        while (m.find()) {
            tokens.add(m.group());
        }
        //tokens.add("$"); // end-of-stream
        return tokens;
    }

    private boolean isNumber(String str) {
        return str.matches("[0-9]+");
    }

    private boolean isVariable(String str) {
        return str.matches("[A-Za-z]");
    }

    private Ast toAst(Deque<String> tokens, List<String> args) {
        Ast ast = null;
        String operator = null;
        Ast a, b;
        String token;

        Deque<String> expression = new LinkedList<>();
        if (tokens.getLast().equals(")")) {
            tokens.removeLast();
            int par = 1;
            while(par > 0) {
                token = tokens.removeLast();
                if (token.equals(")")) {
                    par++;
                } else if (token.equals("(")) {
                    par--;
                } else {
                    expression.push(token);
                }
            }
        } else if (tokens.contains("+") || tokens.contains("-")) {
            while(!tokens.getLast().equals("+") && !tokens.getLast().equals("-")) {
                token = tokens.removeLast();
                expression.push(token);
            }
        } else if (tokens.contains("*") || tokens.contains("/")) {
            while(!tokens.getLast().equals("*") && !tokens.getLast().equals("/")) {
                token = tokens.removeLast();
                expression.push(token);
            }
        } else {
            if (isNumber(tokens.getLast())) {
                return new UnOp(IMM, Integer.valueOf(tokens.pop()).intValue());
            } else if (isVariable(tokens.getLast())) {
                return new UnOp(ARG, args.indexOf(tokens.pop()));
            }
        }
        b = toAst(expression, args);
        if (tokens.isEmpty()) {
            return b;
        }
        operator = tokens.removeLast();
        a = toAst(tokens, args);
        if (operator != null) {
            ast = new BinOp(operator, a, b);
        }

        return ast;
    }

    private Ast reduce(Ast ast) {
        if (ast instanceof BinOp) {
            BinOp bin = (BinOp) ast;
            Ast ast1 = bin.a();
            Ast ast2 = bin.b();
            if (!(ast1 instanceof UnOp) || !(ast2 instanceof UnOp)) {
                ast1 = reduce(ast1);
                ast2 = reduce(ast2);
            }
            if (ast1 instanceof UnOp && ast2 instanceof UnOp) {
                UnOp a = (UnOp) ast1;
                UnOp b = (UnOp) ast2;
                if (a.op().equals(IMM) && b.op().equals(IMM)) {
                    switch(bin.op()) {
                        case "+":
                            return new UnOp(IMM, a.n() + b.n());
                        case "-":
                            return new UnOp(IMM,a.n() - b.n());
                        case "*":
                            return new UnOp(IMM, a.n() * b.n());
                        case "/":
                            return new UnOp(IMM, a.n() / b.n());
                    }
                }
            }
            return new BinOp(bin.op(), ast1, ast2);
        }
        return ast;
    }

    private List<String> toAssembly(Ast ast) {
        List<String> commands = new ArrayList<>();
        if (ast instanceof BinOp) {
            BinOp bin = (BinOp) ast;
            commands.addAll(toAssembly(bin.a()));
            commands.add("PU");
            commands.addAll(toAssembly(bin.b()));
            commands.add("SW");
            commands.add("PO");
            switch(ast.op()) {
                case "+":
                    commands.add("AD");
                    break;
                case "-":
                    commands.add("SU");
                    break;
                case "*":
                    commands.add("MU");
                    break;
                case "/":
                    commands.add("DI");
                    break;
            }
        } else if (ast instanceof UnOp) {
            UnOp un = (UnOp)ast;
            switch(un.op()) {
                case ARG:
                    commands.add("AR " + un.n());
                    break;
                case IMM:
                    commands.add("IM " + un.n());
                    break;
            }

        }
        return commands;
    }

}
