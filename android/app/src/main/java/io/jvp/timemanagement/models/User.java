package io.jvp.timemanagement.models;

public class User {
    private int id;
    private String email;
    private String apiKey;

    public User(int id, String email, String apiKey) {
        this.id = id;
        this.email = email;
        this.apiKey = apiKey;
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
}
