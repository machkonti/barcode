package com.example.machkonti.barcodescanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StockDetails extends AppCompatActivity {
    private static final String TAG = StockDetails.class.getSimpleName();

    private ListView expiresView;
    private List<Expires> epxs;
    private Toolbar toolbar;
    private String bCode, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        TextView bCodeView = (TextView) findViewById(R.id.bCode);
        TextView nameView = (TextView) findViewById(R.id.name);
        expiresView = (ListView) findViewById(R.id.expires);

        Bundle b = getIntent().getExtras();
        bCode = b.get("bcode").toString();

        Stocks stock = getStock(bCode);

        name = stock.getName();

        bCodeView.setText(stock.getbCode());
        nameView.setText(stock.getName());

        ArrayAdapter<Expires> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getEpxs(bCode));
        expiresView.setAdapter(adapter);

        initToolBar();
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
                Toast.makeText(StockDetails.this, item.getItemId() + " clicking the toolbar!", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(StockDetails.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private List<Expires> getEpxs(String bCode) {
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

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                Date expDate = new Date();
                try {
                    expDate = sd.parse(exp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String ep = sd.format(expDate);
                Expires e = new Expires(bCode, expDate, Integer.parseInt(av));

                SQLHelper db = new SQLHelper(StockDetails.this);
                db.insertExpire(e);
                ((ArrayAdapter) expiresView.getAdapter()).notifyDataSetChanged();
            }
        }
    }
}
