package com.example.excell_api_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private Item_adapter item_adapter;
    ListView dataList;
    String url = "https://script.google.com/macros/s/AKfycbxC793kDeQEODSimADvdpXvx4Rpvd8AyQzxSsn8AGXhAjdsVCCEFgDWsyPeWCNRHHU/exec";
    ProgressDialog dialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = (ListView)findViewById(R.id.dataList);

        //----------Create drawer----------

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Log.d("TAG", "onNavigationItemSelected: " + item.getTitle());

                return false;
            }
        });

        //----------Fin Create drawer---------



        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                //Log.d("TAG", "onResponse: " + string);

                parseJsonData(string);

                //addMenuTitle(1,"new2");
                //addMenuTitle(1,"new2");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
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

    void parseJsonData(String jsonString) {


        JSONArray databases = new JSONArray();
        String dataBase ="";

        try {
            JSONObject object = new JSONObject(jsonString);
            //To get all elements
            for (Iterator key = object.keys(); key.hasNext();){
                databases.put(key.next());
                Log.d("data---", "parseJsonData: " + databases);
            }
            dataBase = databases.get(0).toString();

            JSONArray objectArray = object.getJSONArray(dataBase);

            JSONObject data = new JSONObject();
            ArrayList<Item> data_list = new ArrayList();



            for(int i = 0; i < objectArray.length(); ++i) {
                data = objectArray.getJSONObject(i);
                Item temp_item = new Item();

                temp_item.setFirst_label(data.getString("NOMBRE"));
                temp_item.setSecond_label(data.getString("CIF"));
                temp_item.setThird_label(data.getString("MAIL"));

                data_list.add(temp_item);

            }
            item_adapter = new Item_adapter(this, data_list);
            dataList.setAdapter(item_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        navigationView.getMenu().getItem(0).getSubMenu().clear();
        navigationView.getMenu().getItem(1).getSubMenu().clear();
        addMenuTitle(0,"Excel");
        addMenuTitle(1,dataBase);

        dialog.dismiss();


    }

    void addMenuTitle(int menu, String title){
        //navigationView.getMenu().add(menu,Menu.NONE,0, title);
        //navigationView.getMenu().addSubMenu("db").add(title);
        navigationView.getMenu().getItem(menu).getSubMenu().add(title);
    }

}