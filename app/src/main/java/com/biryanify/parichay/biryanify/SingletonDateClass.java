package com.biryanify.parichay.biryanify;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SingletonDateClass {

        private static SingletonDateClass sSoleInstance;
        public String hrDate = "", dbDate = "";

        //private constructor.
        private SingletonDateClass(){
            //Prevent form the reflection api.
            if (sSoleInstance != null){
                throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
            }
        }

        public static SingletonDateClass getInstance(){
            if (sSoleInstance == null){
                //if there is no instance available... create new one
                sSoleInstance = new SingletonDateClass();
            }

            return sSoleInstance;
        }

    public String getHrDate() {
        if(dbDate != "") {
            setHumanReadableDate();
        }
        return hrDate;
    }

    private void setHumanReadableDate() {
        SimpleDateFormat dbFormat = new SimpleDateFormat
                (
                        "dd-MM-yyyy",
                        Locale.US
                );
        // setting human readable format of date
        ParsePosition pos = new ParsePosition(0);
        Date originalDate = dbFormat.parse(dbDate, pos);
        SimpleDateFormat targetFormat = new SimpleDateFormat
                (
                        "EEE, MMM dd, yyyy",
                        Locale.US
                );
        hrDate = targetFormat.format(originalDate);
    }
}
