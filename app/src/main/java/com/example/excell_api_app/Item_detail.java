package com.example.excell_api_app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Item_detail extends AppCompatActivity {

    LinkedHashMap<String, String> objetJson;
    LinkedHashMap<String,String> item_detail = new LinkedHashMap<>();
    LinearLayout linearLayout_main;
    ActionBar actionBar;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        linearLayout_main = findViewById(R.id.main);

        //------Action bar---------
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //Con Gson pasamos el linkedHasMap como string y aqui lo transformamos en linkedHasMap
        Gson gson = new Gson();
        Type entityType = new TypeToken< LinkedHashMap<String, String>>(){}.getType();
        objetJson = gson.fromJson(String.valueOf(getIntent().getExtras().get("dataList")),entityType);

        item_detail.putAll(objetJson);


        //----------Recorremos el objetson que tiene los campos y los valores------------
        for(String key: objetJson.keySet()){
            Log.d("TAG", "onCreate: key" + key);
            EditText editText_temp = new EditText(this);
            TextView textView_temp = new TextView(this);

            textView_temp.setTextColor(Color.BLACK);
            editText_temp.setEnabled(false);

            textView_temp.setText(key);
            editText_temp.setText(objetJson.get(key));

            linearLayout_main.addView(textView_temp);
            linearLayout_main.addView(editText_temp);
        }




    }

    //-----------ActionBar back buton-----------
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
}
