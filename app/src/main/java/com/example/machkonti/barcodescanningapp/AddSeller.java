package com.example.machkonti.barcodescanningapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.machkonti.barcodescanningapp.Database.Seller;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Machkonti on 7.5.2017 г..
 */

public class AddSeller extends AppCompatActivity {
    private ViewHolder holder = new ViewHolder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seller);

        holder.sellerName = (EditText) findViewById(R.id.seller_name_edittext);
        holder.addSellerButton = (Button) findViewById(R.id.add_seller_button);

        holder.addSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seller seller = new Seller();
                seller.setName(holder.sellerName.getText().toString());
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private class ViewHolder {
        EditText sellerName;
        Button addSellerButton;
    }
}
