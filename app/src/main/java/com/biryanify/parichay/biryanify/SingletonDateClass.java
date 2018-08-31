package com.biryanify.parichay.biryanify;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SingletonDateClass {

        private static SingletonDateClass sSoleInstance;
        public String hrDate = "", dbDate = "", basicDate = "";

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

    public String getBasicDate() {
        if(dbDate != "") {
            setBasicDate();
        }
        return basicDate;
    }

    private void setBasicDate() {
        SimpleDateFormat dbFormat = new SimpleDateFormat
                (
                        "yyyyMMdd",
                        Locale.US
                );
        // setting human readable format of date
        ParsePosition pos = new ParsePosition(0);
        Date originalDate = dbFormat.parse(dbDate, pos);
        SimpleDateFormat targetFormat = new SimpleDateFormat
                (
                        "dd/MM/yyyy",
                        Locale.US
                );
        basicDate = targetFormat.format(originalDate);
    }

    private void setHumanReadableDate() {
        SimpleDateFormat dbFormat = new SimpleDateFormat
                (
                        "yyyyMMdd",
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
