package io.jvp.timemanagement.models;

public class User {
    private int id;
    private String email;
    private String apiKey;
    private int currentActivity;

    public User(int id, String email, String apiKey, int currentActivity) {
        this.id = id;
        this.email = email;
        this.apiKey = apiKey;
        this.currentActivity = currentActivity;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getCurrentActivity() {
        return currentActivity;
    }
}
