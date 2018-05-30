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
    private static final String API_KEY = "";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w342/";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String PLOT_OVERVIEW = "overview";
    private static final String USER_RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "results";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";
    private static final String NAME = "name";
    private static final String KEY = "key";


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

    public static URL buildDetailUrl(String popularMovieId, String popularMovieDetail) {
        Uri buildUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                .appendPath(popularMovieId)
                .appendPath(popularMovieDetail)
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
                mPoster = POSTER_URL + mPoster_path.substring(1);
                mUserRating = jsonArray.getJSONObject(i).getDouble(USER_RATING);

                container[i] = new PopularMovies(mTitle, mReleaseDate, mPoster, mUserRating, mSynopsis);
            }

            return container;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Trailer[] parsePopularMovieTrailerJson(String json) throws JSONException {
        Trailer[] parsedTrailer = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
            parsedTrailer = new Trailer[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trailer = jsonArray.getJSONObject(i);

                String name = trailer.getString(NAME);
                String key = trailer.getString(KEY);

                Trailer mTrailer = new Trailer(name, key);
                parsedTrailer[i] = mTrailer;
            }
            return parsedTrailer;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Review[] parsePupolarMovieReviewJson(String json) throws JSONException {
        Review[] parsedReview = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
            parsedReview = new Review[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject review = jsonArray.getJSONObject(i);

                String author = review.getString(AUTHOR);
                String content = review.getString(CONTENT);

                Review mReview = new Review(author, content);
                parsedReview[i] = mReview;
            }
            return parsedReview;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

