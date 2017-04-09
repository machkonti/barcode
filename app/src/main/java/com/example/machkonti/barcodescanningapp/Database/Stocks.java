package com.example.machkonti.barcodescanningapp.Database;

import java.sql.Date;

/**
 * Created by machkonti on 30.3.2017 г..
 */

public class Stocks {
    private String name;
    private String bCode;


    public Stocks() {}

    public Stocks(String bCode, String name) {
        this.name = name;
        this.bCode = bCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "name='" + name + '\'' +
                ", bCode='" + bCode + '\'' +
                '}';
    }
}
