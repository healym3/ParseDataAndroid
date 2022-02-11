package com.example.parsedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String apiUrl = "https://jsonplaceholder.typicode.com/todos";
    String getApiUrl ="https://jsonplaceholder.typicode.com/todos/1";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response ->  {
//            Log.d("Main", "onCreate: " + response.substring(0,500));
//
//                }, error ->  {
//            Log.d("Main", "Failed to get info!");
//        }
//       );
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest();

        JsonArrayRequest jsonArrayRequest = getJsonArrayRequest(textView);

        queue.add(jsonObjectRequest);
// Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        queue.add(jsonArrayRequest);
        //getString(textView, queue);
    }

    @NonNull
    private JsonArrayRequest getJsonArrayRequest(TextView textView) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            textView.setText(textView.getText() + title + "\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //textView.setText("Response: " + response.toString())
                },
                error -> {
                    // TODO: Handle error
                    Log.d("JSON", "onCreate: Failed");
                });
        return jsonArrayRequest;
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getApiUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("jsonObj", "onCreate: " + response.getString("title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        return jsonObjectRequest;
    }

    private void getString(TextView textView, RequestQueue queue) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}