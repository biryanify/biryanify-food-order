package com.biryanify.parichay.biryanify;

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
        viewOrder.setText("Wait!");
        return view;
    }
}
