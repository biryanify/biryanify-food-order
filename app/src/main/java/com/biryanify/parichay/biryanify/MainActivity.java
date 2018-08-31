package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements onDeleteOrder {

    private static final String TAG = "MainActivity";

    SharedPreferences sharedPreferences;
    public static final String datePref = "datePref";
    public static final String dbDateKey = "dbDateKey";

    TextView dateTextView, totalOrdersTextView;

    private DatePickerDialog datePickerDialog;

    public static FragmentManager fragmentManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<DailyOrder> ordersList;
    
    private DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener ordersRefListener;

    private SingletonDateClass instance;

    private void reflectChanges() {

        swipeRefreshLayout.setRefreshing(true);

        ordersRefListener =
            ordersRef
                .child("orders")
                .child(instance.dbDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        update(dataSnapshot);
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

            totalOrdersTextView.setText("Total Orders: " + totalOrders);

            fragmentTransaction.replace
                    (
                            R.id.fragment_container_main,
                            RecyclerViewFragment.newInstance(ordersList),
                            "RecyclerFragment"
                    );
            fragmentTransaction.commitAllowingStateLoss();

        } else if(dataSnapshot.getValue() == null){
            totalOrdersTextView.setText("Total Orders: 0");
            fragmentTransaction.replace
                    (
                            R.id.fragment_container_main,
                            NoOrderFragment.newInstance(),
                            "NoOrderFragment"
                    );
            fragmentTransaction.commitAllowingStateLoss();
        }

        swipeRefreshLayout.setRefreshing(false);
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

    private void setDate() {

        SimpleDateFormat dbFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        dateTextView = (TextView) findViewById(R.id.date_textview);
        dateTextView.setText(instance.getHrDate());

        dateTextView.setOnClickListener(v -> datePickerDialog.show());

        Calendar todayDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener listener =
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    instance.dbDate = dbFormat.format(newDate.getTime());
                    Intent recursiveIntent = new Intent(view.getContext(), MainActivity.class);
                    recursiveIntent.putExtra("SENDER_KEY", "MainActivity");
                    startActivity(recursiveIntent);
                    finish();
                };

        datePickerDialog = new DatePickerDialog(
                this,
                listener,
                todayDate.get(Calendar.YEAR),
                todayDate.get(Calendar.MONTH),
                todayDate.get(Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent sender = getIntent();
        String SENDER_ID = sender.getStringExtra("SENDER_KEY");

        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        startActivity(getIntent());
                        finish();
                    });

        ordersList = new ArrayList<>();

        fragmentManager = getSupportFragmentManager();

        sharedPreferences = getSharedPreferences(datePref, Context.MODE_PRIVATE);

        instance = SingletonDateClass.getInstance();

        if(SENDER_ID != null){
            if(SENDER_ID.equals("Notification Service")) {
                instance.dbDate = sharedPreferences.getString(dbDateKey, "01-07-2018");
            }
        }

        setDate();

        totalOrdersTextView = (TextView) findViewById(R.id.totalorder_textview);
        totalOrdersTextView.setText("Loading..");

        logInstanceID();

        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            reflectChanges();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_order_menu:
                addOrder();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addOrder() {
        startActivityForResult(FragmentActivity.newInstance(
                this,
                "add order"),
                1
        );
    }

    public void deleteOrder(DailyOrder order) {
        ordersRef
                .child("orders")
                .child(instance.dbDate)
                .child(order.getPhone()).removeValue();
        startActivity(getIntent());
        finish();
    }

    public void deleteOrder(DailyOrder order, Boolean refresh) {
        ordersRef
                .child("orders")
                .child(instance.dbDate)
                .child(order.getPhone()).removeValue();
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
            return;
        }
        Log.i("MainActivity", "nothing on backstack, calling super");
        finish();
    }

    private void modifyOrder(DailyOrder dailyOrder, String date) {
        deleteOrder(dailyOrder, false);

        Map<String, Object> order = dailyOrder.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/" + date + "/" + dailyOrder.getPhone(), order);

        ordersRef.updateChildren(childUpdates);

        startActivity(getIntent());
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                DailyOrder dailyOrder = data.getParcelableExtra("order");
                String date = data.getStringExtra("date");
                modifyOrder(dailyOrder, date);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ordersRef.removeEventListener(ordersRefListener);
        super.onDestroy();
    }
}
