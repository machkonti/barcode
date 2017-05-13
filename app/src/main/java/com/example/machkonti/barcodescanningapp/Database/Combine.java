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

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    @Override
    public String toString() {
        return "Combine{" +
                "sellerId=" + sellerId +
                ", bCode='" + bCode + '\'' +
                '}';
    }
}
