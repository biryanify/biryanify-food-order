package com.biryanify.parichay.biryanify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {

    private List<DailyOrder> dailyOrders;

    public OrderAdapter(List<DailyOrder> dailyOrders) {
        this.dailyOrders = dailyOrders;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderView = inflater.inflate(R.layout.item_order, parent, false);
        return new OrderHolder(orderView,R.id.nameTextView, R.id.ordersTextView);
    }

    @Override
    public void onBindViewHolder(final OrderHolder holder, int position) {
        DailyOrder order = dailyOrders.get(position);

        holder.nameTextView.setText(order.getName());
        String orderText = "Orders: " + order.getQuantity();
        holder.ordersTextView.setText(orderText);
    }

    @Override
    public int getItemCount() {
        return dailyOrders.size();
    }
}
