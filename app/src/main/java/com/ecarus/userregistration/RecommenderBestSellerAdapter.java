package com.ecarus.userregistration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class RecommenderBestSellerAdapter extends RecyclerView.Adapter<RecommenderBestSellerAdapter.ViewHolder>{

    private List<RecommendedList> productList;
    private Context context;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


    public RecommenderBestSellerAdapter(List<RecommendedList> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommender_bestseller_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RecommendedList recommendedList = productList.get(position);

        holder.mName.setText(recommendedList.getmProductName());

        int price = Integer.parseInt(recommendedList.getmProductPrice());
        int discount = (int) (price - 0.1*price);
        String price1 = "₹"+String.valueOf(price);
        String discount1 = "₹"+String.valueOf(discount);
        String cost = discount1+" "+ price1;
      //  holder.mPrice.setText(cost, TextView.BufferType.SPANNABLE);
    //    Spannable spannable = (Spannable)holder.mPrice.getText();
  //      spannable.setSpan(new StrikethroughSpan(),price1.length(),cost.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), price1.length(),cost.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        holder.mPrice.setText("₹"+recommendedList.getmProductPrice());
        holder.mWeight.setText(recommendedList.getmProductWeight());
        Glide.with(context).load(recommendedList.getmProductImage()).crossFade().fitCenter().into(holder.mImage);


        holder.mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LIST_ADD_ITEMS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle(recommendedList.getmProductName());
                            alert.setMessage(jsonObject.getString("message"));
                            alert.setCancelable(false);

                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;

                                }
                            });
                            final AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone", SharedPrefManager.getInstance(context).getUserPhone());
                        params.put("category", recommendedList.getmCategory());
                        params.put("productId", recommendedList.getmProductId());
                        params.put("date", date);
                        params.put("products", recommendedList.getmProductName());
                        params.put("cost", recommendedList.getmProductPrice());
                        params.put("weight", recommendedList.getmProductWeight());
                        params.put("image", recommendedList.getmProductImageRaw());
                        params.put("mfgDate", recommendedList.getmMfg());
                        params.put("expDate", recommendedList.getmExp());
                        return params;
                    }
                };

                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mName, mPrice, mWeight;
        public ImageView mImage;
        public Button mAddProduct;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mName =(TextView)itemView.findViewById(R.id.productName);
            mPrice =(TextView)itemView.findViewById(R.id.productPrice);
            mWeight =(TextView)itemView.findViewById(R.id.productWeight);
            mImage = (ImageView)itemView.findViewById(R.id.productImage);
            mAddProduct = (Button)itemView.findViewById(R.id.addProduct);
        }
    }



}
