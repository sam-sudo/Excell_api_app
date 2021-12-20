package com.example.excell_api_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.excell_api_app.Item;
import com.example.excell_api_app.R;

import java.util.ArrayList;

public class Item_adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> list;

    public Item_adapter(Context context, ArrayList<Item> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = (Item) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.info_item, null);

        TextView first_label = convertView.findViewById(R.id.first_label);
        TextView second_label = convertView.findViewById(R.id.second_label);
        TextView third_label = convertView.findViewById(R.id.third_label);

        first_label.setText(item.getFirst_label());
        second_label.setText(item.getSecond_label());
        third_label.setText(item.getThird_label());


        return convertView;
    }
}
