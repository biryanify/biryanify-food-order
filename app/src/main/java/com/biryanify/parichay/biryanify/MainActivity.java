package com.biryanify.parichay.biryanify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  static final String TAG = "MainActivity";

    public static  final String GUEST = "guest";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mOrderListView;
    private OrderAdapter mOrderAdapter;
    private ProgressBar mProgressBar;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = GUEST;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mOrderListView = (ListView) findViewById(R.id.orderListView);

        List<DailyOrder> dailyOrders = new ArrayList<>();

        DailyOrder dailyOrder = new DailyOrder();
        dailyOrder.setAddress("605, City Pride");
        dailyOrder.setAddress(" Farm Road");
        dailyOrder.setName("Parichay Barpanda");
        dailyOrder.setEmail("parichay.barpanda@gmail.com");
        dailyOrder.setMethod("Phone");
        dailyOrder.setPhone("8455071663");
        dailyOrder.setQuantity(3);
        dailyOrder.setSuggestion("_unknown_");

        dailyOrders.add(dailyOrder);

        mOrderAdapter = new OrderAdapter(this, R.layout.item_order, dailyOrders);
        mOrderListView.setAdapter(mOrderAdapter);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mOrdersDatabaseReference = mFirebaseDatabase.getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    onSignedInInitialize(user.getDisplayName());
                    Toast.makeText(MainActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
//        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {}

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                public void onCancelled(DatabaseError databaseError) {}
            };
            mOrdersDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    private void onSignedOutCleanup() {
        mUsername = GUEST;
//        detachdatabaseReadListener();
    }

    private void detachdatabaseReadListener() {
        mOrdersDatabaseReference.removeEventListener(mChildEventListener);
        mChildEventListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        //        detachdatabaseReadListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
