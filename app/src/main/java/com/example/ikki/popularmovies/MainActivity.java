package com.example.ikki.popularmovies;

import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ikki.popularmovies.data.PopularMoviesContract;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.PopularMoviesAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private PopularMoviesAdapter mMoviesAdapter;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private static final int POPULARMOVIE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.movies_recyclerview);
        mProgressBar = findViewById(R.id.progressBar);
        mErrorMessage = findViewById(R.id.error_message);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new PopularMoviesAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMoviesAdapter);
        loadPoster();
    }

    private void loadPoster() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        String moviePreference = PopularMoviePreferences.getPopularMoviePreference();

        if (moviePreference.equals("favourites")){
            getSupportLoaderManager().restartLoader(POPULARMOVIE_LOADER, null, this);
        } else {
        new FetchMovieList().execute(moviePreference);
    }}


    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    @Override
    public void OnClick(PopularMovies popularMovies) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        int popularMovieId = popularMovies.getmId();
        intent.putExtra("Id", popularMovieId);
        intent.putExtra("TITLE", popularMovies.getmTitle());
        intent.putExtra("POSTER", popularMovies.getmPoster());
        intent.putExtra("RATING", Double.toString(popularMovies.getmUserRating()));
        intent.putExtra("RELEASE", popularMovies.getmReleaseDate());
        intent.putExtra("SYNOPSIS", popularMovies.getmSynopsis());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sort_popular:
                PopularMoviePreferences.sortMostPopular();
                loadPoster();
                setTitle(R.string.sort_default);
                return true;
            case R.id.sort_rating:
                PopularMoviePreferences.sortTopRated();
                loadPoster();
                setTitle(R.string.top_rated);
                return true;
            case R.id.sort_favourites:
                PopularMoviePreferences.sortFavourites();
                getSupportLoaderManager().initLoader(POPULARMOVIE_LOADER, null, this);
                setTitle(R.string.favourites);
        }

        return super.onOptionsItemSelected(item);


    }

    public class FetchMovieList extends AsyncTask<String, Void, PopularMovies[]> {

        PopularMovies[] parsedMovie;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected PopularMovies[] doInBackground(String... strings) {
            String preference = strings[0];

            URL movieUrl = QueryUtils.buildUrl(preference);


            try {
                String jsonMovieResponse = QueryUtils.getResponseFromHttpUrl(movieUrl);
                parsedMovie = QueryUtils.parseJson(jsonMovieResponse);
                return parsedMovie;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PopularMovies[] popularMovies) {
            mProgressBar.setVisibility(View.INVISIBLE);

            if (popularMovies == null) {
                showErrorMessage();
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mErrorMessage.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mMoviesAdapter.setPosterList(popularMovies);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID,
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE,
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_IMAGE,
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RATING,
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
                PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_SYNOPSIS
        };

        return new CursorLoader(this, PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, projection, null, null, PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            mRecyclerView.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
        } else {
            PopularMovies[] popularMoviesData = CursorConverter(data);
            mMoviesAdapter.setPosterList(popularMoviesData);
        }

        getSupportLoaderManager().destroyLoader(MainActivity.POPULARMOVIE_LOADER);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.setPosterList(null);

    }

    private PopularMovies[] CursorConverter(Cursor data) {
        PopularMovies[] popularMoviesData = new PopularMovies[data.getCount()];
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            int popularMovieId = data.getInt(data.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID));
            String popularMovieName = data.getString(data.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE));
            String popularMovieImage = data.getString(data.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_IMAGE));
            double popularMovieRating = data.getDouble(data.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RATING));
            String popularMoviesDate = data.getString(data.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_RELEASE_DATE));
            String popularMovieSynopsis = data.getString(data.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_SYNOPSIS));

            PopularMovies mPopularMovie = new PopularMovies(popularMovieId,
                    popularMovieName,
                    popularMoviesDate,
                    popularMovieImage,
                    popularMovieRating,
                    popularMovieSynopsis);

            popularMoviesData[i] = mPopularMovie;
        }

        return popularMoviesData;
    }


}
