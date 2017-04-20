package com.example.machkonti.barcodescanningapp.Database;

/**
 * Created by Machkonti on 6.4.2017 г..
 */

public class Expires {
    private String bCode;
    private String expDate;
    //    private Date expDate;
    private int daysToNotice;

    public Expires(String bCode, String expDate, int daysToNotice) {
        this.bCode = bCode;
        this.expDate = expDate;
        this.daysToNotice = daysToNotice;
    }

    @Override
    public String toString() {
        return "Expires{" +
                "bCode='" + bCode + '\'' +
                ", expDate='" + expDate + '\'' +
                ", daysToNotice=" + daysToNotice +
                '}';
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public int getDaysToNotice() {
        return daysToNotice;
    }

    public void setDaysToNotice(int daysToNotice) {
        this.daysToNotice = daysToNotice;
    }


}
