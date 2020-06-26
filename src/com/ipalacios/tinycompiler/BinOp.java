package com.ipalacios.tinycompiler;

import java.util.Objects;

public class BinOp implements Ast {

    private String operator;
    private Ast a, b;

    public BinOp(String operator, Ast a, Ast b) {
        this.operator = operator;
        this.a = a;
        this.b = b;
    }

    public Ast a() {
        return a;
    }

    public Ast b() {
        return b;
    }

    @Override
    public String op() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinOp)) return false;
        BinOp binOp = (BinOp) o;
        return Objects.equals(operator, binOp.operator) &&
                Objects.equals(a, binOp.a) &&
                Objects.equals(b, binOp.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, a, b);
    }
}
