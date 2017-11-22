package com.emptyproject.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.datecoy.chat.ChatActivity;

/**
 * Created by chirag on 2/5/17.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;
    public ConnectivityReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            boolean isVisible = AppController.isActivityVisible();// Check if
            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();
                // Check internet connection and accrding to state change the
                // text of activity by calling method
                boolean isConnected = networkInfo != null
                        && networkInfo.isConnectedOrConnecting();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Log.e("InternetConnector ", "Internet on");
                    ChatActivity.updateNotificationData(true);
                } else {
                    Log.e("InternetConnector ", "Internet off");
                    ChatActivity.updateNotificationData(false);
                }
                if (connectivityReceiverListener != null) {
                    connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) AppController.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}