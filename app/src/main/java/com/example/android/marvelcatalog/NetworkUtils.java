package com.example.android.marvelcatalog;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     *  Sample URL Format
     *  http://gateway.marvel.com/v1/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150
     */

    final static String MARVEL_COMICS_URL =
            "https://gateway.marvel.com/v1/public/characters";
    final static String MARVEL_PARAM_QUERY = "name";

    final static String MARVEL_PARAM_TIME_STAMP = "ts";
    final static String MARVEL_RESULT_TS_NUM = "1";

    final static String MARVEL_PARAM_API = "apikey";
    final static String MARVEL_PARAM_API_KEY = "";

    final static String MARVEL_PARAM_HASH = "hash";
    final static String MARVEL_PARAM_HASH_KEY = "";

    public static URL marvelBuildURL(String marvelSearchQuery) {

        Uri builtUri = Uri.parse(MARVEL_COMICS_URL).buildUpon()
                .appendQueryParameter(MARVEL_PARAM_QUERY, marvelSearchQuery)
                .appendQueryParameter(MARVEL_PARAM_TIME_STAMP, MARVEL_RESULT_TS_NUM)
                .appendQueryParameter(MARVEL_PARAM_API, MARVEL_PARAM_API_KEY)
                .appendQueryParameter(MARVEL_PARAM_HASH, MARVEL_PARAM_HASH_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        Log.v(TAG, "the url is: " + url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}