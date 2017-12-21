package com.ecarus.userregistration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.ViewHolder>{
    private List<ListItemProductList> productList;
    private Context context;


    public ViewProductAdapter(List<ListItemProductList> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_cart_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ListItemProductList listItemProductList = productList.get(position);

        holder.mName.setText(listItemProductList.getmProductName());

        int price = Integer.parseInt(listItemProductList.getmProductPrice());
        int discount = (int) (price - 0.1*price);
        String price1 = "₹"+String.valueOf(price);
        String discount1 = "₹"+String.valueOf(discount);
        String cost = discount1+" "+ price1;
      //  holder.mPrice.setText(cost, TextView.BufferType.SPANNABLE);
        //Spannable spannable = (Spannable)holder.mPrice.getText();
        //spannable.setSpan(new StrikethroughSpan(),price1.length(),cost.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), price1.length(),cost.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        holder.mPrice.setText("₹"+listItemProductList.getmProductPrice());
        holder.mWeight.setText(listItemProductList.getmProductWeight());
        holder.mQuantity.setText(listItemProductList.getmQuantity());
        Glide.with(context).load(listItemProductList.getmProductImage()).crossFade().fitCenter().into(holder.mImage);

        /* holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, listItemProductList.getmProductName()+" added successfully to cart!", Toast.LENGTH_SHORT).show();
            }
        }); */


        holder.mRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIEW_REMOVE_ITEMS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject object = new JSONObject(response);
                                    context.startActivity(new Intent(context, ViewListMode.class));
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
                        params.put("products", listItemProductList.getmProductName());
                        return params;
                    }
                };

                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

               /* Intent intent = new Intent(context,ListCartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                */
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mName, mPrice, mWeight, mQuantity;
        public ImageView mImage;
        public ImageButton mRemoveProduct;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mName =(TextView)itemView.findViewById(R.id.ProductName);
            mPrice =(TextView)itemView.findViewById(R.id.ProductPrice);
            mWeight =(TextView)itemView.findViewById(R.id.ProductWeight);
            mImage = (ImageView)itemView.findViewById(R.id.ProductImage);
            mRemoveProduct = (ImageButton)itemView.findViewById(R.id.removeProduct);
            mQuantity = (TextView)itemView.findViewById(R.id.productQuantity);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }



}
