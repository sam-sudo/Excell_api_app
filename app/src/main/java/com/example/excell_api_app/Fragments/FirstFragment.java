package com.example.excell_api_app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.excell_api_app.Adapters.Item_adapter;
import com.example.excell_api_app.Data;
import com.example.excell_api_app.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FirstFragment extends Fragment {

    private Item_adapter item_adapter;
    ListView dataList;
    String url = "https://script.google.com/macros/s/AKfycbxC793kDeQEODSimADvdpXvx4Rpvd8AyQzxSsn8AGXhAjdsVCCEFgDWsyPeWCNRHHU/exec";
    String url2 = "https://script.google.com/macros/s/AKfycbzlCSBaW09IhIMSSvWkMamVXGguCODYgGu0Y0TY3G6JTfRHbv1aOnBjcZR6CI3KEY9C/exec";
    ProgressDialog dialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView n_items;

    public FirstFragment() {
        // Required empty public constructor
    }

    public FirstFragment(String sheet) {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);


        dataList = (ListView) container.findViewById(R.id.dataList);
        n_items = container.findViewById(R.id.n_items);




        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Loading....");
        dialog.show();


        StringRequest request = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                Log.d("TAG", "onResponse: " + string);

                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(this.getContext());
        rQueue.add(request);

        return container;
    }





    //------------------ My methods ----------------------------

    void parseJsonData(String jsonString) {


        ArrayList<String> sheets = new ArrayList<>();
        String database ="";

        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject sheets_object = new JSONObject(jsonString);




            //USE THIS IN FRAGMENT TO SHEETS CHANGES
            JSONArray objectArray = sheets_object.getJSONArray("Hoja2");

            JSONObject data ;
            ArrayList<Object> data_list = new ArrayList();

            n_items.setText(objectArray.length() + "");

            navigationView = getActivity().findViewById(R.id.navigationView);

            navigationView.getMenu().getItem(0).getSubMenu().clear();
            navigationView.getMenu().getItem(1).getSubMenu().clear();
            addMenuTitle(0,database);

            for(int i = 0; i < sheets.size(); i++){
                addMenuTitle(1,sheets.get(i));
            }

            for(int i = 0; i < objectArray.length(); ++i) {

                data = objectArray.getJSONObject(i);

                HashMap<String,Object> item_temp = new HashMap<>();

                item_temp.put("NOMBRE",data.getString("NOMBRE"));
                item_temp.put("CIF",data.getString("CIF"));

                data_list.add(item_temp);

            }
            item_adapter = new Item_adapter(this.getContext(), data_list);
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