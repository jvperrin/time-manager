package io.jvp.timemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.larswerkman.holocolorpicker.ColorPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.jvp.timemanagement.models.Activity;

public class MainActivity extends ActionBarActivity {
    public Chronometer chronometer;
    public EditText newActivityField;
    public ArrayList<Activity> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("session", MODE_PRIVATE);
        long currentTime = settings.getLong("currentTime", 0);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        if (currentTime > 0) {
            chronometer.setBase(currentTime);
            chronometer.start();
        }

        int backgroundColor = settings.getInt("currentActivityColor", Color.WHITE);
        getWindow().getDecorView().getRootView().setBackgroundColor(backgroundColor);

        String currentName = settings.getString("currentActivityName", "");
        TextView currentActivityView = (TextView) findViewById(R.id.current_activity_name);
        EditText newActivityView = (EditText) findViewById(R.id.new_activity);

        currentActivityView.setText(currentName);
        int textColor;
        if ((Color.red(backgroundColor) * 0.299 + Color.green(backgroundColor) * 0.587 + Color.blue(backgroundColor) * 0.114) > 186) {
            textColor = Color.BLACK;
        } else {
            textColor = Color.WHITE;
        }
        newActivityField = newActivityView;
        currentActivityView.setTextColor(textColor);
        newActivityView.setTextColor(textColor);
        newActivityView.setHintTextColor(textColor);
        chronometer.setTextColor(textColor);


        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);
        final MainActivity mainActivity = this;

        BackgroundAPIRequest.activitiesRequest(
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONArray activitiesJson = (JSONArray) msg.obj;
                    try {
                        activities = new ArrayList<Activity>(activitiesJson.length());
                        for(int i = 0; i < activitiesJson.length(); i++) {
                            JSONObject activity = activitiesJson.getJSONObject(i);
                            final Activity currentActivity = new Activity(activity.getInt("id"), activity.getString("name"), activity.getString("color"));
                            activities.add(i, currentActivity);

                            Button btn = currentActivity.createButton(mainActivity, i);

                            layout.addView(btn);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    VolleyError error = (VolleyError) msg.obj;

                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null) {
                        switch(response.statusCode) {
                            case 401:
                            case 500:
                                String jsonString;
                                try {
                                    jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                    Toast.makeText(mainActivity, jsonString, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        SharedPreferences settings = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("apiKey");
        editor.apply();

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void create_activity(View view) {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);
        final MainActivity mainActivity = this;
        final EditText newActivityText = (EditText) findViewById(R.id.new_activity);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.activity_color_picker);

        BackgroundAPIRequest.createActivityRequest(
            new Activity(
                0, newActivityText.getText().toString(), picker.getColor()
            ),
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject activityJson = (JSONObject) msg.obj;

                    try {
                        Activity activity = new Activity(activityJson.getInt("id"), activityJson.getString("name"), activityJson.getString("color"));
                        Button btn = activity.createButton(mainActivity, activities.size());
                        activities.add(activity);
                        layout.addView(btn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Handler() {
                @Override
                public void handleMessage(Message msg) {
//                    VolleyError error = (VolleyError) msg.obj;

//                    NetworkResponse response = error.networkResponse;
                }
            }
        );
    }
}
