package com.example.excell_api_app.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.excell_api_app.Data;
import com.example.excell_api_app.Item;
import com.example.excell_api_app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Item_adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Object> list;

    public Item_adapter(Context context, ArrayList<Object> list) {
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

        HashMap<String,String> item = (HashMap) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.info_item, null);


        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.list_item);
        linearLayout.setOrientation(linearLayout.VERTICAL);



        for (String key: item.keySet()) {
            //Log.d("data---","mdg: " + key);
            TextView textView_temp = new TextView(context);
            textView_temp.setText(item.get(key));
            textView_temp.setTextSize(20);
            linearLayout.addView(textView_temp);
        }



        return convertView;
    }
}
