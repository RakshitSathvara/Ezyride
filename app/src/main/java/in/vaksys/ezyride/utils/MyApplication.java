package in.vaksys.ezyride.utils;

import android.app.Application;

/**
 * Created by Harsh on 18-01-2016.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
    // common in volley singleton and analytics
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}