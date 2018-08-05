package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.BiConsumer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FragmentActivity extends AppCompatActivity implements FragmentToActivity {
    private TextView dateTextView;


    public static Intent newInstance(Activity from, String orderCommand, String date) {
        Intent intent = new Intent(from, FragmentActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("extra", orderCommand);

        FragmentToActivity fragmentToActivity = new FragmentToActivity() {
            @Override
            public void communicate(DailyOrder dailyOrder) {

            }
        };

        return intent;
    }

    public void communicate(DailyOrder dailyOrder) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("order", (Parcelable) dailyOrder);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }



    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container1) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

        dateTextView = findViewById(R.id.date_textview);

        Intent intent = getIntent();
        String intentStringExtra = intent.getStringExtra("extra");
        String date = intent.getStringExtra("date");
        dateTextView.setText(date);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (intentStringExtra.equals("add order")) {
            fragmentTransaction.
                    add(
                            R.id.fragment_container1,
                            AddOrderFragment.newInstance(date),
                            null
                    );
            fragmentTransaction.commit();
        }
    }
}
