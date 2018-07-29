package com.biryanify.parichay.biryanify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mOrderListView;
    private OrderAdapter mOrderAdapter;
    private ProgressBar mProgressBar;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;

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
                System.out.println(dataSnapshot);
//                DailyOrder dailyOrder = dataSnapshot.child("28-07-2018").child("07537004144").getValue(DailyOrder.class);
//                List<DailyOrder> dailyOrders = new ArrayList<>();
//                dailyOrders.add(dailyOrder);
//                mOrderAdapter = new OrderAdapter(MainActivity.this, R.layout.item_order, dailyOrders);
//                mOrderListView.setAdapter(mOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


//    private void attachDatabaseReadListener() {
//        if(mChildEventListener == null) {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                addData(dataSnapshot);
//            }
//
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
//
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        mOrdersDatabaseReference.addChildEventListener(mChildEventListener);
//    }
//
//}         mChildEventListener = new ChildEventListener() {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
