package com.biryanify.parichay.biryanify;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mOrderListView;
    private OrderAdapter mOrderAdapter;
    private ProgressBar mProgressBar;

    List<DailyOrder> dailyOrders = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mOrderListView = (ListView) findViewById(R.id.orderListView);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        mOrdersDatabaseReference.child("orders").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {

                        DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
                        dailyOrders.add(dailyOrder);
                    }
                    mOrderAdapter = new OrderAdapter(MainActivity.this, R.layout.item_order, dailyOrders);
                    mOrderListView.setAdapter(mOrderAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    Intent intent1 = new Intent(MainActivity.this, FragmentActivity.class);
                    intent1.putExtra("type", "no orders");
                    startActivity(intent1);
                    finish();
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
}
