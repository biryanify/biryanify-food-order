package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.support.design.internal.ParcelableSparseArray;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.internal.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    private TextView mTextView;
    private SimpleDateFormat originalFormat, targetFormat;
    private String date, dbDate;

    ArrayList<DailyOrder> dailyOrders = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mTextView = (TextView) findViewById(R.id.date_textview);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        Intent intent2 = getIntent();
        dbDate = intent2.getStringExtra("date");

        originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        ParsePosition pos = new ParsePosition(0);
        Date originalDate = originalFormat.parse(dbDate, pos);
        targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);
        date = targetFormat.format(originalDate);

        mTextView.setText(date);

        mOrdersDatabaseReference.child("orders").child(dbDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
                        dailyOrders.add(dailyOrder);
                    }
                    fragmentTransaction.add
                            (
                                    R.id.fragment_container2,
                                    RecyclerViewFragment.newInstance(dailyOrders),
                                    null
                            );
                    fragmentTransaction.commit();
                } else {
                    fragmentTransaction.add
                            (
                                R.id.fragment_container2,
                                NoOrderFragment.newInstance(),
                                null
                            );
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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

    private void writeData(DailyOrder dailyOrder) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        Map<String, Object> order = dailyOrder.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/"+dbDate+"/"+dailyOrder.getPhone(), order);

        mOrdersDatabaseReference.updateChildren(childUpdates);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                DailyOrder dailyOrder = data.getParcelableExtra("order");
                Toast.makeText(this, dailyOrder.getName(), Toast.LENGTH_SHORT).show();
//                writeData(dailyOrder);
            }
        }
    }
}
