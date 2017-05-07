package com.example.machkonti.barcodescanningapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Adapters.SellersAdapter;
import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Seller;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.example.machkonti.barcodescanningapp.Database.StocksWithExps;
import com.example.machkonti.barcodescanningapp.integration.android.IntentIntegrator;
import com.example.machkonti.barcodescanningapp.integration.android.IntentResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener,
        ShareActionProvider.OnShareTargetSelectedListener, ActionMenuView.OnMenuItemClickListener {
    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;
    private static final int STOCK_DETAILS_ACTIVITY_REQUEST_CODE = 2;

    private MainActivity mainActivity = null;
    private ArrayList<Seller> sellersArrayList;
    private ArrayList<Stocks> stocksArrayList;
    private String bCode;

    private ListView sellersListView;
    private ListView stockList;
    private Resources res;

    private ShareActionProvider share = null;
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellers_list_view);

        initToolBar();

        mainActivity = this;
        res = getResources();

        sellersListView = (ListView) findViewById(R.id.sellers);

        displayStocksList();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void displayStocksList() {
        makeList();
        makeSellerList();
        SellersAdapter adapter = new SellersAdapter(mainActivity, sellersArrayList, res);
        sellersListView.setAdapter(adapter);
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

    private List<Seller> makeSellerList() {
        SQLHelper sqh = new SQLHelper(this);
        ArrayList<Seller> sellers = sqh.getAllSellers();

        this.sellersArrayList = sellers;
        return sellers;
    }

    private List<Stocks> makeList() {
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
        return stocks;
    }

    private List<StocksWithExps> exportList() {
        SQLHelper sqh = new SQLHelper(this);
        ArrayList tmp = sqh.exportList();
        return tmp;
    }

    public void onItemClick(int position) {
        Stocks s = stocksArrayList.get(position);
        startDetailsActivity(s.getbCode());
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sellers_toolbar);
        toolbar.setTitle("Sellers");
        setSupportActionBar(toolbar);

        toolbar.setCollapsible(true);
        toolbar.inflateMenu(R.menu.sellers_activity_menu);
        toolbar.collapseActionView();
        toolbar.showOverflowMenu();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.add_new_seller) {
                    Intent i = new Intent(MainActivity.this, AddSeller.class);
                    startActivityForResult(i, 1);
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sellers_activity_menu, menu);

        MenuItem add_seller_button = menu.findItem(R.id.add_new_seller);
//        share = (ShareActionProvider) MenuItemCompat.getActionProvider(shareitem);
//        share.setOnShareTargetSelectedListener(this);
//
//        shareitem.setEnabled(true);
//
//        shareitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                Gson gson = new Gson();
//
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, gson.toJson(exportList()));
//                sendIntent.setType("text/plain");
//                startActivity(Intent.createChooser(sendIntent, "Share items"));
//                return false;
//            }
//        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        Toast.makeText(this, intent.getComponent().toString(),
                Toast.LENGTH_LONG).show();

        return (false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(MainActivity.this, item.getTitle() + "menu item is clicked !!!", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("Main", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
}
