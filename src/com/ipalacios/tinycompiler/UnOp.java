package com.ipalacios.tinycompiler;

import java.util.Objects;

public class UnOp implements Ast {

    private String source;
    private int n;

    public UnOp(String source, int n) {
        this.source = source;
        this.n = n;
    }

    public int n() {
        return this.n;
    }

    @Override
    public String op() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnOp)) return false;
        UnOp unOp = (UnOp) o;
        return n == unOp.n &&
                Objects.equals(source, unOp.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, n);
    }
}
