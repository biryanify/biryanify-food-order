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

    public static ViewOrderFragment newInstance(DailyOrder dailyOrder) {
        ViewOrderFragment viewOrderFragment = new ViewOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", dailyOrder);
        viewOrderFragment.setArguments(bundle);
        return viewOrderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vieworder, container, false);
        viewOrder = view.findViewById(R.id.view_order_textView);
        Bundle bundle = getArguments();
        DailyOrder order = (DailyOrder) bundle.getParcelable("order");
        String info = "Name: "+order.getName()+
                "\n\nPhone: "+order.getPhone()+
                "\n\nEmail: "+order.getEmail()+
                "\n\nItem: "+order.getItem()+
                "\n\nQuantity: "+order.getQuantity()+
                "\n\nAddress: "+order.getAddress().get("flat") +", "+order.getAddress().get("area");
        viewOrder.setText(info);
        return view;
    }
}
