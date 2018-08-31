package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

public class FragmentActivity extends AppCompatActivity implements onModifyOrder {

    public static Intent newInstance(Activity from, String orderCommand) {
        Intent intent = new Intent(from, FragmentActivity.class);
        intent.putExtra("extra", orderCommand);
        return intent;
    }
    public static Intent newInstance(Activity from, String orderCommand, DailyOrder order) {
        Intent intent = new Intent(from, FragmentActivity.class);
        intent.putExtra("extra", orderCommand);
        intent.putExtra("order", (Parcelable)order);
        return intent;
    }

    public void modifyOrder(DailyOrder dailyOrder, String dbDate) {
//        Log.d("FragmentActivity", "Got this date "+mDbDate);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("order", (Parcelable) dailyOrder);
        returnIntent.putExtra("date", dbDate);
        returnIntent.putExtra("SENDER_KEY", "Fragment Activity");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container_modify) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

        TextView dateTextView = findViewById(R.id.date_textview);

        Intent intent = getIntent();
        String intentStringExtra = intent.getStringExtra("extra");
        dateTextView.setText(SingletonDateClass.getInstance().hrDate);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (intentStringExtra.equals("add order")) {
            fragmentTransaction.
                    add(
                            R.id.fragment_container_modify,
                            AddOrderFragment.newInstance(),
                            null
                    );
            fragmentTransaction.commit();
        } else if(intentStringExtra.equals("edit order")) {
            fragmentTransaction.
                    add(
                            R.id.fragment_container_modify,
                            EditOrderFragment.newInstance(intent.getParcelableExtra("order")),
                            null
                    );
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

}
