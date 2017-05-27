package com.example.machkonti.barcodescanningapp.Database;

/**
 * Created by machkonti on 10.5.2017 г..
 */

public class Combine {
    private int sellerId;
    private String bCode;

    public Combine(int sellerId, String bCode) {
        this.sellerId = sellerId;
        this.bCode = bCode;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getbCode() {
        return bCode;
    }

    @Override
    public String toString() {
        return "Combine{" +
                "sellerId=" + sellerId +
                ", bCode='" + bCode + '\'' +
                '}';
    }
}
