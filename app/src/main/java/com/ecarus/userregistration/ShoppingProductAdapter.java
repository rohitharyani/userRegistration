package com.ecarus.userregistration;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class ShoppingProductAdapter extends RecyclerView.Adapter<ShoppingProductAdapter.ViewHolder>{
    private List<ShoppingItemProductList> productList;
    private Context context;


    public ShoppingProductAdapter(List<ShoppingItemProductList> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_mode_cart_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ShoppingItemProductList shoppingItemProductList = productList.get(position);

        holder.mName.setText(shoppingItemProductList.getmProductName());

        int price = Integer.parseInt(shoppingItemProductList.getmProductPrice());
        int discount = (int) (price - 0.1*price);
        String price1 = "₹"+String.valueOf(price);
        String discount1 = "₹"+String.valueOf(discount);
        String cost = discount1+" "+ price1;
        //holder.mPrice.setText(cost, TextView.BufferType.SPANNABLE);
        //Spannable spannable = (Spannable)holder.mPrice.getText();
        //spannable.setSpan(new StrikethroughSpan(),price1.length(),cost.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), price1.length(),cost.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        holder.mPrice.setText("₹"+shoppingItemProductList.getmProductPrice());
        holder.mWeight.setText(shoppingItemProductList.getmProductWeight());
        holder.mQuantity.setText(shoppingItemProductList.getmQuantity());
        Glide.with(context).load(shoppingItemProductList.getmProductImage()).crossFade().fitCenter().into(holder.mImage);

       holder.mMfgDate.setText(shoppingItemProductList.getmMfg());
        holder.mExpDate.setText(shoppingItemProductList.getmExp());
        holder.mTotal.setText("₹"+shoppingItemProductList.getmTotal());
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mName, mPrice, mWeight, mMfgDate, mExpDate, mQuantity, mTotal;
        public ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mName =(TextView)itemView.findViewById(R.id.ProductName);
            mPrice =(TextView)itemView.findViewById(R.id.ProductPrice);
            mWeight =(TextView)itemView.findViewById(R.id.ProductWeight);
            mImage = (ImageView)itemView.findViewById(R.id.ProductImage);
            mQuantity = (TextView)itemView.findViewById(R.id.ProductQuantity);
            mMfgDate = (TextView)itemView.findViewById(R.id.mfgDate);
            mExpDate = (TextView)itemView.findViewById(R.id.expDate);
            mTotal = (TextView)itemView.findViewById(R.id.total);

        }
    }



}
