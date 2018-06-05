package com.example.ikki.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public class PopularMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.ikki.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "popularmovies";

    public static final class PopularMoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);



    }
}
