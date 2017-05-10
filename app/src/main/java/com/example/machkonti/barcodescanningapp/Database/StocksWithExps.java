package com.example.machkonti.barcodescanningapp.Database;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public StocksWithExps(Context context, String bCode, String name, List<Expires> exp) {
        this.bCode = bCode;
        this.name = name;
        this.exp = exp;
    }

    private void setExpiresFromDb() {
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

    private List<Expires> sortExps(List<Expires> e) {
        if (e == null) return new ArrayList<>();
        if (e.size() < 1 || e.isEmpty()) return new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date d11 = new Date();
        Date d22 = new Date();
        for (int i = 0; i < e.size(); i++) {
            Expires t1 = e.get(i);
            for (int u = 0; u < e.size(); u++) {
                try {
                    d11 = sf.parse(t1.getExpDate());
                    d22 = sf.parse(e.get(u).getExpDate());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                long d1 = d11.getTime() - (t1.getDaysToNotice() * 24 * 60 * 60);
                long d2 = d22.getTime() - (e.get(u).getDaysToNotice() * 24 * 60 * 60);
                if (d2 < d1) {
                    Expires tu = e.get(u);
                    e.set(u, t1);
                    e.set(i, tu);
                }
            }
        }
        return e;
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
