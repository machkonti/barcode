package com.example.machkonti.barcodescanningapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.R;
import com.example.machkonti.barcodescanningapp.StockDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Machkonti on 16.4.2017 г..
 */

public class ExpiresListAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    private final Activity activity;
    private final ArrayList list;
    int i = 0;

    public ExpiresListAdapter(Activity activity, ArrayList list, Resources resources) {
        this.activity = activity;
        this.list = list;
        Resources resources1 = resources;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onClick(View v) {
        Log.v("ExpiresListAdapter", "expires list item clicked !");

    }

    @Override
    public int getCount() {
        if (list.size() <= 0) return 1;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        @SuppressWarnings("UnusedAssignment") ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            vi = inflater.inflate(R.layout.expire_list_item, null);

            holder = new ViewHolder();
            holder.daysAvans = (TextView) vi.findViewById(R.id.ela_daysAvans);
            holder.expireDate = (TextView) vi.findViewById(R.id.ela_expireDate);
            holder.daysLeft = (TextView) vi.findViewById(R.id.ela_leftDays);


            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (list.size() <= 0) {
            holder.daysAvans.setText("NO");
            holder.expireDate.setText("DATA");
        } else {
            Expires tmp = null;
            tmp = (Expires) list.get(position);

            String[] dd = tmp.getExpDate().split("-");
            String dateString = dd[2] + "/" + dd[1] + "/" + dd[0];

            holder.expireDate.setText(dateString);
            holder.daysAvans.setText(String.valueOf(tmp.getDaysToNotice()));

            vi.setOnClickListener(new OnItemClickListener(position));

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = sdf.parse(dateString);
                long expDateLong = date.getTime();
                long today = new Date().getTime();
                long dif = (expDateLong - today) / (24 * 60 * 60 * 1000);

                holder.daysLeft.setText("" + dif);
                if (dif <= 0) {
                    vi.setBackgroundColor(Color.RED);
                    holder.daysAvans.setTypeface(null, Typeface.BOLD);
                    holder.expireDate.setTypeface(null, Typeface.BOLD);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return vi;
    }

    public static class ViewHolder {
        public TextView daysAvans;
        public TextView expireDate;
        public TextView daysLeft;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private final int position;

        public OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            StockDetails stc = (StockDetails) activity;
            stc.onItemClick(position);
        }
    }
}
