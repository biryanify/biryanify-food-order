package com.biryanify.parichay.biryanify;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class IndexActivity extends AppCompatActivity {

    private static final String TAG = "Index Activity";
    private TextView dateEditText;
    private DatePickerDialog datePickerDialog;
    SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);

    public static FragmentManager fragmentManager;

    SharedPreferences sharedPreferences;
    public static final String datePref = "datePref";
    public static final String activeDateKey = "activeDateKey";

    TextView dateTextView, orderTextView, timeTextView;
    Calendar todayDate;

    private ArrayList<DailyOrder> ordersList;

    private SingletonDateClass instance;

    private DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener ordersRefListener;

    private void getUpcomingOrder() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add
                (
                        R.id.fragment_container_index,
                        NoOrderFragment.newInstance(),
                        ""
                );
        fragmentTransaction.commitAllowingStateLoss();
        ordersRefListener =
                ordersRef
                        .child("orders")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, dataSnapshot.toString());
//                                update(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
    }

    private void update(DataSnapshot dataSnapshot) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ordersList.clear();
        long totalOrders = 0;

        if(dataSnapshot.getValue() != null && dataSnapshot.getKey().equals(instance.dbDate)) {

            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
                totalOrders += Long.parseLong(dailyOrder.getQuantity());
                ordersList.add(dailyOrder);
            }

            orderTextView.setText("Total Orders: " + totalOrders);

            fragmentTransaction.replace
                    (
                            R.id.fragment_container_index,
                            RecyclerViewFragment.newInstance(ordersList),
                            "RecyclerFragment"
                    );
            fragmentTransaction.commitAllowingStateLoss();

        } else if(dataSnapshot.getValue() == null){
            orderTextView.setText("Total Orders: 0");
            fragmentTransaction.replace
                    (
                            R.id.fragment_container_index,
                            NoOrderFragment.newInstance(),
                            "NoOrderFragment"
                    );
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void getDate() {
        SimpleDateFormat basicFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        dateEditText.setOnClickListener(v -> datePickerDialog.show());

        DatePickerDialog.OnDateSetListener listener =
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(activeDateKey, dbFormat.format(newDate.getTime()));
                    editor.apply();

                    dateEditText.setText(basicFormat.format(newDate.getTime()));
                };

        datePickerDialog = new DatePickerDialog(
                this,
                listener,
                todayDate.get(Calendar.YEAR),
                todayDate.get(Calendar.MONTH),
                todayDate.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void logInstanceID() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, msg);
                        }
                );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        logInstanceID();

        sharedPreferences = getSharedPreferences(datePref, Context.MODE_PRIVATE);

        instance = SingletonDateClass.getInstance();
        dateEditText = findViewById(R.id.getDate);

        todayDate = Calendar.getInstance();

        getDate();

        getUpcomingOrder();

        orderTextView = (TextView) findViewById(R.id.totalorder_textview);
        orderTextView.setText("Upcoming Orders");

        Button mButton = findViewById(R.id.button_check);
        mButton.setOnClickListener(v -> {
                if(dateEditText.getText().toString().length() != 0) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("SENDER_KEY", "Index Activity");
                    startActivity(intent);
                } else {
                    Toast.makeText(IndexActivity.this, "Choose a date", Toast.LENGTH_SHORT).show();
                }

        });
    }

    private void setDate() {
        instance.dbDate = dbFormat.format(todayDate.getTime());

        dateTextView = (TextView) findViewById(R.id.date_textview);
        dateTextView.setText(instance.getHrDate());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDate();
        dateEditText.setText("");
    }

    @Override
    protected void onDestroy() {
        ordersRef.removeEventListener(ordersRefListener);
        super.onDestroy();
    }

}
