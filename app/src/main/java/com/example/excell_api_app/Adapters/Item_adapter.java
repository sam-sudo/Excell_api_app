package com.example.excell_api_app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.excell_api_app.Data;
import com.example.excell_api_app.Item;
import com.example.excell_api_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

        convertView = LayoutInflater.from(context).inflate(R.layout.info_item, null);


        LinkedHashMap<String,String> item = (LinkedHashMap ) getItem(position);
        ArrayList<String> img_list = new ArrayList<>();
        int default_ico = R.drawable.punto;

        img_list.add("empresa");
        img_list.add("cif");
        img_list.add("telefono");


        LinearLayout linearLayout =  convertView.findViewById(R.id.list_item);
        linearLayout.setOrientation(linearLayout.HORIZONTAL);

        LinearLayout linearLayout_internal_1 = new LinearLayout(context);
        LinearLayout linearLayout_internal_2 = new LinearLayout(context);
        ImageView imageView = new ImageView(context);


        //------Params to first linearLayout-------

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1);
        linearLayout_internal_1.setOrientation(linearLayout.VERTICAL);

        linearLayout_internal_1.setLayoutParams(params1);


        int img_count = 0;

        for (String key: item.keySet()) {

            TextView textView_temp2 = new TextView(context);
            ImageView img_temp = new ImageView(context);
            LinearLayout linearLayout_internal_1_1 = new LinearLayout(context);


            if (img_list.size() > img_count) {

                String img_name = img_list.get(img_count);
                int img_exit = context.getResources().getIdentifier(img_name, "drawable",context.getPackageName());

                if(img_exit != 0){
                    int imgID = context.getResources().getIdentifier(img_name,"drawable", context.getPackageName());
                    Drawable drawable = context.getResources().getDrawable(imgID);
                    img_temp.setImageDrawable(drawable);
                }else {
                    img_temp.setImageResource(default_ico);

                }

            }

            //------Params to first nº1 linearLayout-------

            LinearLayout.LayoutParams params1_1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1);
            params1_1.gravity = Gravity.CENTER;
            linearLayout_internal_1_1.setOrientation(linearLayout.HORIZONTAL);

            linearLayout_internal_1_1.setLayoutParams(params1_1);


            //------Params to first nº1 textView linearLayout-------

            LinearLayout.LayoutParams param_temp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1);

            textView_temp2.setLayoutParams(param_temp);

            //------Params to first nº1 imgView linearLayout-------

            LinearLayout.LayoutParams param_temp_img = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1);

            img_temp.setLayoutParams(param_temp_img);



            textView_temp2.setText(item.get(key));
            textView_temp2.setPadding(20,0,0,0);
            textView_temp2.setTextSize(15);
            textView_temp2.setGravity(Gravity.CENTER | Gravity.LEFT);


            linearLayout_internal_1_1.addView(img_temp);
            linearLayout_internal_1_1.addView(textView_temp2);


            linearLayout_internal_1.addView(linearLayout_internal_1_1);

            img_count++;
        }


        //------Params to second linearLayout-------

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                3);
        linearLayout_internal_2.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        linearLayout_internal_2.setLayoutParams(params2);

        //------Params to second imgView linearLayout-------

        LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(50,50);

        params_img.setMargins(0,0,0,20);

        imageView.setLayoutParams(params_img);




        imageView.setImageResource(R.drawable.pencil);
        linearLayout_internal_2.addView(imageView);


        linearLayout.addView(linearLayout_internal_1);
        linearLayout.addView(linearLayout_internal_2);

        return convertView;
    }
}
