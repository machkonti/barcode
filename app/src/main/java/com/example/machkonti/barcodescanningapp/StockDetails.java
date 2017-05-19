package com.example.machkonti.barcodescanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.machkonti.barcodescanningapp.Adapters.ExpiresListAdapter;
import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StockDetails extends AppCompatActivity {
    private static final String TAG = StockDetails.class.getSimpleName();
    private static final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

    private ListView expiresView;
    private ArrayList<Expires> epxs;
    private Toolbar toolbar;
    private String bCode, name;

    private ExpiresListAdapter adapter;
    private int p = 0;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        TextView bCodeView = (TextView) findViewById(R.id.bCode);
        TextView nameView = (TextView) findViewById(R.id.name);
        expiresView = (ListView) findViewById(R.id.expires);

        registerForContextMenu(expiresView);

        Bundle b = getIntent().getExtras();
        bCode = b.get("bcode").toString();

        Stocks stock = getStock(bCode);

        name = stock.getName();

        bCodeView.setText(stock.getbCode());
        nameView.setText(stock.getName());

        displayExpiresList();

        initToolBar();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void displayExpiresList() {
        if (this.epxs != null) {
            epxs.clear();
        }
        epxs = getEpxs(bCode);

        adapter = new ExpiresListAdapter(this, getEpxs(bCode), getResources());
        expiresView.setAdapter(adapter);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Stock Details");

        setSupportActionBar(toolbar);

        toolbar.setCollapsible(true);
        toolbar.inflateMenu(R.menu.stock_details_menu);
        toolbar.collapseActionView();

        toolbar.showOverflowMenu();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_new_expire) {
                    Intent i = new Intent(StockDetails.this, AddExpire.class);
                    i.putExtra("bCode", bCode);
                    i.putExtra("name", name);
                    startActivityForResult(i, 1);
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

    private ArrayList<Expires> getEpxs(String bCode) {
        SQLHelper db = new SQLHelper(this);

        return db.getExpiresByBCode(bCode);
    }

    private Stocks getStock(String bCode) {
        SQLHelper db = new SQLHelper(this);

        return db.getStock(bCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_details_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String exp = data.getStringExtra("exp");
                String av = data.getStringExtra("av");


                Date expDate = new Date();
                try {
                    expDate = sd.parse(exp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String ep = sd.format(expDate);
                Expires e = new Expires(bCode, exp, Integer.parseInt(av));

                SQLHelper db = new SQLHelper(StockDetails.this);
                db.insertExpire(e);
                displayExpiresList();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayExpiresList();
    }

    public void onItemClick(int position) {
        this.p = position;
        openContextMenu(expiresView);
        expiresView.showContextMenuForChild(expiresView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_exp_menu, menu);

        try {
            String[] s = sd.parse(epxs.get(this.p).getExpDate()).toGMTString().split(" ");
            menu.setHeaderTitle(s[0] + " " + s[1] + " " + s[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            Expires e = epxs.get(this.p);
            SQLHelper db = new SQLHelper(StockDetails.this);
            db.deleteExpire(e);
            epxs.remove(e);
            adapter.notifyDataSetChanged();
            displayExpiresList();
        }
        return super.onContextItemSelected(item);
    }
}
