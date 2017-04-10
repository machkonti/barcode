package com.example.machkonti.barcodescanningapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;

import java.util.List;

public class StockDetails extends Activity {
    private static final String TAG = StockDetails.class.getSimpleName();

    private TextView bCodeView;
    private TextView nameView;
    private ListView expiresView;

    private String bCode;
    private Stocks stock;
    private List<Expires> epxs;

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

        ArrayAdapter<Expires> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getEpxs(bCode));
        expiresView.setAdapter(adapter);

        getActionBar().setTitle(stock.getName());
        getActionBar().show();
    }

    private List<Expires> getEpxs(String bCode) {
        SQLHelper db = new SQLHelper(this);
        return db.getExpiresByBCode(bCode);
    }

    private Stocks getStock(String bCode) {
        SQLHelper db = new SQLHelper(this);

        return db.getStock(bCode);
    }

}
