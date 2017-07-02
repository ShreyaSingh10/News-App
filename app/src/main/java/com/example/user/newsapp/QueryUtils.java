package com.example.user.newsapp;

/**
 * Created by user on 6/21/2017.
 */

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class QueryUtils {

    public static String LOG_TAG = QueryUtils.class.getSimpleName();
    static String mainURL = "http://content.guardianapis.com/search?api-key=test";

    public static List<News> fetchNewsData() {

        URL url = createURL();

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

    private static URL createURL() {

        URL url = null;
        try {
            url = new URL(mainURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error during creating URL ", e);
        }
        return url; //if fail then null will be returned as default is null
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000); // milliseconds
            urlConnection.setConnectTimeout(15000); // milliseconds
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);//can throw IOException. will catch the exception in catch{...} here if it occurs
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem Retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.e(LOG_TAG, jsonResponse);
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {// exception will be handled at the calling place(i.e. where call is made(); see above for call)
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFromJson(String newsJSON) {

        List<News> newsList = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        try {
            //converts JSON response which is in string to JSONObject
            JSONObject baseJsonResponse = new JSONObject(newsJSON); //this call can throw system exception exactly like new URL(); in createUrl() function above. these are reported from the compiler,
            // so i won't defer its handling by writing 'throws Exception' after the method signature. Better handle this type of exception here in the same code block like in createUrl() I have handled

            int count = baseJsonResponse.getInt("total");
            if (count == 0) {  //if total items is 0 means no news found
                return null;
            }

            JSONArray newsArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                //JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = currentNews.getString("webTitle");
                String section=currentNews.getString("sectionName");
                //JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                //String allAuthors = extractAllAuthors(authorsArray);

                News news = new News(title, section,"This is info");
                newsList.add(news);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the News JSON results", e);
        }
        return newsList;
    }

}

