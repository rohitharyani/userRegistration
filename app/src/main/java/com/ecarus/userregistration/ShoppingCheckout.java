package com.ecarus.userregistration;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecarus.userregistration.UserProfileBarcode.bmp;

public class ShoppingCheckout extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItemProductList> listItemProductList;

    private TextView mQuantity, mTotal, mDiscount, mSubTotal, mVAT, mPayable;
    private Button mPayBill, mYes, mNo;
    private RatingBar mRatingBar;
    private static int ratingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_checkout);

        mQuantity = (TextView)findViewById(R.id.ShoppingQuantity);
        mTotal = (TextView)findViewById(R.id.ShoppingTotal);
        mDiscount = (TextView)findViewById(R.id.ShoppingDiscount);
        mSubTotal = (TextView)findViewById(R.id.ShoppingSubTotal);
        mVAT = (TextView)findViewById(R.id.ShoppingVAT);
        mPayable = (TextView)findViewById(R.id.ShoppingPayable);
        mPayBill = (Button)findViewById(R.id.ShoppingPayBill);
        mPayBill.setOnClickListener(this);


        getBillAmount();

    }

    private void getBillAmount() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching total bill amount...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SHOPPING_BILL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if(!object.getBoolean("error")){
                                mTotal.setText("₹ "+object.get("bill"));
                                mQuantity.setText(object.getString("quantity"));
                                mDiscount.setText("₹ "+object.get("discount"));
                                mSubTotal.setText("₹ "+object.get("subTotal"));
                                mVAT.setText("₹ "+object.get("vat"));
                                mPayable.setText("₹ "+object.get("payable"));
                                // Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            else{
                                setContentView(R.layout.cart_is_empty);
                            }

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
    }


    private void listRemainingItems() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finalising shopping list...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LIST_REMAINING_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if(!object.getBoolean("error")){
                                askRating();
                            }
                            else{
                                confirmEmptyList();
                            }

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
    }

    private void confirmEmptyList() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching remaining items...");
        progressDialog.show();

        listItemProductList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LIST_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object1 = new JSONObject(response);
                            JSONArray array = object1.getJSONArray("cartProducts");

                            for (int i =0; i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                ListItemProductList item = new ListItemProductList(o.getString("category"), o.getString("productId"),
                                        o.getString("products"),o.getString("cost"),o.getString("weight"),
                                        o.getString("image"),o.getString("quantity"),o.getString("mfgDate"),o.getString("expDate"));
                                listItemProductList.add(item);
                            }

                            adapter = new ListRemainingProductAdapter(listItemProductList, ShoppingCheckout.this);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_empty_list, null);

        //AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingCheckout.this);
        //alert.setView(confirmDialog);

        final Dialog dialog = new Dialog(ShoppingCheckout.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(confirmDialog);
        dialog.getWindow().getDecorView().setBottom(100);
        recyclerView = (RecyclerView)dialog.findViewById(R.id.productRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dialog.show();
        dialog.setCancelable(false);

        //  final AlertDialog alertDialog = alert.create();
       // alertDialog.show();

        mYes = (Button)confirmDialog.findViewById(R.id.yesButton);
        mNo = (Button)confirmDialog.findViewById(R.id.noButton);

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LIST_EMPTY,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if(!object.getBoolean("error")){
                                        dialog.dismiss();
                                        askRating();
                                    }
                                    else{
                                        Toast.makeText(ShoppingCheckout.this, "ERROR: Please try again!", Toast.LENGTH_SHORT).show();
                                    }

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

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void askRating(){
        LayoutInflater li = LayoutInflater.from(this);
        View ratingDialog = li.inflate(R.layout.dialog_rating, null);

        final Dialog dialog = new Dialog(ShoppingCheckout.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(ratingDialog);
        dialog.getWindow().getDecorView().setBottom(100);
        dialog.setCancelable(false);
        dialog.show();

        mRatingBar = (RatingBar)ratingDialog.findViewById(R.id.ratingBar);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = (int) rating;
                dialog.dismiss();
                walletCheckAndCheckout();
            }
        });

    }




    private void walletCheckAndCheckout() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finalising shopping list...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_WALLET_CHECK_AND_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if(!object.getBoolean("error")){

                                shoppingCheckout();

                            }
                            else{
                                balanceUpdate();
                            }

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
                params.put("rating", String.valueOf(ratingValue));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void shoppingCheckout() {
        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        l.setOrientation(LinearLayout.VERTICAL);

        l.setBackgroundColor(Color.parseColor("#0099cc"));
        //setContentView(l);


        String barcode_data = SharedPrefManager.getInstance(this).getUserPhone();
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(barcode_data, BarcodeFormat.QR_CODE, 1024, 1024);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            TextView tv1 = new TextView(this);
            tv1.setGravity(Gravity.CENTER);
            tv1.setBackgroundColor(Color.parseColor("#33b5e5"));
            tv1.setText("Payment successful!");
            tv1.setTextColor(Color.WHITE);
            tv1.setTextSize(22);
            tv1.setTypeface(Typeface.SANS_SERIF);
            tv1.setTypeface(null,Typeface.BOLD);
            tv1.setPadding(30,25,0,25);
            l.addView(tv1);


            LinearLayout l1 = new LinearLayout(this);
            l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            l1.setOrientation(LinearLayout.VERTICAL);

            l1.setBackgroundColor(Color.parseColor("#0099cc"));
            l1.setGravity(Gravity.CENTER);

            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setText("Scan this ECARUS code at the exit counter!");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16);
            tv.setPadding(15,0,15,30);
            l1.addView(tv);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bmp);
            imageView.setPadding(0,30,0,0);
            l1.addView(imageView);


            TextView phone = new TextView(this);
            phone.setGravity(Gravity.CENTER_HORIZONTAL);
            phone.setText(SharedPrefManager.getInstance(this).getUserPhone());
            phone.setTextColor(Color.WHITE);
            phone.setTextSize(16);
            phone.setPadding(15,0,15,30);
            l1.addView(phone);

            l.addView(l1);

        }
        catch (WriterException e) {
            e.printStackTrace();
        }


        final Dialog dialog = new Dialog(ShoppingCheckout.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(l);
        dialog.getWindow().getDecorView().setBottom(100);
        dialog.setCancelable(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                finish();
                Intent intent = new Intent(ShoppingCheckout.this,ShoppingMode.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 60000);

    }


    private void balanceUpdate() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_update_balance, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingCheckout.this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        mYes = (Button)confirmDialog.findViewById(R.id.yesButton);
        mNo = (Button)confirmDialog.findViewById(R.id.noButton);

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent a = new Intent(ShoppingCheckout.this,WalletAddMoney.class);
                startActivity(a);
            }
        });

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == mPayBill){
            listRemainingItems();
        }
    }


}
