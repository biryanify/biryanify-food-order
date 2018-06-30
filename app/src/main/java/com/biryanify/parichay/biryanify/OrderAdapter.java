package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<DailyOrder> {
    public OrderAdapter(Context context, int resource, List<DailyOrder> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_order, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView phoneTextView = (TextView) convertView.findViewById(R.id.phoneTextView);

        DailyOrder order = getItem(position);

        nameTextView.setVisibility(View.VISIBLE);
        nameTextView.setText(order.getName());

        phoneTextView.setText(order.getPhone());

        return convertView;

    }
}
