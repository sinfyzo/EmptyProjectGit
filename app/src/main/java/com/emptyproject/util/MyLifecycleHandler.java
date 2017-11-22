package com.emptyproject.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by chirag on 7/7/17.
 */

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
   /* private int resumed;
    private int paused;
    private int started;
    private int stopped;*/
    private boolean isBackGround = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        android.util.Log.w("test", "application is visible: Hello ");
//        android.util.Log.w("test", "application is visible: " + (started > stopped));

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        isBackGround = false;
        /*if (AppController.getInstance(activity).getSocket() != null) {
            if (!AppController.getInstance(activity).getSocket().connected()) {
                AppController.getInstance(activity).connectSocket();
            }
        }*/
//        ++resumed;
//        android.util.Log.w("test", "application is visible: Hello 123 ");
//        android.util.Log.w("onActivityResumed", "application is visible: " + isBackGround);
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        ++paused;
        isBackGround = true;
//        android.util.Log.w("onActivityPaused", "application is in foreground: " + isBackGround);

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        ++started;
    }


    @Override
    public void onActivityStopped(Activity activity) {
//        ++stopped;
//        android.util.Log.w("test", "application is visible: " + (started > stopped));
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    /*
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }
    */

}
