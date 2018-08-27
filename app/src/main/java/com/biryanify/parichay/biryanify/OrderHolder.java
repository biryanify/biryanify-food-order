package com.biryanify.parichay.biryanify;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView nameTextView;
    TextView ordersTextView;
    private RecyclerViewListener mListener;

    public OrderHolder(View orderView, int nameTextViewID, int ordersTextViewID, RecyclerViewListener listener) {
        super(orderView);
        nameTextView = orderView.findViewById(nameTextViewID);
        ordersTextView = orderView.findViewById(ordersTextViewID);
        mListener = listener;
        orderView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getAdapterPosition());
    }
}
