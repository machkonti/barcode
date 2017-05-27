package com.example.machkonti.barcodescanningapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Seller;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

/**
 * Created by Machkonti on 7.5.2017 г..
 */

public class AddSeller extends AppCompatActivity {
    private static final String LOG_TAG = "EXAMPLE";
    private final ViewHolder holder = new ViewHolder();
    private VideoController mVideoController;

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

                SQLHelper sqh = new SQLHelper(getApplicationContext());
                boolean success = false;
                if (sqh.insertSeller(seller) > 0) success = true;

                if (!success) {
                    Toast.makeText(AddSeller.this, "Something met wrong !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddSeller.this, "Inserted !", Toast.LENGTH_SHORT).show();
                    AddSeller.this.finishAndRemoveTask();
                }

            }
        });

        NativeExpressAdView mAdView = (NativeExpressAdView) findViewById(R.id.adView);
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        mVideoController = mAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                Log.d(LOG_TAG, "Video playback is finished.");
                super.onVideoEnd();
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d(LOG_TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(LOG_TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        mAdView.loadAd(new AdRequest.Builder().build());

    }

    class ViewHolder {
        EditText sellerName;
        Button addSellerButton;
    }
}
