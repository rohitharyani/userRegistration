package com.ecarus.userregistration;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

public class BarcodeProductAdapter extends RecyclerView.Adapter<BarcodeProductAdapter.ViewHolder>{
    private List<ListItemProductList> productList;
    private Context context;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


    public BarcodeProductAdapter(List<ListItemProductList> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_product_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ListItemProductList shoppingItemProduct = productList.get(position);

        holder.mName.setText(shoppingItemProduct.getmProductName());

        int price = Integer.parseInt(shoppingItemProduct.getmProductPrice());
        int discount = (int) (price - 0.1*price);
        String price1 = "₹"+String.valueOf(price);
        String discount1 = "₹"+String.valueOf(discount);
        String cost = discount1+" "+ price1;
        //holder.mPrice.setText(cost, TextView.BufferType.SPANNABLE);
       // Spannable spannable = (Spannable)holder.mPrice.getText();
      //  spannable.setSpan(new StrikethroughSpan(),price1.length(),cost.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    //    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), price1.length(),cost.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        holder.mPrice.setText("₹"+shoppingItemProduct.getmProductPrice());
        holder.mWeight.setText(shoppingItemProduct.getmProductWeight()+" Kg");
        holder.mMfg.setText(shoppingItemProduct.getmMfg());
        holder.mExp.setText(shoppingItemProduct.getmExp());
        Glide.with(context).load(shoppingItemProduct.getmProductImage()).crossFade().fitCenter().into(holder.mImage);

        holder.mRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SHOPPING_REMOVE_ITEMS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject object = new JSONObject(response);
                                    if(!object.getBoolean("error")){
                                        // Toast.makeText(context, listItemProduct.getmProductName()+ " "+object.getString("message"), Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, ShoppingMode.class));
                                    }else{
                                        //    Toast.makeText(context, listItemProduct.getmProductName()+ " "+object.getString("message"), Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, ShoppingMode.class));
                                    }
                                    //context.startActivity(new Intent(context, ListCartActivity.class));
                                    //  Toast.makeText(context, listItemProduct.getmProductName()+ " "+object.getString("message"), Toast.LENGTH_SHORT).show();
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
                        params.put("products", shoppingItemProduct.getmProductName());
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


        holder.mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SHOPPING_ADD_ITEMS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(!object.getBoolean("error")){
                                Toast.makeText(context, shoppingItemProduct.getmProductName()+ " "+object.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context,ShoppingMode.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }else {
                                Toast.makeText(context, shoppingItemProduct.getmProductName() + " " + object.getString("message"), Toast.LENGTH_SHORT).show();
                                //  Toast.makeText(context, listItemProduct.getmProductName()+ " "+object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
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
                        params.put("category", shoppingItemProduct.getmCategory());
                        params.put("productId", shoppingItemProduct.getmProductId());
                        params.put("date", date);
                        params.put("products", shoppingItemProduct.getmProductName());
                        params.put("cost", shoppingItemProduct.getmProductPrice());
                        params.put("weight", shoppingItemProduct.getmProductWeight());
                        params.put("image",shoppingItemProduct.getmProductImageRaw());
                        params.put("mfgDate",shoppingItemProduct.getmMfg());
                        params.put("expDate",shoppingItemProduct.getmExp());

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

        public TextView mName, mPrice, mWeight, mMfg,mExp;
        public ImageView mImage;
        public Button mRemoveProduct;
        public Button mAddProduct;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mName =(TextView)itemView.findViewById(R.id.ProductName);
            mPrice =(TextView)itemView.findViewById(R.id.ProductPrice);
            mWeight =(TextView)itemView.findViewById(R.id.ProductWeight);
            mImage = (ImageView)itemView.findViewById(R.id.ProductImage);
            mAddProduct = (Button)itemView.findViewById(R.id.addProduct);
            mRemoveProduct = (Button)itemView.findViewById(R.id.removeProduct);
            mMfg = (TextView)itemView.findViewById(R.id.mfgDate);
            mExp = (TextView)itemView.findViewById(R.id.expDate);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }



}
