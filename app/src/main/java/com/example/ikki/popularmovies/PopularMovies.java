package com.example.ikki.popularmovies;

import android.graphics.Movie;

/**
 * Created by Ikki on 18/03/2018.
 */

public class PopularMovies {

    private int mId;
    private String mTitle;
    private String mReleaseDate;
    private String mPoster;
    private double mUserRating;
    private String mSynopsis;

    public PopularMovies(int id, String title, String releaseDate, String poster, double userRating, String synopsis) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mPoster = poster;
        mUserRating = userRating;
        mSynopsis = synopsis;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        this.mId = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        this.mTitle = title;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String poster) {
        this.mPoster = poster;
    }

    public double getmUserRating() {
        return mUserRating;
    }

    public void setmUserRating(double userRating) {
        this.mUserRating = userRating;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public void setmSynopsis(String synopsis) {
        this.mSynopsis = synopsis;
    }


}
