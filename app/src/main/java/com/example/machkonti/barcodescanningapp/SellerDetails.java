package com.example.machkonti.barcodescanningapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Adapters.MainListAdapter;
import com.example.machkonti.barcodescanningapp.Database.Combine;
import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Seller;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.example.machkonti.barcodescanningapp.integration.android.IntentIntegrator;
import com.example.machkonti.barcodescanningapp.integration.android.IntentResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by Machkonti on 10.5.2017 г..
 */

public class SellerDetails extends AppCompatActivity {
    private static final String TAG = SellerDetails.class.getSimpleName();
    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;

    private final ViewHolder holder = new ViewHolder();
    private ArrayList<Stocks> stocksArrayList;

    private String bCode;
    private Seller seller;
    private int sellerId;
    private int p = 0;

    private Resources res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();

        holder.stocksListView = (ListView) findViewById(R.id.stocks);

        registerForContextMenu(holder.stocksListView);

        Bundle b = getIntent().getExtras();
        sellerId = b.getInt("sellerid");

        this.seller = getSeller(sellerId);

        initToolbar(seller.getName());

        displayStocksList();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initToolbar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);

        setSupportActionBar(toolbar);

        toolbar.setCollapsible(true);
        toolbar.inflateMenu(R.menu.stocks_activity_menu);
        toolbar.collapseActionView();

        toolbar.showOverflowMenu();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.new_scan) {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(SellerDetails.this);
                    scanIntegrator.initiateScan();
                }
                if (item.getItemId() == R.id.menu_item_delete) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(SellerDetails.this);
                    ad.setMessage("Delete Seller - " + seller.getName() + " ?");
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // startAddActivity();

                            deleteSeller(seller);
                            dialog.dismiss();
                            finishAndRemoveTask();
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
                return false;
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void deleteSeller(Seller s) {
        SQLHelper db = new SQLHelper(this);
        ArrayList<Stocks> stockses = db.getAllStocksBySeller(s.getId());
        for (int i = 0; i < stockses.size(); i++) {
            ArrayList<Expires> expires = db.getExpiresByBCode(stockses.get(i).getbCode());
            for (int o = 0; o < expires.size(); o++) {
                db.deleteExpire(expires.get(o));
            }
            db.deleteStock(stockses.get(i));
        }
        ArrayList<Combine> combines = db.getAllCombinesBySellerId(s.getId());
        for (int i = 0; i < combines.size(); i++) {
            db.deleteCombine(combines.get(i));
        }

        db.deleteSeller(s);
        db.close();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            if (scanningResult.getContents() != null) {
                this.bCode = scanningResult.getContents();

                Stocks stock = getStock(this.bCode);

                if (stock == null) {
                    addBcodeDialot();
                } else {
                    startDetailsActivity(this.bCode);

                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

        displayStocksList();

    }

    private void displayStocksList() {
//        makeList();
        if (stocksArrayList != null) {
            stocksArrayList = null;
        }
        stocksArrayList = makeCombineList();

        MainListAdapter adapter = new MainListAdapter(SellerDetails.this, stocksArrayList, res);

        holder.stocksListView.setAdapter(adapter);
    }

    private ArrayList<Stocks> makeCombineList() {
        SQLHelper db = new SQLHelper(this);
        return db.getAllStocksBySeller(sellerId);
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
        i.putExtra("sellerId", sellerId);
        i.putExtra("bcode", bCode);
        this.startActivityForResult(i, ADD_ACTIVITY_REQUEST_CODE);
    }

    private Seller getSeller(int sellerId) {
        SQLHelper db = new SQLHelper(this);
        return db.getSeller(sellerId);
    }

    private Stocks getStock(String bCode) {
        SQLHelper db = new SQLHelper(this);
        return db.getStock(bCode);
    }

    private void startDetailsActivity(String bCode) {
        Intent detailedIntent = new Intent(this, StockDetails.class);
        Bundle b = new Bundle();
        b.putString("bcode", bCode);
        detailedIntent.putExtras(b);

        startActivityForResult(detailedIntent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stocks_activity_menu, menu);
        MenuItem scan_button = menu.findItem(R.id.new_scan);

        return super.onCreateOptionsMenu(menu);
    }

    public void onItemClick(int position) {
        Stocks s = stocksArrayList.get(position);
        startDetailsActivity(s.getbCode());
    }

    class ViewHolder {
        ListView stocksListView;
    }
}
