package com.example.machkonti.barcodescanningapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import java.util.Calendar;

public class AddExpire extends AppCompatActivity {
    NativeExpressAdView mAdView;
    VideoController mVideoController;
    private int year, month, day;
    private ViewHolder holder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expire);
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String bCode = getIntent().getStringExtra("bCode");
        String name = getIntent().getStringExtra("name");

        holder.bCodeView = (TextView) findViewById(R.id.bCode);
        holder.bNameView = (TextView) findViewById(R.id.name);
        holder.exp = (EditText) findViewById(R.id.expire);

        holder.exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog mDatePicker = new DatePickerDialog(AddExpire.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        holder.exp.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth));
                    }
                }, year, month, day);
                mDatePicker.setTitle("Expire Date");
                mDatePicker.show();
            }
        });

        holder.av = (EditText) findViewById(R.id.avans);

        holder.bCodeView.setText(bCode);
        holder.bNameView.setText(name);

        holder.addBtn = (Button) findViewById(R.id.AddButton);
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("exp", holder.exp.getText().toString());
                i.putExtra("av", holder.av.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
        holder.cancelBtn = (Button) findViewById(R.id.cancel_button_expire);
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        mAdView = (NativeExpressAdView) findViewById(R.id.adView);
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        mVideoController = mAdView.getVideoController();
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
        TextView bCodeView;
        TextView bNameView;
        EditText exp;
        EditText av;
        Button addBtn, cancelBtn;
    }
}
