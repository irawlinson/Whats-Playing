package com.example.isaiahrawlinson.whatsplaying;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * Created by isaiahrawlinson on 5/5/17.
 */

public class JSONGracenote {
    private static final String LOG_TAG = "JSONGracenote";
    private String API_KEY = "ehazbrzyrqeg5zfpx93jfdkt";
    private String GracenoteTestUrl = "http://data.tmsapi.com/v1.1/movies/showings?startDate=2017-05-06&zip=02135&api_key=" + API_KEY;

    public static JSONArray getJSONfromURL(String url) {
        InputStream inputStream = null;
        String result = "";
        JSONArray jArray = null;
        HttpURLConnection con = null;

        //Download JSON data from URL
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpPost = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in http connection " + e.toString());
        }
        //Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            inputStream.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error converting result " + e.toString());
        }

        try {
            jArray = new JSONArray(result);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing data " + e.getMessage());
        }
        return jArray;
    }
}
