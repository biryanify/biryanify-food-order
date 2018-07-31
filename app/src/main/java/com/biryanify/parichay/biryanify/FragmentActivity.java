package com.biryanify.parichay.biryanify;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import javax.xml.transform.dom.DOMLocator;

public class FragmentActivity extends AppCompatActivity {

    public static Intent newInstance(Activity from, DailyOrder order, String orderCommand) {
        Intent intent = new Intent(from, FragmentActivity.class);
        intent.putExtra("order", (Serializable) order);
        intent.putExtra("extra", orderCommand);
        return intent;
    }

    public static Intent newInstance(Activity from, String orderCommand) {
        Intent intent = new Intent(from, FragmentActivity.class);
        intent.putExtra("extra", orderCommand);
        return intent;
    }


    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        fragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }
        }

        Intent intent = getIntent();
        String intentStringExtra = intent.getStringExtra("extra");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(intentStringExtra.equals("no orders")) {
            NoOrderFragment noOrderFragment = new NoOrderFragment();
            fragmentTransaction.add(R.id.fragment_container, noOrderFragment, null);
            fragmentTransaction.commit();
        }
        else if(intentStringExtra.equals("expand order")) {
            DailyOrder order = (DailyOrder) intent.getSerializableExtra("order");
            Toast.makeText(this, order.getName(), Toast.LENGTH_SHORT).show();
            ViewOrderFragment viewOrderFragment = new ViewOrderFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("order", order);
            viewOrderFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.fragment_container, viewOrderFragment, null);
            fragmentTransaction.commit();
        }
    }
}
