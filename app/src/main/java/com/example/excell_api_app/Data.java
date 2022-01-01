package com.example.excell_api_app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Data extends AppCompatActivity {

    private Item_adapter item_adapter;
    ListView dataList;
    TextView emptyDataList;
    TextView n_items;
    String sheet;
    String word_to_search;
    ArrayList<LinkedHashMap<String,String>> data_list = new ArrayList();

    String url = "https://script.google.com/macros/s/AKfycbxC793kDeQEODSimADvdpXvx4Rpvd8AyQzxSsn8AGXhAjdsVCCEFgDWsyPeWCNRHHU/exec";
    String url2 = "https://script.google.com/macros/s/AKfycbzy0Oq5guaNAlQdRWYZvHlc3EnoVoCfpLn13d5Y1iS9yJs8cke0U-ZtBUtEmDnSkjEw/exec";

    ProgressDialog dialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SwipeRefreshLayout refreshLayout;
    SearchView searchView;

    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);


        dataList = (ListView)findViewById(R.id.dataList);
        emptyDataList = findViewById(R.id.empty);
        n_items = findViewById(R.id.n_items);
        refreshLayout = findViewById(R.id.refreshLayout);
        searchView = findViewById(R.id.search);


        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Data.this, Item_detail.class);
                Gson gson = new Gson();
                String list_parsed = gson.toJson(data_list.get(position));
                intent.putExtra("dataList",  list_parsed);
                startActivity(intent);
            }
        });

        //----------Create refresh----------

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);

                if(sheet != null){
                    getResponse();
                    searchView.setQuery("",false);
                    searchView.clearFocus();

                }

            }
        });

        //----------Create search----------

        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG", "parseJsonData: word --- " + newText.toLowerCase());
                parseJsonData(json,sheet,newText.toLowerCase());
                return false;
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

                try {
                    sheet = item.getTitle().toString();
                    parseJsonData(json,item.getTitle().toString(),null);
                }catch (Exception e){

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        //----------Fin Create drawer---------




    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------ My methods ----------------------------

    private void getResponse() {

        dialog = new ProgressDialog(Data.this);
        dialog.setMessage("Loading....");
        dialog.show();


        StringRequest request = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                Log.d("TAG", "onResponse: " + string);

                makeMenu(string);
                parseJsonData(string,sheet,null);

                json = string;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Data.this);
        rQueue.add(request);
    }

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

    private void parseJsonData(String jsonString,String sheet, @Nullable String word) {


        Log.d("TAG", "parseJsonData: paaaaarse ");
        Log.d("TAG", "parseJsonData: word1 " + word);


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

            JSONArray objectArray = sheets_object.getJSONArray(sheet);
            data_list = new ArrayList();

            JSONObject data ;


            for(int i = 0; i < objectArray.length(); ++i) {

                LinkedHashMap<String,String> item_temp = new LinkedHashMap <>();

                data = objectArray.getJSONObject(i);

                //here get all datas and add into object
                for (Iterator key = data.keys(); key.hasNext();){

                    String temp_value = key.next().toString();
                    item_temp.put(temp_value,data.getString(temp_value));

                }


                if(word == null || word.length() <= 0 ){
                    data_list.add(item_temp);
                }else {
                    boolean isInlist = searchItem(item_temp, word);

                    if(isInlist){
                        data_list.add(item_temp);
                    }
                }

            }

            if(data_list != null && data_list.size() > 0){
                emptyDataList.setVisibility(View.INVISIBLE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                params.setMargins(10,10,10,10);
                refreshLayout.setLayoutParams(params);

            }

            n_items.setText("Datos: " + data_list.size());

            item_adapter = new Item_adapter(this, data_list);
            dataList.setAdapter(item_adapter);
            item_adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();

    }

    private Boolean searchItem(LinkedHashMap<String, String> item, String word){
        Log.d("TAG", "seachItem:  mmmmmm " + item);


        for (String key : item.values()){
            if(key.toLowerCase().startsWith(word.toLowerCase())){
                Log.d("TAG", "seachItem:  is true" );
                return true;
            }
        }

        return false;
    }

    void addMenuTitle(int menu, String title){
        navigationView.getMenu().getItem(menu).getSubMenu().add(title);
    }


}
