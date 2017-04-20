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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

public class AddExpire extends AppCompatActivity {
    private int year, month, day;

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

        TextView bCodeView = (TextView) findViewById(R.id.bCode);
        TextView bNameView = (TextView) findViewById(R.id.name);
        final EditText exp = (EditText) findViewById(R.id.expire);

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDialog(10);
                final DatePickerDialog mDatePicker = new DatePickerDialog(AddExpire.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        exp.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth));
//                        int month_k = month+1;
                    }
                }, year, month, day);
                mDatePicker.setTitle("Expire Date");
//                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });

        final EditText av = (EditText) findViewById(R.id.avans);

        bCodeView.setText(bCode);
        bNameView.setText(name);

        Button addBtn = (Button) findViewById(R.id.AddButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("exp", exp.getText().toString());
                i.putExtra("av", av.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
