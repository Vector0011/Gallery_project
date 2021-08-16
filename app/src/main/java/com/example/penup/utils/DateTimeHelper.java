package com.example.penup.utils;

import android.text.format.DateFormat;

import org.joda.time.DateTimeComparator;

import java.util.Date;

public class DateTimeHelper {
    private static DateTimeHelper INSTANCE = null;
    private DateTimeHelper(){};
    public static synchronized DateTimeHelper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DateTimeHelper();
        }
        return INSTANCE;
    }
    public int compareDateWithoutTime(Date d1, Date d2){
        DateTimeComparator compareDate = DateTimeComparator.getDateOnlyInstance();
        return compareDate.compare(d1,d2);
    }
    public String getStringWithoutTime(Date d){
        return (String) DateFormat.format("dd/MM/yyyy",d);
    }
    public String getDuration(long duration){
        int second = (int)duration/1000;
        int minute = second/60;
        second = second%60;
        return minute+":"+second;
    }
    public String getDateTime(long l){
        Date d = new Date(l);
        return (String) DateFormat.format("dd/MM/yyyy | hh:mm",d);
    }
}
