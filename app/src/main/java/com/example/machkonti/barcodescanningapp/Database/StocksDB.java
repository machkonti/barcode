package com.example.machkonti.barcodescanningapp.Database;

/**
 * Created by machkonti on 30.3.2017 г..
 */

class StocksDB {
    private int id;
    private String name;
    private String bCode;
    private String expire;
    private int timeToNotice;

    public StocksDB() {
    }

    public StocksDB(int id, String name, String bCode, String expire, int timeToNotice) {
        this.id = id;
        this.name = name;
        this.bCode = bCode;
        this.expire = expire;
        this.timeToNotice = timeToNotice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public int getTimeToNotice() {
        return timeToNotice;
    }

    public void setTimeToNotice(int timeToNotice) {
        this.timeToNotice = timeToNotice;
    }

    @Override
    public String toString() {
        return "StocksDB{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bCode='" + bCode + '\'' +
                ", expire=" + expire +
                ", timeToNotice=" + timeToNotice +
                '}';
    }
}
