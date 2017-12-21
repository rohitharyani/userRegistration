package com.ecarus.userregistration;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class PreviousOrderAdapter extends RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder>{
    private List<PreviousOrderList> billList;
    private Context context;
    public static String OrderId;

    public PreviousOrderAdapter(List<PreviousOrderList> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_order_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final PreviousOrderList previousOrderList = billList.get(position);

        holder.mOrderId.setText(previousOrderList.getmOrderId());
        holder.mOrderDate.setText(previousOrderList.getmOrderDate());
        holder.mOrderTime.setText(previousOrderList.getmOrderTime());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderId = previousOrderList.getmOrderId();
                context.startActivity(new Intent(context, PreviousOrderDetails.class));
            }
        });


    }


    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mOrderId, mOrderDate, mOrderTime;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mOrderId =(TextView)itemView.findViewById(R.id.orderId);
            mOrderDate =(TextView)itemView.findViewById(R.id.orderDate);
            mOrderTime=(TextView)itemView.findViewById(R.id.orderTime);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }



}
