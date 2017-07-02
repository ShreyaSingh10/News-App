package com.example.user.newsapp;

/**
 * Created by user on 6/21/2017.
 */

import android.text.TextUtils;
import android.util.Log;

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


public class QueryUtils2 {

    public static String LOG_TAG = QueryUtils.class.getSimpleName();
    static String mainURL = "http://content.guardianapis.com/search?q=";

    public static List<News> fetchNewsData(String textEntered) {

        URL url = createURL(textEntered);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

    private static URL createURL(String textEntered) {

        URL url = null;
        String modifiedURL = textEntered+"&api-key=test";
        try {
            url = new URL(mainURL + modifiedURL);
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
            Log.e(LOG_TAG, "Problem retrieving the Books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
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

            int count = baseJsonResponse.getInt("results");
            if (count == 0) {  //if total items is 0 means no book found
                return null;
            }

            JSONArray bookArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentNews = bookArray.getJSONObject(i);
                //JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = currentNews.getString("webTitle");
                String section=currentNews.getString("sectionName");
                //JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                //String allAuthors = extractAllAuthors(authorsArray);

                News news = new News(title, section,"This is info");
                newsList.add(news);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Book JSON results", e);
        }
        return newsList;
    }

    // To extract array of authors and concatenate all author names like "- author1,author2,author3" in a single string
    /*private static String extractAllAuthors(JSONArray authorsArray) throws JSONException {

        String authorsList = null;

        if (authorsArray.length() == 0)
            authorsList = "No Author Found";

        for (int i = 0; i < authorsArray.length(); i++) {
            if (i == 0)
                authorsList = "- " + authorsArray.getString(0);
            else
                authorsList = authorsList + ", " + authorsArray.getString(i);
        }

        return authorsList;
    }*/

}

