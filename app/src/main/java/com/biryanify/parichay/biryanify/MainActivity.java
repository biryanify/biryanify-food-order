package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.widget.TextView;


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
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity {

    

    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private SimpleDateFormat originalFormat, targetFormat;

    List<DailyOrder> dailyOrders = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.date_textview);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setItemAnimator(new SlideInUpAnimator());

        mRecyclerView.addOnItemTouchListener(new MyTouchListener(this, mRecyclerView,
                new MyTouchListener.OnTouchActionListener() {
                    @Override
                    public void onLeftSwipe(View view, int position) {

                    }

                    @Override
                    public void onRightSwipe(View view, int position) {

                    }

                    @Override
                    public void onClick(View view, int position) {
                        startActivity(
                                FragmentActivity.newInstance(
                                        MainActivity.this,
                                        dailyOrders.get(position),
                                        "expand order")
                        );
                    }
                }));

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        Intent intent2 = getIntent();
        String date = intent2.getStringExtra("date");

        originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        ParsePosition pos = new ParsePosition(0);
        Date date1 = originalFormat.parse(date, pos);
        targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);
        String formattedDate = targetFormat.format(date1);

        mTextView.setText(formattedDate);

        mOrdersDatabaseReference.child("orders").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        DailyOrder dailyOrder = orderSnapshot.getValue(DailyOrder.class);
                        dailyOrders.add(dailyOrder);
                    }
                    OrderAdapter adapter = new OrderAdapter(dailyOrders);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } else {
                    startActivity(
                            FragmentActivity.newInstance(
                                MainActivity.this,
                                "no orders")
                    );
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
