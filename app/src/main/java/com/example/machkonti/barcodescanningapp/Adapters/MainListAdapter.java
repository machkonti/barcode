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

import com.example.machkonti.barcodescanningapp.Database.Stocks;
import com.example.machkonti.barcodescanningapp.MainActivity;
import com.example.machkonti.barcodescanningapp.R;

import java.util.ArrayList;

/**
 * Created by machkonti on 16.4.2017 г..
 */

public class MainListAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    public Resources resources;
    Stocks tmp = null;
    int i = 0;
    private Activity activity;
    private ArrayList list;

    public MainListAdapter(Activity activity, ArrayList list, Resources resources) {
        this.activity = activity;
        this.list = list;
        this.resources = resources;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onClick(View v) {
        Log.v("MainListAdapter", "main list item clicked !");
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
            vi = inflater.inflate(R.layout.main_list_item, null);

            holder = new ViewHolder();
            holder.bCodeLabel = (TextView) vi.findViewById(R.id.bCodeLabel);
            holder.nameLabel = (TextView) vi.findViewById(R.id.nameLabel);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (list.size() <= 0) {
            holder.bCodeLabel.setText("no data!");
        } else {
            tmp = null;
            tmp = (Stocks) list.get(position);

            holder.bCodeLabel.setText(tmp.getbCode());
            holder.nameLabel.setText(tmp.getName());

            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    public static class ViewHolder {
        public TextView bCodeLabel;
        public TextView nameLabel;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int position;

        public OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            MainActivity stc = (MainActivity) activity;
            stc.onItemClick(position);
        }
    }
}
