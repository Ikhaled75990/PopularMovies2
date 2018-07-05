package com.example.ikki.popularmovies;

public class PopularMoviePreferences {

    private static final String MOST_POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVOURITES = "favourites";
    private static String mSortPreference = MOST_POPULAR;


    public static String getPopularMoviePreference() {
        return mSortPreference;
    }

    public static void sortMostPopular(){
        mSortPreference = MOST_POPULAR;
    }

    public static void sortTopRated(){
        mSortPreference = TOP_RATED;
    }

    public static void sortFavourites(){
        mSortPreference = FAVOURITES;
    }
}
