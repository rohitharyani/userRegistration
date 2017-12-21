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

import static android.graphics.Color.*;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder>{

    private List<ListItemProductList> productList;
    private Context context;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


    public ListProductAdapter(List<ListItemProductList> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_item,parent,false);

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
       // holder.mPrice.setText(cost, TextView.BufferType.SPANNABLE);
      //  Spannable spannable = (Spannable)holder.mPrice.getText();
       // spannable.setSpan(new StrikethroughSpan(),price1.length(),cost.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       // spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), price1.length(),cost.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        holder.mPrice.setText("₹"+listItemProductList.getmProductPrice());
        holder.mWeight.setText(listItemProductList.getmProductWeight());
        Glide.with(context).load(listItemProductList.getmProductImage()).crossFade().fitCenter().into(holder.mImage);

        /* holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, listItemProductList.getmProductName()+" added successfully to cart!", Toast.LENGTH_SHORT).show();
            }
        }); */

        holder.mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LIST_ADD_ITEMS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle(listItemProductList.getmProductName());
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
                            //Toast.makeText(context, listItemProductList.getmProductName()+ " "+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        params.put("category", listItemProductList.getmCategory());
                        params.put("productId", listItemProductList.getmProductId());
                        params.put("date", date);
                        params.put("products", listItemProductList.getmProductName());
                        params.put("cost", listItemProductList.getmProductPrice());
                        params.put("weight", listItemProductList.getmProductWeight());
                        params.put("image", listItemProductList.getmProductImageRaw());
                        params.put("mfgDate", listItemProductList.getmMfg());
                        params.put("expDate", listItemProductList.getmExp());

                        return params;
                    }
                };

                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
            }
        });

      /*  holder.mRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LIST_REMOVE_ITEMS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject object = new JSONObject(response);
                                    Toast.makeText(context, listItemProductList.getmProductName()+ " "+object.getString("message"), Toast.LENGTH_SHORT).show();

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
            }
        }); */

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

            mName =(TextView)itemView.findViewById(R.id.ProductName);
            mPrice =(TextView)itemView.findViewById(R.id.ProductPrice);
            mWeight =(TextView)itemView.findViewById(R.id.ProductWeight);
            mImage = (ImageView)itemView.findViewById(R.id.ProductImage);
            mAddProduct = (Button)itemView.findViewById(R.id.addProduct);
            //mRemoveProduct = (ImageButton)itemView.findViewById(R.id.removeProduct);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }



}
