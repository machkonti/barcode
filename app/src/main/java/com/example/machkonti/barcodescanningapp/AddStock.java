package com.example.machkonti.barcodescanningapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Database.Combine;
import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import java.util.Calendar;

public class AddStock extends Activity {
    private final ViewHolder holder = new ViewHolder();
    private VideoController mVideoController;
    private int year, month, day, sellerId;
    private String bCodeString;
    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            holder.expire.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        holder.bCode = (TextView) findViewById(R.id.bCode);
        holder.name = (EditText) findViewById(R.id.name);
        holder.expire = (EditText) findViewById(R.id.expire);
        holder.daysAdvance = (EditText) findViewById(R.id.avans);
        Button addButton = (Button) findViewById(R.id.AddButton);

        bCodeString = this.getIntent().getStringExtra("bcode");
        sellerId = this.getIntent().getIntExtra("sellerId", 1);

        holder.bCode.setText(bCodeString);

        holder.expire.setShowSoftInputOnFocus(false);
        holder.expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog mDatePicker = new DatePickerDialog(AddStock.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        holder.expire.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth));

                    }
                }, year, month, day);
                mDatePicker.setTitle("Expire Date");
                mDatePicker.show();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bcode, namev, exp, avans;
                boolean success = false;
                bcode = holder.bCode.getText().toString();
                namev = holder.name.getText().toString();
                exp = holder.expire.getText().toString();
                avans = holder.daysAdvance.getText().toString();

                Stocks st = new Stocks(bcode, namev);
                Expires ex = new Expires(bcode, exp, Integer.parseInt(avans));
                Combine combine = new Combine(sellerId, bCodeString);

                SQLHelper sqh = new SQLHelper(getApplicationContext());
                success = sqh.insertStock(st) > 0;
                success = sqh.insertExpire(ex) > 0;
                success = sqh.insertCombine(combine) > 0;
                if (!success) {
                    Toast.makeText(AddStock.this, "Something met wrong !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddStock.this, "Inserted !", Toast.LENGTH_SHORT).show();
                    AddStock.this.finishAndRemoveTask();
                }
            }
        });

        NativeExpressAdView mAdView = (NativeExpressAdView) findViewById(R.id.adView);
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        mVideoController = mAdView.getVideoController();
        //noinspection EmptyMethod
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                } else {
                }
            }
        });

        mAdView.loadAd(new AdRequest.Builder().build());
    }

    class ViewHolder {
        TextView bCode;
        EditText name;
        EditText expire;
        EditText daysAdvance;
    }
}
