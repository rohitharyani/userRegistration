package com.ecarus.userregistration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class ListRemainingProductAdapter extends RecyclerView.Adapter<ListRemainingProductAdapter.ViewHolder>{
    private List<ListItemProductList> productList;
    private Context context;


    public ListRemainingProductAdapter(List<ListItemProductList> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_remaining_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ListItemProductList listItemProductList = productList.get(position);

        holder.mName.setText(listItemProductList.getmProductName());
        holder.mQuantity.setText(listItemProductList.getmQuantity());
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mName, mQuantity;

        public ViewHolder(View itemView) {
            super(itemView);

            mName =(TextView)itemView.findViewById(R.id.ProductName);
            mQuantity = (TextView)itemView.findViewById(R.id.productQuantity);

        }
    }



}
