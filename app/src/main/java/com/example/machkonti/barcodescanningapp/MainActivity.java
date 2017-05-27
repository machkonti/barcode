package com.example.machkonti.barcodescanningapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Adapters.MainListAdapter;
import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.example.machkonti.barcodescanningapp.integration.android.IntentIntegrator;
import com.example.machkonti.barcodescanningapp.integration.android.IntentResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;
    private static final int STOCK_DETAILS_ACTIVITY_REQUEST_CODE = 2;
    private MainActivity mainActivity = null;
    private ArrayList<Stocks> stocksArrayList;
    private String bCode;
    private ListView stockList;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();

        mainActivity = this;
        res = getResources();

        stockList = (ListView) findViewById(R.id.stocks);

        displayStocksList();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void displayStocksList() {
        makeList();
        MainListAdapter adapter = new MainListAdapter(mainActivity, stocksArrayList, res);
        stockList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
    }

    private Stocks getStock(String bCode) {
        SQLHelper db = new SQLHelper(this);
        return db.getStock(bCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            this.bCode = scanningResult.getContents();

            Log.e("TAG :: ", scanningResult.toString());

            Stocks stock = getStock(this.bCode);

            if (stock == null) {
                addBcodeDialot();
            } else {
                startDetailsActivity(this.bCode);

            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

        displayStocksList();

    }

    private void addBcodeDialot() {
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

    private void startAddActivity() {
        Intent i = new Intent(this, AddStock.class);
        i.putExtra("bcode", bCode);
        this.startActivityForResult(i, ADD_ACTIVITY_REQUEST_CODE);
    }

    private void startDetailsActivity(String bCode) {
        Intent detailedIntent = new Intent(this, StockDetails.class);
        Bundle b = new Bundle();
        b.putString("bcode", bCode);
        detailedIntent.putExtras(b);

        startActivity(detailedIntent);
    }


    @SuppressWarnings("UnusedReturnValue")
    private List<String> makeList() {
        SQLHelper sqh = new SQLHelper(this);
        ArrayList<Stocks> stocks = sqh.getAllStocks();
        List<String> rValue = new ArrayList<>();
        for (int i = 0; i < stocks.size(); i++) {
            Stocks s = stocks.get(i);
            List<Expires> te = sqh.getExpiresByBCode(s.getbCode());

            String sb = s.getName() + " :: " + s.getbCode();
            rValue.add(sb);
        }
        this.stocksArrayList = stocks;
        return rValue;
    }

    public void onItemClick(int position) {
        Stocks s = stocksArrayList.get(position);
        startDetailsActivity(s.getbCode());
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setCollapsible(true);

        toolbar.inflateMenu(R.menu.main_activity_menu);
        toolbar.collapseActionView();
        toolbar.showOverflowMenu();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.new_scan) {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                    scanIntegrator.initiateScan();
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}
