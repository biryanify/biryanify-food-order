package com.biryanify.parichay.biryanify;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditOrderFragment extends Fragment{

    private onModifyOrder mCallback;
    private DailyOrder mOrder;
    public String mDbDate;
    private DatePickerDialog datePickerDialog;

    public EditOrderFragment() {
    }

    public static EditOrderFragment newInstance(DailyOrder order) {
        EditOrderFragment editOrderFragment = new EditOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", order);
        editOrderFragment.setArguments(bundle);
        return editOrderFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (onModifyOrder) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
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
        setField(view, R.id.date_editText, SingletonDateClass.getInstance().getBasicDate());
    }


    private void setField(View view, int eID, String defaultText) {
        EditText editText = view.findViewById(eID);
        editText.setText(defaultText);
        if(eID == R.id.phone_editText) {
            editText.setEnabled(false);
        } else if(eID == R.id.date_editText) {
            editText.setSelection(editText.getText().length());
        }
    }

    private void editOrder(View view, DailyOrder order) {
        // will not be able to edit phone number because it is unique id in db
        reflect(view, R.id.name_edittext, order::setName);
        reflect(view, R.id.email_edittext, order::setEmail);
        reflect(view, R.id.item_edittext, order::setItem);
        reflect(view, R.id.quantity_edittext, order::setQuantity);
        reflect(view, R.id.address_edittext, order::setFlat);
    }

    private void getDate(View view, int eID) {

        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        SimpleDateFormat basicFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        EditText dateEditText = view.findViewById(eID);
        mDbDate = SingletonDateClass.getInstance().dbDate;

        dateEditText.setOnClickListener(v -> datePickerDialog.show());

        Calendar todayDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener listener =
                (DatePicker v, int year, int monthOfYear, int dayOfMonth) -> {

                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    mDbDate = dbFormat.format(newDate.getTime());
                    dateEditText.setText(basicFormat.format(newDate.getTime()));
                };

        datePickerDialog = new DatePickerDialog(
                getContext(),
                listener,
                todayDate.get(Calendar.YEAR),
                todayDate.get(Calendar.MONTH),
                todayDate.get(Calendar.DAY_OF_MONTH)
        );
        Log.d("FragmentActivity", "Got this date "+ mDbDate);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addorder, container, false);

        setHasOptionsMenu(true);

        getDate(view, R.id.date_editText);

        Bundle bundle = getArguments();
        mOrder = (DailyOrder) bundle.getParcelable("order");

        if(mOrder != null) {
            buildFields(view, mOrder);
            editOrder(view, mOrder);
        }

        return view;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    private void sendOrder() {
        mCallback.modifyOrder(mOrder, mDbDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_order_menu:
                Toast.makeText(getContext(), "Saved to: "+ mDbDate, Toast.LENGTH_SHORT).show();
                sendOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
