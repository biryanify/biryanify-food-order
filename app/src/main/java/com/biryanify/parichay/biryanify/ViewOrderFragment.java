package com.biryanify.parichay.biryanify;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class ViewOrderFragment extends Fragment{

    private TextView viewOrder;
    private String info;
    DailyOrder order;
    private onDeleteOrder mCallback;

    public ViewOrderFragment() {
        info = "";
        order = new DailyOrder();
    }


    public static ViewOrderFragment newInstance(DailyOrder dailyOrder) {
        ViewOrderFragment viewOrderFragment = new ViewOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", dailyOrder);
        viewOrderFragment.setArguments(bundle);
        return viewOrderFragment;
    }

    private Calendar getCal(String time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(time) * 1000L);
        return cal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (onDeleteOrder) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vieworder, container, false);
        viewOrder = view.findViewById(R.id.view_order_textView);

        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        Bundle bundle = getArguments();
        order = bundle.getParcelable("order");

        info += "Name: "+order.getName()+
                "\n\nPhone: "+order.getPhone()+
                "\n\nEmail: "+order.getEmail()+
                "\n\nItem: "+order.getItem()+
                "\n\nQuantity: "+order.getQuantity()+
                "\n\nAddress: "+order.getAddress().get("flat") +", "+order.getAddress().get("area")+
                "\n\nSuggestion: "+order.getSuggestion();

        if(!order.getTime().equals("_unknown_")) {
            String date = DateFormat.format("EEE, dd/MM/yyyy, hh:mm aaa", getCal(order.getTime())).toString();
            info += "\n\nOrder placed on: " + date;
        }

        viewOrder.setText(info);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_order_menu:
                editOrder();
                return true;
            case R.id.delete_order_menu:
                sendOrder(order);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    private void sendOrder(DailyOrder order) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        mCallback.deleteOrder(order);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.i("MainActivity", "You said no to delete");
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete order?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void editOrder() {
        getActivity().startActivityForResult(
                FragmentActivity.newInstance(
                        getActivity(),
                        "edit order",
                        order),
                1
        );
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.add_order_menu).setVisible(false);
    }
}
