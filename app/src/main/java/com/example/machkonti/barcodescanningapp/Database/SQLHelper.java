package com.example.machkonti.barcodescanningapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by machkonti on 30.3.2017 г..
 */

public class SQLHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    private static final String database_name = "Stocks";

    private static final String table_stocks = "Stocks";
    private static final String s_bCode = "bCode";
    private static final String s_name = "name";

    private static final String table_exprire = "Expire";
    private static final String e_bCode = "bCode";
    private static final String e_expire = "expire";
    private static final String e_daysToNotice = "daysToNotice";

    private static final String table_sellers = "Sellers";
    private static final String ss_id = "ID";
    private static final String ss_name = "name";

    private static final String table_combine = "Combine";
    private static final String c_seller_id = "sellerId";
    private static final String c_bCode = "bCode";

    private static final String[] s_columns = {s_bCode, s_name};
    private static final String[] e_columns = {e_bCode, e_expire, e_daysToNotice};
    private static final String[] ss_columns = {ss_id, ss_name};
    private static final String[] c_columns = {c_seller_id, c_bCode};

    private static final String CREATE_TABLE_STOCKS = "CREATE TABLE " + table_stocks +
            " ( " + s_bCode + " TEXT PRIMARY KEY," +
            s_name + " TEXT )";
    private static final String CRETE_TABLE_EXPIRE = "CREATE TABLE " + table_exprire +
            " ( " + e_bCode + " TEXT , " + e_expire + " TEXT, " + e_daysToNotice + " INTEGER )";

    private static final String CREATE_TABLE_SELLER = "CREATE TABLE " + table_sellers +
            " ( " + ss_id + " INTEGER PRIMARY KEY, " + ss_name + " TEXT )";
    private static final String CREATE_TABLE_COMBINE = "CREATE TABLE " + table_combine +
            " ( " + c_seller_id + " INTEGER, " + c_bCode + " TEXT )";

    public SQLHelper(Context context) {
        super(context, database_name, null, DATABASE_VERSION);
    }

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STOCKS);
        db.execSQL(CRETE_TABLE_EXPIRE);
        db.execSQL(CREATE_TABLE_SELLER);
        db.execSQL(CREATE_TABLE_COMBINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_stocks);
        db.execSQL("DROP TABLE IF EXISTS " + table_exprire);
        db.execSQL("DROP TABLE IF EXISTS " + table_sellers);
        db.execSQL("DROP TABLE IF EXISTS " + table_combine);
        this.onCreate(db);
    }

    public Seller getSeller(int sellerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_sellers, ss_columns, ss_id + "=?", new String[]{String.valueOf(sellerId)}, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        Seller s = new Seller(cursor.getInt(0), cursor.getString(1));
        cursor.close();
        return s;

    }

    public Stocks getStock(String bCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_stocks, s_columns, s_bCode + "=?", new String[]{bCode}, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        Stocks st = new Stocks(cursor.getString(0), cursor.getString(1));
        cursor.close();
        return st;
    }

    public ArrayList<Seller> getAllSellers() {
        ArrayList<Seller> sellers = new ArrayList<>();
        String query = "SELECT * FROM " + table_sellers;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                sellers.add(new Seller(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return sellers;
    }

    public ArrayList<Combine> getAllCombines() {
        ArrayList<Combine> combines = new ArrayList<>();
        String query = "SELECT * FROM " + table_combine;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                combines.add(new Combine(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return combines;
    }

    public ArrayList<Stocks> getAllStocks() {
        ArrayList<Stocks> stocks = new ArrayList<>();
        String query = "SELECT * FROM " + table_stocks;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Stocks s = new Stocks(cursor.getString(0), cursor.getString(1));
                stocks.add(s);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(stocks, new Comparator<Stocks>() {
            @Override
            public int compare(Stocks o1, Stocks o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String[] d1 = getExpiresByBCode(o1.getbCode()).get(0).getExpDate().split("-");
                String[] d2 = getExpiresByBCode(o2.getbCode()).get(0).getExpDate().split("-");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = sdf.parse(d1[2] + "/" + d1[1] + "/" + d1[0]);
                    date2 = sdf.parse(d2[2] + "/" + d2[1] + "/" + d2[0]);

                    if (date1.getTime() > date2.getTime()) {
                        return 1;
                    } else if (date1.getTime() < date2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });

        return stocks;
    }

    public ArrayList<Stocks> getAllStocksBySeller(int sellerId) {
        ArrayList<Stocks> stockses = new ArrayList<>();
        String query = "SELECT * FROM " + table_combine + " c INNER JOIN " + table_stocks + " s " +
                "ON c." + c_bCode + "=" + "s." + s_bCode + " WHERE c." + c_seller_id + "=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sellerId)});
        if (cursor.moveToFirst()) {
            do {
                stockses.add(new Stocks(cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return stockses;
    }

    public ArrayList<Combine> getAllCombinesBySellerId(int id) {
        ArrayList<Combine> combines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_combine, c_columns, c_seller_id + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                combines.add(new Combine(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return combines;
    }

    public ArrayList<Expires> getExpiresByBCode(String bCode) {
        ArrayList<Expires> expires = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_exprire, e_columns, e_bCode + "=?", new String[]{bCode}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Expires e = new Expires(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                expires.add(e);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(expires, new Comparator<Expires>() {
            @Override
            public int compare(Expires o1, Expires o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String[] d1 = o1.getExpDate().split("-");
                String[] d2 = o2.getExpDate().split("-");

                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = sdf.parse(d1[2] + "/" + d1[1] + "/" + d1[0]);
                    date2 = sdf.parse(d2[2] + "/" + d2[1] + "/" + d2[0]);

                    if (date1.getTime() > date2.getTime()) {
                        return 1;
                    } else if (date1.getTime() < date2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
        return expires;
    }

    public int getSellersCount() {
        String query = "SELECT * FROM " + table_sellers;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getCombinesCount() {
        String query = "SELECT * FROM " + table_combine;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getCombinesCountBySellerId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_combine, c_columns, c_seller_id + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getStocksCount() {
        String countQuery = "SELECT  * FROM " + table_stocks;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getExpiresCount() {
        String query = "SELECT * FROM " + table_exprire;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getExpiresCountByBCode(String bCode) {
        String query = "SELECT * FROM " + table_exprire + " WHERE " + e_bCode + " = " + bCode;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateSeller(Seller s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(ss_id, s.getId());
        v.put(ss_name, s.getName());

        int r = db.update(table_sellers, v, ss_id + "=?", new String[]{String.valueOf(s.getId())});
        db.close();
        return r;
    }

    public int updateStocks(Stocks s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(s_name, s.getName());
        values.put(s_bCode, s.getbCode());

        // updating row
        int r = db.update(table_stocks, values, s_bCode + " = ?",
                new String[]{String.valueOf(s.getbCode())});
        db.close();
        return r;
    }

    public void deleteStock(Stocks s) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_stocks, s_bCode + "=?",
                new String[]{s.getbCode()});
        db.close();
    }

    public void deleteExpire(Expires e) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_exprire, s_bCode + "=? AND " + e_expire + "=?", new String[]{e.getbCode(), e.getExpDate()});
        db.close();
    }

    public void deleteSeller(Seller s) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_sellers, ss_id + "=?", new String[]{String.valueOf(s.getId())});
        db.close();
    }

    public void deleteCombine(Combine c) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_combine, c_seller_id + "=? AND " + c_bCode + "=?", new String[]{String.valueOf(c.getSellerId()), c.getbCode()});
        db.close();
    }

    public long insertStock(Stocks s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(s_bCode, s.getbCode());
        contentValues.put(s_name, s.getName());
        return db.insert(table_stocks, null, contentValues);
    }

    public long insertExpire(Expires e) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(e_bCode, e.getbCode());
        cv.put(e_expire, e.getExpDate());
        cv.put(e_daysToNotice, e.getDaysToNotice());
        return db.insert(table_exprire, null, cv);
    }

    public long insertSeller(Seller seller) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.putNull(ss_id);
        v.put(ss_name, seller.getName());
        long r = db.insert(table_sellers, null, v);
        db.close();
        return r;
    }

    public long insertCombine(Combine c) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(c_seller_id, c.getSellerId());
        values.put(c_bCode, c.getbCode());

        long r = db.insert(table_combine, null, values);
        db.close();
        return r;
    }



}

