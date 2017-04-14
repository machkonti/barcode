package com.example.machkonti.barcodescanningapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.Database.SQLHelper;
import com.example.machkonti.barcodescanningapp.Database.Stocks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddStock extends Activity {
    TextView bCode;
    EditText name;
    EditText expire;
    EditText daysAdvance;
    Button addButton;

    private int year, month, day;
    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            expire.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth));
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

        bCode = (TextView) findViewById(R.id.bCode);
        name = (EditText) findViewById(R.id.name);
        expire = (EditText) findViewById(R.id.expire);
        daysAdvance = (EditText) findViewById(R.id.avans);
        addButton = (Button) findViewById(R.id.AddButton);

        bCode.setText(this.getIntent().getStringExtra("bcode"));

        expire.setShowSoftInputOnFocus(false);
        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(10);
                Toast.makeText(getApplicationContext(), "bg", Toast.LENGTH_SHORT).show();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bcode,namev,exp,avans;
                boolean success = false;
                bcode = bCode.getText().toString();
                namev = name.getText().toString();
                exp = expire.getText().toString();
                avans = daysAdvance.getText().toString();

                Stocks st = new Stocks(bcode, namev);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date expDate = new Date();

                try {
                    expDate = sd.parse(exp);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Expires ex = new Expires(bcode,expDate,Integer.parseInt(avans));

                SQLHelper sqh = new SQLHelper(getApplicationContext());
                if(sqh.insertStock(st)>0) success = true;
                if(sqh.insertExpire(ex)>0) success = true;

                if(!success) {
                    Toast.makeText(AddStock.this, "Something met wrong !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddStock.this, "Inserted !", Toast.LENGTH_SHORT).show();
                    AddStock.this.finishAndRemoveTask();
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 10) {
            return new DatePickerDialog(this,mDatePickerListener,year, month, day);
        }

        return null;
    }


}
