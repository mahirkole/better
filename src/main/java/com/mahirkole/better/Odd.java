package com.mahirkole.better;

public class Odd {
    private OddType type;
    private OddResult result;
    private Short mbs;
    private Double rate;

    public Odd(OddType type, OddResult result, Short mbs, Double rate) {
        this.type = type;
        this.result = result;
        this.mbs = mbs;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Odd{" +
                "type=" + type +
                ", result=" + result +
                ", mbs=" + mbs +
                ", rate=" + rate +
                '}';
    }
}
