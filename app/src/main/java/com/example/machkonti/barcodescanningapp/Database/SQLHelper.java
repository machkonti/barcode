package com.example.machkonti.barcodescanningapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private static final String[] s_columns = {s_bCode, s_name};
    private static final String[] e_columns = {e_bCode, e_expire, e_daysToNotice};
    private static final String CREATE_TABLE_STOCKS = "CREATE TABLE " + table_stocks +
            " ( " + s_bCode + " TEXT PRIMARY KEY," +
            s_name + " TEXT )";
    private static final String CRETE_TABLE_EXPIRE = "CREATE TABLE " + table_exprire +
            " ( " + e_bCode + " TEXT , " + e_expire + " TEXT, " + e_daysToNotice + " INTEGER )";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_stocks);
        db.execSQL("DROP TABLE IF EXISTS " + table_exprire);
        this.onCreate(db);
    }

    public void createStock(Stocks s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(s_bCode, s.getbCode());
        values.put(s_name, s.getName());

        db.insert(table_stocks, null, values);
        db.close();
    }

    public void createExpire(Expires e) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(e_bCode, e.getbCode());
        values.put(e_expire, e.getExpDate());
        values.put(e_daysToNotice, e.getDaysToNotice());

        db.insert(table_stocks, null, values);
        db.close();
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
        return stocks;
    }

    public List<Expires> getExpiresByBCode(String bCode) {
        List<Expires> expires = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_exprire, e_columns, e_bCode + "=?", new String[]{bCode}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                Date d = null;
//                try {
//                    d = sf.parse(cursor.getString(1));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                Expires e = new Expires(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                expires.add(e);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expires;
    }

    public int getStocksCount() {
        String countQuery = "SELECT  * FROM " + table_stocks;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
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

    public int updateStocks(Stocks s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(s_name, s.getName());
        values.put(s_bCode, s.getbCode());

        // updating row
        return db.update(table_stocks, values, s_bCode + " = ?",
                new String[]{String.valueOf(s.getbCode())});
    }

    public void deleteStock(Stocks s) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_stocks, s_bCode + " = ?",
                new String[]{s.getbCode()});
        db.close();
    }

    public void deleteExpire(Expires e) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_exprire, s_bCode + "=? AND " + e_expire + "=?", new String[]{e.getbCode(), e.getExpDate()});
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
}

