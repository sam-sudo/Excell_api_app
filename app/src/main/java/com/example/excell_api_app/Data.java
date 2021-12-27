package com.example.excell_api_app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.excell_api_app.Adapters.Item_adapter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Data extends AppCompatActivity {

    private Item_adapter item_adapter;
    private static final int MAX_INFO_DETAIL = 3;
    ListView dataList;
    TextView emptyDataList;
    TextView n_items;
    String sheet;

    String url = "https://script.google.com/macros/s/AKfycbxC793kDeQEODSimADvdpXvx4Rpvd8AyQzxSsn8AGXhAjdsVCCEFgDWsyPeWCNRHHU/exec";
    String url2 = "https://script.google.com/macros/s/AKfycbzlCSBaW09IhIMSSvWkMamVXGguCODYgGu0Y0TY3G6JTfRHbv1aOnBjcZR6CI3KEY9C/exec";

    ProgressDialog dialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SwipeRefreshLayout refreshLayout;


    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);


        dataList = (ListView)findViewById(R.id.dataList);
        emptyDataList = findViewById(R.id.empty);
        n_items = findViewById(R.id.n_items);
        refreshLayout = findViewById(R.id.refreshLayout);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getResponse();

            }
        });

        //----------Create drawer----------

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getResponse();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                sheet = item.getTitle().toString();
                parseJsonData(json,item.getTitle().toString());

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        //----------Fin Create drawer---------



    }

    private void getResponse() {

        dialog = new ProgressDialog(Data.this);
        dialog.setMessage("Loading....");
        dialog.show();


        StringRequest request = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                Log.d("TAG", "onResponse: " + string);

                makeMenu(string);
                parseJsonData(string,sheet);

                json = string;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Data.this);
        rQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------ My methods ----------------------------

    private String makeMenu(String json){
        ArrayList<String> sheets = new ArrayList<>();
        String database ="";
        Log.d("TAG", "makeMenu: make menuuu--- ");

        try {
            Log.d("TAG", "makeMenu: make menuuu--- dentro ");

            JSONObject object = new JSONObject(json);
            JSONObject sheets_object = new JSONObject(json);


            //To get all database

            for (Iterator key = object.keys(); key.hasNext(); ) {

                String database_temp = key.next().toString();
                database = database_temp;


            }

            sheets_object = object.getJSONObject(database);

            //To get all sheets
            for (Iterator key = sheets_object.keys(); key.hasNext(); ) {

                String sheet_temp = key.next().toString();
                sheets.add(sheet_temp);


            }



            navigationView.getMenu().getItem(0).getSubMenu().clear();
            navigationView.getMenu().getItem(1).getSubMenu().clear();
            addMenuTitle(0, database);

            for (int i = 0; i < sheets.size(); i++) {
                addMenuTitle(1, sheets.get(i));
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        Log.d("TAG", "makeMenu: make menuuu--- dismiss ");
        dialog.dismiss();




        return sheets.get(0);
    }

    private void parseJsonData(String jsonString,String sheet) {

        Log.d("TAG", "parseJsonData: paaaaarse ");

        ArrayList<String> sheets = new ArrayList<>();
        String database ="";


        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject sheets_object = new JSONObject(jsonString);


            //To get all database

            for (Iterator key = object.keys(); key.hasNext();){

                String database_temp = key.next().toString();
                database = database_temp;


            }

            sheets_object = object.getJSONObject(database);

            //To get all sheets
            for (Iterator key = sheets_object.keys(); key.hasNext();){

                String sheet_temp = key.next().toString();
                sheets.add(sheet_temp);


            }

            //USE THIS IN FRAGMENT TO SHEETS CHANGES
            JSONArray objectArray = sheets_object.getJSONArray(sheet);

            JSONObject data ;
            ArrayList<Object> data_list = new ArrayList();

            n_items.setText("Datos: " + objectArray.length());


            for(int i = 0; i < objectArray.length(); ++i) {

                LinkedHashMap<String,String> item_temp = new LinkedHashMap <>();
                int count = 0;
                data = objectArray.getJSONObject(i);



                for (Iterator key = data.keys(); key.hasNext();){

                    if (count < MAX_INFO_DETAIL && count <= data.length()){

                        String temp_value = key.next().toString();
                        item_temp.put(temp_value,data.getString(temp_value));
                        item_temp.put(temp_value,data.getString(temp_value));

                        count++;
                    }else {
                        break;
                    }
                }
                data_list.add(item_temp);

            }

            if(data_list != null && data_list.size() > 0){
                emptyDataList.setVisibility(View.INVISIBLE);
            }


            item_adapter = new Item_adapter(this, data_list);
            dataList.setAdapter(item_adapter);




        } catch (JSONException e) {
            e.printStackTrace();
        }



        dialog.dismiss();


    }


    void addMenuTitle(int menu, String title){
        navigationView.getMenu().getItem(menu).getSubMenu().add(title);
    }


}
