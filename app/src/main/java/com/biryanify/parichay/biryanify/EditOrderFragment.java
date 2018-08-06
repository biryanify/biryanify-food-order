package com.biryanify.parichay.biryanify;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditOrderFragment extends Fragment{
    private Button saveButton;

    private FragmentToActivity mCallback;

    public EditOrderFragment() {
    }

    public static EditOrderFragment newInstance(DailyOrder dailyOrder) {
        EditOrderFragment editOrderFragment = new EditOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", dailyOrder);
        editOrderFragment.setArguments(bundle);
        return editOrderFragment;
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

    public interface Consumer<T> {
        void accept(T data);
    }

    public <T> void reflect(View view, int eID, Consumer<String> onTextChanged) {
        EditText editText = view.findViewById(eID);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChanged.accept(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void buildFields(View view, DailyOrder order) {
        setField(view, R.id.name_edittext, order.getName());
        setField(view, R.id.phone_editText, order.getPhone());
        setField(view, R.id.email_edittext, order.getEmail());
        setField(view, R.id.item_edittext, order.getItem());
        setField(view, R.id.quantity_edittext, order.getQuantity());
        setField(view, R.id.address_edittext,
                order.getAddress().get("flat") +
                        ", " + order.getAddress().get("area"));
    }


    private void setField(View view, int eID, String defaultText) {
        EditText editText = view.findViewById(eID);
        if(eID == R.id.phone_editText) {
            editText.setEnabled(false);
        }
        editText.setText(defaultText);
    }

    private void editOrder(View view, DailyOrder order) {
        reflect(view, R.id.name_edittext, order::setName);
//        reflect(view, R.id.phone_editText, order::setPhone);
        reflect(view, R.id.email_edittext, order::setEmail);
        reflect(view, R.id.item_edittext, order::setItem);
        reflect(view, R.id.quantity_edittext, order::setQuantity);
        reflect(view, R.id.address_edittext, order::setFlat);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addorder, container, false);

        Bundle bundle = getArguments();
        DailyOrder order = (DailyOrder) bundle.getParcelable("order");

        buildFields(view, order);
        editOrder(view, order);

        saveButton = view.findViewById(R.id.save_order_button);
        saveButton.setOnClickListener(v -> sendOrder(order));

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
    private void sendOrder(DailyOrder order) {
        mCallback.communicate(order);
    }
}
