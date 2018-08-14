package com.biryanify.parichay.biryanify;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IndexActivity extends AppCompatActivity {

    private TextView editText;
    private Button mButton;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dbFormat;
    private SingletonDateClass instance;

    private void setDate() {

        dbFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        editText = findViewById(R.id.getDate);

        editText.setOnClickListener(v -> datePickerDialog.show());

        Calendar todayDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener listener =
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {

                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    instance.dbDate = dbFormat.format(newDate.getTime());

                    editText.setText(instance.dbDate);

                    // setting human readable format of date
                    ParsePosition pos = new ParsePosition(0);
                    Date originalDate = dbFormat.parse(instance.dbDate, pos);
                    SimpleDateFormat targetFormat = new SimpleDateFormat
                            (
                                    "EEE, MMM dd, yyyy",
                                    Locale.US
                            );
                    instance.hrDate = targetFormat.format(originalDate);
                };

        datePickerDialog = new DatePickerDialog(
                this,
                listener,
                todayDate.get(Calendar.YEAR),
                todayDate.get(Calendar.MONTH),
                todayDate.get(Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = SingletonDateClass.getInstance();

        setContentView(R.layout.activity_index);

        setDate();

        mButton = findViewById(R.id.button_check);
        mButton.setOnClickListener(v -> {
                if(instance.dbDate.length() != 0) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("SENDER_KEY", "index activity");
                    startActivity(intent);
                } else {
                    Toast.makeText(IndexActivity.this, "Choose a date", Toast.LENGTH_SHORT).show();
                }

        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        instance.dbDate = "";
        instance.hrDate = "";
        editText.setText("");
    }
}
