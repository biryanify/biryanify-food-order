package com.biryanify.parichay.biryanify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddOrderFragment extends Fragment {
    private EditText nameEdittext, phoneEdittext, emailEdittext, itemEdittext, quantityEdittext, addressEdittext;
    private Button saveButton;
    private String date;

    private FragmentToActivity mCallback;

    public AddOrderFragment() {
    }

    public static AddOrderFragment newInstance(String date) {
        AddOrderFragment addOrderFragment = new AddOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        addOrderFragment.setArguments(bundle);
        return addOrderFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addorder, container, false);

        Bundle bundle = getArguments();
        date = bundle.getString("date");

        nameEdittext = view.findViewById(R.id.name_edittext);
        phoneEdittext = view.findViewById(R.id.phone_editText);
        emailEdittext = view.findViewById(R.id.email_edittext);
        itemEdittext = view.findViewById(R.id.item_edittext);
        quantityEdittext = view.findViewById(R.id.quantity_edittext);
        addressEdittext = view.findViewById(R.id.address_edittext);

        saveButton = view.findViewById(R.id.save_order_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DailyOrder dailyOrder = new DailyOrder();
                dailyOrder.setName(nameEdittext.getText().toString());
                dailyOrder.setPhone(phoneEdittext.getText().toString());
                dailyOrder.setEmail(emailEdittext.getText().toString());
                dailyOrder.setItem(itemEdittext.getText().toString());
                dailyOrder.setQuantity(quantityEdittext.getText().toString());
                Map<String, String> address = new HashMap<>();
                address.put("flat", addressEdittext.getText().toString());
                address.put("area", "");
                dailyOrder.setAddress(address);

                sendData(dailyOrder);
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment : Refresh called.",
                Toast.LENGTH_SHORT).show();
    }

    private void sendData(DailyOrder order)
    {
        mCallback.communicate(order);
    }

}
