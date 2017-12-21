package com.ecarus.userregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingMode extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mBarcode, mCheckout, mList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ShoppingItemProductList> shoppingItemProductList;
    private SearchView searchView;
    public static String mSearchBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_mode);

        mCheckout = (ImageButton)findViewById(R.id.ShoppingModeCheckoutImageButton);
        mCheckout.setOnClickListener(this);
        mBarcode = (ImageButton)findViewById(R.id.barcodeScanImageButton);
        mBarcode.setOnClickListener(this);
        mList = (ImageButton)findViewById(R.id.viewList);
        mList.setOnClickListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.shoppingModeProductRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(ShoppingMode.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        SnapHelper snapHelper1 = new PagerSnapHelper();
        snapHelper1.attachToRecyclerView(recyclerView);
        shoppingItemProductList = new ArrayList<>();

        loadRecyclerViewData();


        searchView = (SearchView)findViewById(R.id.barcodeSearchView);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("Search products by barcode...");
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               mSearchBarcode = query.trim();
               Intent intent = new Intent(ShoppingMode.this,BarcodeGetProduct.class);
               Bundle bundle = new Bundle();
               bundle.putString("barcode",mSearchBarcode);
               intent.putExtras(bundle);
               startActivity(intent);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SHOPPING_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("cartProducts");

                            for (int i =0; i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                ShoppingItemProductList item = new ShoppingItemProductList(o.getString("category"), o.getString("productId"),
                                        o.getString("products"),o.getString("cost"),o.getString("weight"),
                                        o.getString("image"),o.getString("quantity"),o.getString("mfgDate"),
                                        o.getString("expDate"),o.getString("total"));
                                shoppingItemProductList.add(item);
                            }

                            adapter = new ShoppingProductAdapter(shoppingItemProductList, ShoppingMode.this);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
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

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        if(shoppingItemProductList.size() == 0){
            adapter = new ShoppingCartEmptyAdapter(ShoppingMode.this);
            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent a = new Intent(this,SelectListOrShoppingMode.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if(v == mBarcode){
            startActivity(new Intent(this,BarcodeScanner.class));
        }
        else if(v == mCheckout){
            startActivity(new Intent(this,ShoppingCheckout.class));
        }
        if(v == mList){
            startActivity(new Intent(this,ViewListMode.class));
        }

    }

}
