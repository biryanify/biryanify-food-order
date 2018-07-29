package com.biryanify.parichay.biryanify;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IndexActivity extends AppCompatActivity {

    private EditText editText;
    private Button mButton;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        editText = findViewById(R.id.getDate);
        editText.setInputType(InputType.TYPE_NULL);
        editText.requestFocus();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               datePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mButton = findViewById(R.id.button_check);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = editText.getText().toString();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

//    public void checkOrder(View view) {
//        System.out.print("exec");
//        editText = findViewById(R.id.getDate);
//        String date = editText.getText().toString();
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("date", date);
//        startActivity(intent);
//    }
}
