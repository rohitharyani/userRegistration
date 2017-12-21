package com.ecarus.userregistration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationSystem extends AppCompatActivity {

    private RecyclerView bestSellerRecyclerView, similarRatedRecyclerView;
    private RecyclerView.Adapter adapter;
    private List<RecommendedList> bestSellerList, similarRatedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_system);

        bestSellerRecyclerView = (RecyclerView)findViewById(R.id.BestSellerRecyclerView);

        similarRatedRecyclerView = (RecyclerView)findViewById(R.id.SimilarRatesRecyclerView);
        SnapHelper snapHelper1 = new PagerSnapHelper();
        snapHelper1.attachToRecyclerView(similarRatedRecyclerView);


        bestSellerList = new ArrayList<>();
        similarRatedList = new ArrayList<>();

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(RecommendationSystem.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager1
                = new LinearLayoutManager(RecommendationSystem.this, LinearLayoutManager.HORIZONTAL, false);
        bestSellerRecyclerView.setLayoutManager(horizontalLayoutManager);
        similarRatedRecyclerView.setLayoutManager(horizontalLayoutManager1);


        loadBestSeller();
        loadSimilarRated();

    }


    private void loadBestSeller() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RECOMMENDED_BS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("bestSeller");

                    for (int i =0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        RecommendedList item = new RecommendedList(o.getString("category"), o.getString("productId"),
                                o.getString("products"),o.getString("cost"),o.getString("weight"),
                                o.getString("image"),o.getString("mfgDate"),o.getString("expDate"));
                        bestSellerList.add(item);
                    }

                    adapter = new RecommenderBestSellerAdapter(bestSellerList, RecommendationSystem.this);
                    bestSellerRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR \n Data not loaded", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadSimilarRated() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RECOMMENDED_SR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("similarRated");

                            for (int i =0; i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                RecommendedList item = new RecommendedList(o.getString("category"), o.getString("productId"),
                                        o.getString("products"),o.getString("cost"),o.getString("weight"),
                                        o.getString("image"),o.getString("mfgDate"),o.getString("expDate"));
                                similarRatedList.add(item);
                            }

                            adapter = new RecommenderSimilarRatedAdapter(similarRatedList, RecommendationSystem.this);
                            similarRatedRecyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", SharedPrefManager.getInstance(getApplicationContext()).getUserPhone());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
