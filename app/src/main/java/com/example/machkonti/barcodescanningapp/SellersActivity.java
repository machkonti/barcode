package com.example.machkonti.barcodescanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.machkonti.barcodescanningapp.Adapters.SellersAdapter;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Seller;

import java.util.ArrayList;

public class SellersActivity extends AppCompatActivity {

    private static final int ADD_SELLER_REQUEST_CODE = 1;

    private ViewHolder holder = new ViewHolder();
    private ArrayList<Seller> sellerArrayList;

    public void onItemClick(int position) {
        Seller s = sellerArrayList.get(position);
        startSellerDetailsActivity(s.getId());
    }

    private void startSellerDetailsActivity(int id) {
        Intent detailedIntent = new Intent(this, SellerDetails.class);
        Bundle b = new Bundle();
        b.putInt("sellerid", id);
        detailedIntent.putExtras(b);

        startActivity(detailedIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers);
        holder.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(holder.toolbar);

        holder.fab = (FloatingActionButton) findViewById(R.id.fab);
        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellersActivity.this, AddSeller.class);

                startActivityForResult(i, ADD_SELLER_REQUEST_CODE);
            }
        });


        displaySellersList();
    }

    private void displaySellersList() {
        if (this.sellerArrayList != null) {
            this.sellerArrayList.clear();
        }
        this.sellerArrayList = makeSellerArrayList();
        holder.sellersListView = (ListView) findViewById(R.id.sellers_list_view);
        SellersAdapter adapter = new SellersAdapter(SellersActivity.this, this.sellerArrayList, getResources());
        holder.sellersListView.setAdapter(adapter);

    }

    private ArrayList<Seller> makeSellerArrayList() {
        SQLHelper db = new SQLHelper(this);
        return db.getAllSellers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SELLER_REQUEST_CODE) {
            displaySellersList();
        }
    }

    class ViewHolder {
        Toolbar toolbar;
        FloatingActionButton fab;
        ListView sellersListView;
    }
}
