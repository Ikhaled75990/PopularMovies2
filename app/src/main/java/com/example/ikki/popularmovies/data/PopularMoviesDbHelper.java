package com.example.ikki.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ikki.popularmovies.PopularMovies;

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "popularmovies.db";


    private static final String SQL_CREATE_DATABASE =
            "CREATE TABLE " + PopularMoviesContract.PopularMoviesEntry.TABLE_NAME + " (" +
                    PopularMoviesContract.PopularMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_IMAGE + " TEXT NOT NULL, " +
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL);";

    private static final String SQL_DELETE_DATABASE =
            "DROP TABLE IF EXISTS " + PopularMoviesContract.PopularMoviesEntry.TABLE_NAME;



    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);

    }
}
