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
import java.util.Calendar;
import java.util.Locale;

public class IndexActivity extends AppCompatActivity {

    private static final String TAG = "Index Activity";
    private TextView editText;
    private DatePickerDialog datePickerDialog;

    public static FragmentManager fragmentManager;

    SharedPreferences sharedPreferences;
    public static final String datePref = "datePref";
    public static final String activeDateKey = "activeDateKey";

    TextView dateTextView, totalOrdersTextView;

//    private ArrayList<DailyOrder> ordersList;

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

//    private void update(DataSnapshot dataSnapshot) {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        ordersList.clear();
//        long totalOrders = 0;
//
//        if(dataSnapshot.getValue() != null && dataSnapshot.getKey().equals(instance.dbDate)) {
//
//            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
//                DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
//                totalOrders += Long.parseLong(dailyOrder.getQuantity());
//                ordersList.add(dailyOrder);
//            }
//
//            totalOrdersTextView.setText("Total Orders: " + totalOrders);
//
//            fragmentTransaction.replace
//                    (
//                            R.id.fragment_container_index,
//                            RecyclerViewFragment.newInstance(ordersList),
//                            "RecyclerFragment"
//                    );
//            fragmentTransaction.commitAllowingStateLoss();
//
//        } else if(dataSnapshot.getValue() == null){
//            totalOrdersTextView.setText("Total Orders: 0");
//            fragmentTransaction.replace
//                    (
//                            R.id.fragment_container_index,
//                            NoOrderFragment.newInstance(),
//                            "NoOrderFragment"
//                    );
//            fragmentTransaction.commitAllowingStateLoss();
//        }
//
//    }

    private void setDate() {

        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        SimpleDateFormat basicFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        editText.setOnClickListener(v -> datePickerDialog.show());

        Calendar todayDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener listener =
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    instance.dbDate = dbFormat.format(newDate.getTime());
                    editText.setText(basicFormat.format(newDate.getTime()));
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

        instance = SingletonDateClass.getInstance();
        editText = findViewById(R.id.getDate);

        sharedPreferences = getSharedPreferences(datePref, Context.MODE_PRIVATE);

        logInstanceID();

        getUpcomingOrder();

        setDate();

//        ordersList = new ArrayList<>();

        dateTextView = (TextView) findViewById(R.id.date_textview);
        instance.dbDate = sharedPreferences.getString(activeDateKey, "20180701");
        dateTextView.setText(instance.getHrDate());

        totalOrdersTextView = (TextView) findViewById(R.id.totalorder_textview);
        totalOrdersTextView.setText("No Upcoming Orders");

        Button mButton = findViewById(R.id.button_check);
        mButton.setOnClickListener(v -> {
                if(editText.getText().toString().length() != 0) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("SENDER_KEY", "Index Activity");
                    startActivity(intent);
                } else {
                    Toast.makeText(IndexActivity.this, "Choose a date", Toast.LENGTH_SHORT).show();
                }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(activeDateKey, instance.dbDate);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText("");
        instance.dbDate = sharedPreferences.getString(activeDateKey, "20180701");
        dateTextView.setText(instance.getHrDate());
    }
}
