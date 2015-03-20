package io.jvp.timemanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
//    public final static String EXTRA_MESSAGE = "io.jvp.timemanagement.MESSAGE";
//    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        View root = getWindow().getDecorView().getRootView();
//        root.setBackgroundColor(Color.parseColor("#000000"));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        SharedPreferences settings = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("apiKey");
        editor.apply();
    }
}
