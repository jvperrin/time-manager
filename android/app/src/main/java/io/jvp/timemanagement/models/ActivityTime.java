package io.jvp.timemanagement.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityTime {
    private int id;
    private float duration;
    private Date start;
    private int activityId;

    public ActivityTime(int id, float duration, String start, int activityId) {
        this.id = id;
        this.duration = duration;
        this.activityId = activityId;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            this.start = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public float getDuration() {
        return duration;
    }

    public Date getStart() {
        return start;
    }

    public int getActivityId() {
        return activityId;
    }
}