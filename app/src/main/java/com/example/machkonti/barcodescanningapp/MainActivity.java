package com.example.machkonti.barcodescanningapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.example.machkonti.barcodescanningapp.Database.StocksWithExps;
import com.example.machkonti.barcodescanningapp.integration.android.IntentIntegrator;
import com.example.machkonti.barcodescanningapp.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {
    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;
    private static final int STOCK_DETAILS_ACTIVITY_REQUEST_CODE = 2;
    List<Stocks> stocksArrayList;
    ArrayAdapter<String> adapter;
    private TextView formatTxt, contentTxt;
    private String bCode;
    private SQLHelper sql;
    private Stocks stock;
    private ListView stockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanBtn = (Button) findViewById(R.id.scan_button);
        stockList = (ListView) findViewById(R.id.stocks);

        scanBtn.setOnClickListener(this);

        displayStocksList();

    }

    private void displayStocksList() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, makeList());
        stockList.setAdapter(adapter);
        final Context context = this;
        stockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stocks s = stocksArrayList.get(position);
                startDetailsActivity(s.getbCode());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    private Stocks getStock(String bCode) {
        SQLHelper db = new SQLHelper(this);
        return db.getStock(bCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
//            String scanFormat = scanningResult.getFormatName();

            this.bCode = scanningResult.getContents();

            Log.e("TAG :: ", scanningResult.toString());

            Stocks stock = getStock(this.bCode);

            if (stock == null) {
                addBcodeDialot();
            } else {
                startDetailsActivity(this.bCode);

            }
//            formatTxt.setText("FORMAT: " + scanFormat);
//            contentTxt.setText("CONTENT: " + scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

        displayStocksList();

    }

    public void addBcodeDialot() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("Not in database! Add id ?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAddActivity();

                dialog.dismiss();
            }
        });
        ad.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    public void startAddActivity() {
        Intent i = new Intent(this, AddStock.class);
        i.putExtra("bcode", bCode);
        this.startActivityForResult(i, ADD_ACTIVITY_REQUEST_CODE);
    }

    public void startDetailsActivity(String bCode) {
        Intent detailedIntent = new Intent(this, StockDetails.class);
        Bundle b = new Bundle();
        b.putString("bcode", bCode);
        detailedIntent.putExtras(b);

        startActivity(detailedIntent);
    }


    private List<String> makeList() {
        SQLHelper sqh = new SQLHelper(this);
        List<Stocks> stocks = sqh.getAllStocks();
        List<StocksWithExps> list = new ArrayList<>();
        List<String> rValue = new ArrayList<>();
        for (int i = 0; i < stocks.size(); i++) {
            Stocks s = stocks.get(i);
            List<Expires> te = sqh.getExpiresByBCode(s.getbCode());

            StocksWithExps t = new StocksWithExps(s.getbCode(), s.getName(), te);
            list.add(t);
            String sb = s.getName() + " :: " + s.getbCode();
            rValue.add(sb);
        }
        this.stocksArrayList = stocks;
        return rValue;
    }
}
