package io.jvp.timemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import io.jvp.timemanagement.models.Activity;

public class BackgroundAPIRequest {
    private static RequestQueue requestQueue = TimeManagement.requestQueue;
    private static Context context = TimeManagement.context;
    private static String baseUrl = "https://tm-app.jvp.io/api/v1";

    public static <T> void addToRequestQueue(Request<T> req) {
        req.setTag("VolleyRequest");
        requestQueue.add(req);
    }

    public static void loginRequest(String email, String password, final Handler successCallback,
                                    final Handler errorCallback) {
        String url = baseUrl + "/signin";

        JSONObject jsonLogin = new JSONObject();
        try {
            jsonLogin.put("email", email);
            jsonLogin.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginReq = objectRequest(Request.Method.POST, url, jsonLogin, successCallback, errorCallback);

        addToRequestQueue(loginReq);
    }

    public static void activitiesRequest(final Handler successCallback, final Handler errorCallback) {
        String url = baseUrl +  "/activities";

        JsonArrayRequest activityReq = arrayRequest(Request.Method.GET, url, null, successCallback, errorCallback);

        addToRequestQueue(activityReq);
    }

    public static void createActivityRequest(Activity activity, final Handler successCallback, final Handler errorCallback) {
        String url = baseUrl + "/activities";

        JSONObject activityJson = new JSONObject();
        try {
            JSONObject activityValues = new JSONObject();
            activityValues.put("name", activity.getName());
            activityValues.put("color", activity.getColorString());

            activityJson.put("activity", activityValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activityReq = objectRequest(Request.Method.POST, url, activityJson, successCallback, errorCallback);

        addToRequestQueue(activityReq);
    }

    public static void setCurrentActivityRequest(Activity activity, final Handler successCallback, final Handler errorCallback) {
        String url = baseUrl + "/set-current-activity";

        JSONObject currentActivityJson = new JSONObject();
        try {
            currentActivityJson.put("id", activity.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest activityReq = objectRequest(Request.Method.POST, url, currentActivityJson, successCallback, errorCallback);

        addToRequestQueue(activityReq);
    }



    private static JsonObjectRequest objectRequest(final int method, String url, JSONObject jsonData,
                                                   final Handler successCallback, final Handler errorCallback) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, jsonData,
            new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("VolleyResponse", response.toString());

                    Message successMessage = new Message();
                    successMessage.obj = response;
                    successCallback.handleMessage(successMessage);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    StringWriter sw = new StringWriter();
                    error.printStackTrace(new PrintWriter(sw));

                    Log.d("VolleyError", "Error: " + error.getMessage());
                    Log.d("VolleyStackTrace", sw.toString());

                    Message errorMessage = new Message();
                    errorMessage.obj = error;
                    errorCallback.handleMessage(errorMessage);
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  headers = new HashMap<>();

                SharedPreferences settings = context.getSharedPreferences("session", Context.MODE_PRIVATE);

                if (!settings.getString("apiKey", "").equals("")) {
                    headers.put("Authorization", "Token " + settings.getString("apiKey", ""));
                }
                if (method == Request.Method.POST) {
                    headers.put("Content-Type", "application/json; charset=utf-8");
                }

                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(
            new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        );

        return jsonObjReq;
    }


    private static JsonArrayRequest arrayRequest(final int method, String url, JSONArray jsonData,
                                                 final Handler successCallback, final Handler errorCallback) {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(method, url, jsonData,
            new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    Log.d("VolleyResponse", response.toString());

                    Message successMessage = new Message();
                    successMessage.obj = response;
                    successCallback.handleMessage(successMessage);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    StringWriter sw = new StringWriter();
                    error.printStackTrace(new PrintWriter(sw));

                    Log.d("VolleyError", "Error: " + error.getMessage());
                    Log.d("VolleyStackTrace", sw.toString());

                    Message errorMessage = new Message();
                    errorMessage.obj = error;
                    errorCallback.handleMessage(errorMessage);
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  headers = new HashMap<>();

                SharedPreferences settings = context.getSharedPreferences("session", Context.MODE_PRIVATE);

                if (!settings.getString("apiKey", "").equals("")) {
                    headers.put("Authorization", "Token " + settings.getString("apiKey", ""));
                }
                if (method == Request.Method.POST) {
                    headers.put("Content-Type", "application/json; charset=utf-8");
                }

                return headers;
            }
        };

        jsonArrReq.setRetryPolicy(
            new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        );

        return jsonArrReq;
    }
}