package com.devx.naming;

class SimpleClass {
    private int attribute;

    public SimpleClass() {
        attribute = -1;
    }

    public SimpleClass(int attribute) {
        this.attribute = attribute;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }
}
