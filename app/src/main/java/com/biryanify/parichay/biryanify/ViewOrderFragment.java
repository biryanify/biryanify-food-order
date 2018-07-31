package com.biryanify.parichay.biryanify;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewOrderFragment extends Fragment{

    private TextView viewOrder;

    public ViewOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vieworder_fragment, container, false);
        viewOrder = view.findViewById(R.id.view_order_textView);
        Bundle bundle = getArguments();
        DailyOrder order = (DailyOrder) bundle.getSerializable("order");
        String info = "Name: "+order.getName()+
                "\n\nPhone: "+order.getPhone()+
                "\n\nEmail: \n"+order.getEmail()+
                "\n\nItem: "+order.getItem()+
                "\n\nQuantity: "+order.getQuantity()+
                "\n\nAddress: "+order.getAddress().get("flat") +" "+order.getAddress().get("area");
        viewOrder.setText(info);
        return view;
    }
}
