package com.example.machkonti.barcodescanningapp;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.example.machkonti.barcodescanningapp.Database.StocksWithExps;
import com.example.machkonti.barcodescanningapp.R;

import java.text.ParseException;
import java.util.List;

public class StockDetails extends Activity {
    private static final String TAG = StockDetails.class.getSimpleName();

    private TextView bCodeView;
    private TextView nameView;
    private ListView expiresView;

    private String bCode;
    private StocksWithExps stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        bCodeView = (TextView) findViewById(R.id.bCode);
        nameView = (TextView) findViewById(R.id.name);
        expiresView = (ListView) findViewById(R.id.expires);

        Bundle b = getIntent().getExtras();
        this.bCode = b.get("bcode").toString();

        this.stock = getStock(bCode);

        bCodeView.setText(stock.getbCode());
        nameView.setText(stock.getName());

        ArrayAdapter<Expires> adapter = new ArrayAdapter<Expires>(this, android.R.layout.simple_list_item_1, stock.getExp());
        expiresView.setAdapter(adapter);
    }

    private StocksWithExps getStock(String bCode) {
        SQLHelper db = new SQLHelper(this);
        Stocks s = db.getStock(bCode);
        List<Expires> e = null;
        try {
            e = db.getExpiresByBCode(bCode);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
       return new StocksWithExps(s,e);
    }

}
