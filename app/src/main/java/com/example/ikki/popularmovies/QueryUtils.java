package com.example.ikki.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ikki on 10/04/2018.
 */

public class QueryUtils {

    private static final String POPULAR_MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String API = "api_key";
    private static final String API_KEY = "96c1dbfb8dd93fd10fb7f4a9892a08e7";


    public static URL buildUrl(String sortOrder) {

        Uri buildUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setConnectTimeout(7000);
            urlConnection.setReadTimeout(12000);
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

    public static PopularMovies[] parseJson(String json) throws JSONException {
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_URL = "http://image.tmdb.org/t/p/w342/";
        final String MOVIE_POSTER = "poster_path";
        final String PLOT_OVERVIEW = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String RESULTS = "results";

        String mTitle, mReleaseDate, mPoster, mPoster_path, mSynopsis;
        double mUserRating;

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

            PopularMovies[] container = new PopularMovies[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                mTitle = jsonArray.getJSONObject(i).getString(ORIGINAL_TITLE);
                mReleaseDate = jsonArray.getJSONObject(i).getString(RELEASE_DATE);
                mSynopsis = jsonArray.getJSONObject(i).getString(PLOT_OVERVIEW);
                mPoster_path = jsonArray.getJSONObject(i).getString(MOVIE_POSTER);
                mPoster = POSTER_URL + mPoster_path;
                mUserRating = jsonArray.getJSONObject(i).getDouble(USER_RATING);

                container[i] = new PopularMovies(mTitle, mReleaseDate, mSynopsis, mUserRating, mPoster);
            }

            return container;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

