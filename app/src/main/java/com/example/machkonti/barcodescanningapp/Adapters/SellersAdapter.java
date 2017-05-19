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

import com.example.machkonti.barcodescanningapp.Database.Seller;
import com.example.machkonti.barcodescanningapp.R;
import com.example.machkonti.barcodescanningapp.SellersActivity;

import java.util.ArrayList;

/**
 * Created by Machkonti on 7.5.2017 г..
 */

public class SellersAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    private final Activity activity;
    int i = 0;
    private ArrayList list;
    private Resources resources;

    public SellersAdapter(Activity activity, ArrayList list, Resources resources) {
        this.activity = activity;
        this.list = list;
        this.resources = resources;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onClick(View v) {
        Log.v("SellerAdapter", "seller list item clicked !");
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
            vi = inflater.inflate(R.layout.seller_list_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.seller_name);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (list.size() <= 0) {
            holder.name.setText("NO DATA");
        } else {
            Seller tmp = null;
            tmp = (Seller) list.get(position);

            holder.name.setText(tmp.getName());

            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }


    public static class ViewHolder {
        public TextView name;
    }

    public class OnItemClickListener implements View.OnClickListener {
        private final int position;

        public OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            SellersActivity stc = (SellersActivity) activity;
            stc.onItemClick(position);
        }
    }
}
