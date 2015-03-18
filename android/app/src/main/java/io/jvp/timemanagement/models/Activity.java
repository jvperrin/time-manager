package io.jvp.timemanagement.models;

import android.graphics.Color;

public class Activity {
    private int id;
    private String name;
    private int color;

    public Activity(int id, String name, String colorString) {
        this.id = id;
        this.name = name;
        this.color = Color.parseColor(colorString);
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
}
