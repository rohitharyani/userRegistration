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

public class PreviousOrderDetailsAdapter extends RecyclerView.Adapter<PreviousOrderDetailsAdapter.ViewHolder>{
    private List<PreviousOrderDetailsList> billList;
    private Context context;


    public PreviousOrderDetailsAdapter(List<PreviousOrderDetailsList> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_order_details_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final PreviousOrderDetailsList previousOrderDetailsList = billList.get(position);

        holder.mProductName.setText(previousOrderDetailsList.getmProductName());
        holder.mProductBill.setText("₹ "+previousOrderDetailsList.getmProductBill());
        holder.mProductQuantity.setText(previousOrderDetailsList.getmProductQuantity());


    }


    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mProductName, mProductBill, mProductQuantity;

        public ViewHolder(View itemView) {
            super(itemView);

            mProductName =(TextView)itemView.findViewById(R.id.productName);
            mProductBill =(TextView)itemView.findViewById(R.id.productBill);
            mProductQuantity=(TextView)itemView.findViewById(R.id.productQuantity);
        }
    }



}
