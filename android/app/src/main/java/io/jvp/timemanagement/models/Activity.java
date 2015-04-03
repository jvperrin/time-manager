package io.jvp.timemanagement.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.jvp.timemanagement.BackgroundAPIRequest;
import io.jvp.timemanagement.MainActivity;
import io.jvp.timemanagement.R;


public class Activity {
    private int id;
    private String name;
    private int color;

    public Activity(int id, String name, String colorString) {
        this.id = id;
        this.name = name;
        this.color = Color.parseColor(colorString);
    }

    public Activity(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getColorString() { return String.format("#%06X", 0xFFFFFF & color); }

    public String toString() {
        return "Activity: <id:" + id + " name:" + name + " color:" + color + ">";
    }

    public Button createButton(final MainActivity activity, int index) {
        Button btn = new Button(activity);
        btn.setId(2000 + index);
        btn.setText(name);
        btn.setBackgroundColor(color);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        params.addRule(RelativeLayout.BELOW, 1999 + index); // Set button to be below the previously created button.

        params.setMargins(10, 5, 0, 5);
        btn.setLayoutParams(params);

        // Select either black or white font color for the button to contrast
        //   with the dynamic background color.
        // http://stackoverflow.com/questions/3942878/how-to-decide-font-color-in-white-or-black-depending-on-background-color
        // http://stackoverflow.com/questions/596216/formula-to-determine-brightness-of-rgb-color
        final int textColor;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        if (Math.sqrt((r*r * 0.299 + g*g * 0.587 + b*b * 0.114)) > 186) {
            textColor = Color.BLACK;
        } else {
            textColor = Color.WHITE;
        }
        btn.setTextColor(textColor);

        final Activity currentActivity = this;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getWindow().getDecorView().getRootView().setBackgroundColor(color);

                TextView currentActivityView = (TextView) activity.findViewById(R.id.current_activity_name);
                currentActivityView.setTextColor(textColor);
                currentActivityView.setText(name);

                SharedPreferences settings = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("currentTime", SystemClock.elapsedRealtime());
                editor.putInt("currentActivityColor", color);
                editor.putString("currentActivityName", name);
                editor.apply();

                activity.chronometer.stop();
                activity.chronometer.setTextColor(textColor);
                activity.chronometer.setBase(SystemClock.elapsedRealtime());
                activity.chronometer.start();

                activity.newActivityField.setTextColor(textColor);
                activity.newActivityField.setHintTextColor(textColor);

                BackgroundAPIRequest.setCurrentActivityRequest(currentActivity,
                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            JSONObject response = (JSONObject) msg.obj;
                            try {
                                Toast.makeText(activity, response.getString("success"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            VolleyError error = (VolleyError) msg.obj;

                            NetworkResponse response = error.networkResponse;
                            JSONObject json;
                            if(response != null && response.data != null) {
                                switch(response.statusCode) {
                                    case 401:
                                    case 500:
                                        String jsonString = null;
                                        try {
                                            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                            Log.d("CurrentActivityError", jsonString);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                            }
                        }
                    }
                );
            }
        });

        return btn;
    }
}
