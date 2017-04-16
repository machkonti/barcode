package com.example.machkonti.barcodescanningapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.machkonti.barcodescanningapp.Database.Expires;
import com.example.machkonti.barcodescanningapp.R;
import com.example.machkonti.barcodescanningapp.StockDetails;

import java.util.ArrayList;

/**
 * Created by Machkonti on 16.4.2017 г..
 */

public class ExpiresListAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    public Resources resources;
    Expires tmp = null;
    int i = 0;
    private Activity activity;
    private ArrayList list;

    public ExpiresListAdapter(Activity activity, ArrayList list, Resources resources) {
        this.activity = activity;
        this.list = list;
        this.resources = resources;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            vi = inflater.inflate(R.layout.expire_list_item, null);

            holder = new ViewHolder();
            holder.daysAvans = (TextView) vi.findViewById(R.id.daysAvans);
            holder.expireDate = (TextView) vi.findViewById(R.id.expireDate);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (list.size() <= 0) {
            holder.daysAvans.setText("NO");
            holder.expireDate.setText("DATA");
        } else {
            tmp = null;
            tmp = (Expires) list.get(position);

            holder.expireDate.setText(tmp.getExpDate());
            holder.daysAvans.setText(tmp.getDaysToNotice());

            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    public static class ViewHolder {
        public TextView daysAvans;
        public TextView expireDate;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int position;

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
