package com.biryanify.parichay.biryanify;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OrderHolder extends RecyclerView.ViewHolder {
    TextView nameTextView;
    TextView ordersTextView;

    public OrderHolder(View orderView, int nameTextViewID, int ordersTextViewID) {
        super(orderView);
        nameTextView = orderView.findViewById(nameTextViewID);
        ordersTextView = orderView.findViewById(ordersTextViewID);
    }
}
