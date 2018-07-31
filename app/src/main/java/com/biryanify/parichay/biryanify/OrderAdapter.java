package com.biryanify.parichay.biryanify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<DailyOrder> mDailyOrders;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView phoneTextView;
        public ViewHolder(View orderView) {
            super(orderView);

            nameTextView = (TextView) orderView.findViewById(R.id.nameTextView);
            phoneTextView = (TextView) orderView.findViewById(R.id.phoneTextView);
        }
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
        public void onClick(View view) {

        }
    }

    public OrderAdapter(List<DailyOrder> dailyOrders) {
        mDailyOrders = dailyOrders;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View orderView = inflater.inflate(R.layout.item_order, parent, false);
        ViewHolder vh = new ViewHolder(orderView);

        return vh;
    }

    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        DailyOrder order = mDailyOrders.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(order.getName());
        TextView textView1 = holder.phoneTextView;
        textView1.setText(order.getPhone());
    }

    @Override
    public int getItemCount() {
        return mDailyOrders.size();
    }
}
