package com.ecarus.userregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListPersonalAndHouse extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItemProductList> listItemProductList;
    private ImageButton mCart,mRecommender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_product_display);

        mCart = (ImageButton)findViewById(R.id.productShoppingCartImageButton);
        mCart.setOnClickListener(this);

        mRecommender = (ImageButton)findViewById(R.id.RecommendationImageButton);
        mRecommender.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.productRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItemProductList = new ArrayList<>();

        loadRecyclerViewData();

    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading products...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_LIST_PCAHH_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("favProducts");

                    for (int i =0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        ListItemProductList item = new ListItemProductList(o.getString("category"), o.getString("productId"), o.getString("productName"),
                                o.getString("cost"),o.getString("weight"),o.getString("image"),
                                o.getString("quantity"),o.getString("mfgDate"),o.getString("expDate"));
                        listItemProductList.add(item);
                    }

                    adapter = new ListProductAdapter(listItemProductList, ListPersonalAndHouse.this);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "ERROR \n Data not loaded", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == mCart){
            startActivity(new Intent(this,ListCartActivity.class));
        }else if(v == mRecommender){
            startActivity(new Intent(this,RecommendationSystem.class));
        }

    }
}
