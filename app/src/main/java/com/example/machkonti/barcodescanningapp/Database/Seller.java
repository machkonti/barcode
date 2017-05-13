package com.example.machkonti.barcodescanningapp.Database;

/**
 * Created by machkonti on 10.5.2017 г..
 */

public class Seller {
    private int id;
    private String name;

    public Seller() {
    }

    public Seller(String name) {
        this.name = name;
    }

    public Seller(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
