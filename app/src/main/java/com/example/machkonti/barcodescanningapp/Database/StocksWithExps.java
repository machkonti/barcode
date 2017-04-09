package com.example.machkonti.barcodescanningapp.Database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Machkonti on 7.4.2017 г..
 */

public class StocksWithExps {
    private String bCode;
    private String name;
    private List<Expires> exp;
    private Context context;

    public StocksWithExps() {

    }

    public StocksWithExps(Context context, String bCode) {
        this.context = context;
        this.bCode = bCode;
        setExpiresFromDb();
    }

    public StocksWithExps(Stocks s, List<Expires> e) {
        this.bCode = s.getbCode();
        this.name = s.getName();
        this.exp = e;
    }

    public StocksWithExps(String bCode, String name, List<Expires> exp) {
        this.bCode = bCode;
        this.name = name;
        this.exp = exp;
    }

    public void setExpiresFromDb() {
        SQLHelper db = new SQLHelper(context);
        Stocks s = db.getStock(this.bCode);
        this.name = s.getName();
        this.exp = db.getExpiresByBCode(this.bCode);
    }

    public String getbCode() {
        return this.bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expires> getExp() {
        return sortExps(this.exp);
    }

    public void setExp(List<Expires> exp) {
        this.exp = exp;
    }

    public List<Expires> sortExps(List<Expires> e) {
        if (e == null) return new ArrayList<>();
        if (e.size() < 1 || e.isEmpty()) return new ArrayList<>();

        List<Expires> tmp = e;

        for(int i = 0; i < tmp.size(); i++) {
            Expires t1 = tmp.get(i);
            for(int u = 0; u<tmp.size();u++) {
                long d1 = t1.getExpDate().getTime() - (t1.getDaysToNotice()*24*60*60);
                long d2 = tmp.get(u).getExpDate().getTime() - (tmp.get(u).getDaysToNotice()*24*60*60);
                if(d2 < d1) {
                    Expires tu = tmp.get(u);
                    tmp.set(u,t1);
                    tmp.set(i,tu);
                }
            }
        }
        return tmp;
    }

    @Override
    public String toString() {
        return "StocksWithExps{" +
                "bCode='" + bCode + '\'' +
                ", name='" + name + '\'' +
                ", exp=" + exp +
                '}';
    }
}
