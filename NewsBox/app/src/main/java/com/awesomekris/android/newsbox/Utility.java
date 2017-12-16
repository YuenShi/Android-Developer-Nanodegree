package com.awesomekris.android.newsbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kris on 16/10/3.
 */
public class Utility {

    private static String LOG_TAG = Utility.class.getSimpleName();

    public static String getStartDate(long dateInMillis) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date(dateInMillis);
        String currentDate = dateFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        calendar.add(Calendar.DAY_OF_YEAR, -3);

        Date startDate = calendar.getTime();
        String start = dateFormat.format(startDate);

        return start;
    }

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }


}
