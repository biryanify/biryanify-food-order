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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static FragmentManager fragmentManager;

    private TextView mTextView;
    private SimpleDateFormat originalFormat, targetFormat;

    private String date, dbDate;

    ArrayList<DailyOrder> dailyOrders = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ValueEventListener mOrderDatabaseReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent2 = getIntent();
        dbDate = intent2.getStringExtra("date");

        mTextView = (TextView) findViewById(R.id.date_textview);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        ParsePosition pos = new ParsePosition(0);
        Date originalDate = originalFormat.parse(dbDate, pos);
        targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);
        date = targetFormat.format(originalDate);

        mTextView.setText(date);

        mOrderDatabaseReferenceListener = mOrdersDatabaseReference.child("orders").child(dbDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(dataSnapshot.getValue() != null) {
                    dailyOrders.clear();
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
                        dailyOrders.add(dailyOrder);
                    }
                    fragmentTransaction.replace
                            (
                                    R.id.fragment_container2,
                                    RecyclerViewFragment.newInstance(dailyOrders, date),
                                    null
                            );
                    fragmentTransaction.commitAllowingStateLoss();
                } else {
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
        mOrdersDatabaseReference.removeEventListener(mOrderDatabaseReferenceListener);
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
                "add order",
                date),
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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        Map<String, Object> order = dailyOrder.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/"+dbDate+"/"+dailyOrder.getPhone(), order);

        mOrdersDatabaseReference.updateChildren(childUpdates);
    }
}
