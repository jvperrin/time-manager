package io.jvp.timemanagement;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class TimeManagement extends Application {
    public static RequestQueue requestQueue;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);
    }
}
