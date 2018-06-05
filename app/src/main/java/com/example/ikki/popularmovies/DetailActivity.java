package com.example.ikki.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ikki.popularmovies.data.PopularMoviesContract;
import com.example.ikki.popularmovies.data.PopularMoviesDbHelper;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Ikki on 10/04/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTv;
    private ImageView mPosterIv;
    private TextView mUserRatingTv;
    private TextView mReleaseDateTv;
    private TextView mSynopsisTv;
    private TextView mTrailerTv;
    private CheckBox mCheckBoxFavourite;
    private ListView mTrailerLv;
    private LinearLayout mReviewView;
    private boolean mFavourite;

    TrailerAdapter mTrailerAdapter;
    Trailer[] mTrailer;
    ReviewAdapter mReviewAdapter;
    Review[] mReview;

    String mPopularMovieId;
    String title;
    String userRating;
    String releaseDate;
    String synopsis;
    String poster;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mTitleTv = findViewById(R.id.movie_title_tv);
        mPosterIv = findViewById(R.id.movie_poster_iv);
        mUserRatingTv = findViewById(R.id.movie_rating_tv);
        mReleaseDateTv = findViewById(R.id.movie_date_tv);
        mSynopsisTv = findViewById(R.id.movie_synopsis_tv);
        mCheckBoxFavourite = findViewById(R.id.checkbox_favourite);
        mTrailerTv = findViewById(R.id.trailer_tv);
        mTrailerLv = findViewById(R.id.trailer_lv);
        mReviewView = findViewById(R.id.reviews_tv);


        Intent intent = getIntent();

        title = intent.getStringExtra("TITLE");
        userRating = intent.getStringExtra("RATING");
        releaseDate = intent.getStringExtra("RELEASE");
        synopsis = intent.getStringExtra("SYNOPSIS");
        poster = intent.getStringExtra("POSTER");
        int id = intent.getIntExtra("Id", 0);
        mPopularMovieId = String.valueOf(id);

        if (intent.hasExtra("TITLE"))
            mTitleTv.setText(title);
        if (intent.hasExtra("RATING"))
            mUserRatingTv.setText(userRating);
        if (intent.hasExtra("RELEASE"))
            mReleaseDateTv.setText(releaseDate);
        if (intent.hasExtra("SYNOPSIS"))
            mSynopsisTv.setText(synopsis);
        if (intent.hasExtra("POSTER"))
            Picasso.with(this).load(poster).into(mPosterIv);

        mFavourite = searchPopularMovie(mPopularMovieId);
        mCheckBoxFavourite.setChecked(mFavourite);

        if (PreferenceManager.getDefaultSharedPreferences(this).equals("favourites")){
            mTrailerLv.setVisibility(View.GONE);
            mReviewView.setVisibility(View.GONE);
        } else{
            new loadPopularMovieTrailer().execute(mPopularMovieId, "videos");
            new loadPopularMovieReview().execute(mPopularMovieId, "reviews");
        }


    }

    public void onCheckBoxClicked(View view) {

        if (mFavourite){
            deletePopularMovie();
            mCheckBoxFavourite.setChecked(false);
            if (PreferenceManager.getDefaultSharedPreferences(this).equals("favourites")){
                getContentResolver().notifyChange(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null);

            }
        } else {
            insertPopularMovie();
            mCheckBoxFavourite.setChecked(true);
        }

    }

    public void insertPopularMovie() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID, mPopularMovieId);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE, title);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_IMAGE, poster);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RATING, userRating);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_SYNOPSIS, synopsis);

        Uri latestUri = getContentResolver().insert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, contentValues);
        getContentResolver().notifyChange(latestUri, null);
    }

    public void deletePopularMovie() {
        PopularMoviesDbHelper moviesDbHelper = new PopularMoviesDbHelper(this);
        SQLiteDatabase sqLiteDatabase = moviesDbHelper.getWritableDatabase();
        String selection = PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID + " =?";
        String[] selectionArgs = {mPopularMovieId};
        sqLiteDatabase.delete(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
    }

    private boolean searchPopularMovie(String id) {
        PopularMoviesDbHelper moviesDbHelper = new PopularMoviesDbHelper(this);
        SQLiteDatabase sqLiteDatabase = moviesDbHelper.getReadableDatabase();
        String[] projection = {PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID};
        String selection = PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID + " =?";
        String[] selectionArgs = {id};
        Cursor cursor = sqLiteDatabase.query(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        try {
            if (cursor.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        } finally {
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.favourites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                //User's action will mark the movie as favourite and add it to
                //database.

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public class loadPopularMovieTrailer extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {
            String popularMovieId = params[0];
            String popularMovieDetail = params[1];

            URL popularMovieRequestUrl = QueryUtils.buildDetailUrl(popularMovieId, popularMovieDetail);

            try {
                String jsonResponse = QueryUtils.getResponseFromHttpUrl(popularMovieRequestUrl);
                Trailer[] popularMovieTrailerInfo = QueryUtils.parsePopularMovieTrailerJson(jsonResponse);

                return popularMovieTrailerInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] popularMovieTrailer) {
            mTrailer = popularMovieTrailer;
            mTrailerAdapter = new TrailerAdapter(DetailActivity.this, mTrailer);
            mTrailerLv.setAdapter(mTrailerAdapter);
            mTrailerLv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Trailer trailer = mTrailer[i];
                            String key = trailer.getmKey();

                            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));

                            try {
                                startActivity(appIntent);
                            } catch (ActivityNotFoundException e) {
                                startActivity(browserIntent);
                            }

                        }
                    }
            );

        }
    }

    public class loadPopularMovieReview extends AsyncTask<String, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(String... params) {
            String popularMovieId = params[0];
            String popularMovieDetail = params[1];

            URL popularMovieRequest = QueryUtils.buildDetailUrl(popularMovieId, popularMovieDetail);

            try {
                String jsonResponse = QueryUtils.getResponseFromHttpUrl(popularMovieRequest);

                Review[] popularMovieReviewInfo = QueryUtils.parsePopularMoiveReviewJson(jsonResponse);

                return popularMovieReviewInfo;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Review[] popularMovieReviewInfo) {
            mReview = popularMovieReviewInfo;
            mReviewAdapter = new ReviewAdapter(DetailActivity.this, mReview);
            final int view = mReviewAdapter.getCount();

            for (int i = 0; i < view; i++){
                View views = mReviewAdapter.getView(i, null, null);
                mReviewView.addView(views);
            }
        }
    }
}
