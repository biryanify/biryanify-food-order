package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private long totalOrders;
    TextView dateTextView, totalOrdersTextView;

    public static FragmentManager fragmentManager;

    private ArrayList<DailyOrder> dailyOrders;
    
    private DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener ordersRefListener;

    private SingletonDateClass instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dailyOrders = new ArrayList<>();

        instance = SingletonDateClass.getInstance();

        dateTextView = (TextView) findViewById(R.id.date_textview);
        totalOrdersTextView = (TextView) findViewById(R.id.totalorder_textview);

        dateTextView.setText(instance.hrDate);
        totalOrdersTextView.setText("Loading..");

        logInstanceID();

        reflectChanges();

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

    private void reflectChanges() {

        ordersRefListener =
            ordersRef
                .child("orders")
                .child(instance.dbDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction =
                                fragmentManager.beginTransaction();
                        if(dataSnapshot.getValue() != null) {
                            dailyOrders.clear();
                            totalOrders = 0;
                            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                                DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
                                totalOrders += Long.parseLong(dailyOrder.getQuantity());
                                dailyOrders.add(dailyOrder);
                            }
                            totalOrdersTextView.setText("Total Orders: " + totalOrders);
                            fragmentTransaction.replace
                                    (
                                            R.id.fragment_container2,
                                            RecyclerViewFragment.newInstance(dailyOrders),
                                            null
                                    );
                            fragmentTransaction.commitAllowingStateLoss();
                        } else {
                            totalOrdersTextView.setText("Total Orders: 0" );
                            fragmentTransaction.replace
                                    (
                                            R.id.fragment_container2,
                                            NoOrderFragment.newInstance(),
                                            null
                                    );
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ordersRef.removeEventListener(ordersRefListener);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addOrder() {
        startActivityForResult(FragmentActivity.newInstance(
                this,
                "add order"),
                1
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                DailyOrder dailyOrder = data.getParcelableExtra("order");
                Toast.makeText(this, "Orded Added", Toast.LENGTH_SHORT).show();
                writeData(dailyOrder);
            }
        }
    }

    private void writeData(DailyOrder dailyOrder) {

        Map<String, Object> order = dailyOrder.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/"+instance.dbDate+"/"+dailyOrder.getPhone(), order);

        ordersRef.updateChildren(childUpdates);
    }
}
