package com.example.ikki.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.PopularMoviesAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;
    private PopularMoviesAdapter mMoviesAdapter;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private String sortBy;

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPreferences.getString("sort_by", "popular");
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        new FetchMovieList().execute(sortBy);

    }





    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnClick(PopularMovies popularMovies) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("TITLE", popularMovies.getmTitle());
        intent.putExtra("POSTER", popularMovies.getmPoster());
        intent.putExtra("RATING", Double.toString(popularMovies.getmUserRating()));
        intent.putExtra("RELEASE", popularMovies.getmReleaseDate());
        intent.putExtra("SYNOPSIS", popularMovies.getmSynopsis());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            mMoviesAdapter.setPosterList(null);
            loadPoster();
            return true;
        }

        if (item.getItemId() == R.id.sort_by) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;

        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPoster();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadPoster();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(sortBy)){
            notifyAll();

        }

    }

    public class FetchMovieList extends AsyncTask<String, Void, PopularMovies[]> {

        PopularMovies[] parsedMovie;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected PopularMovies[] doInBackground(String... strings) {

            URL movieUrl = QueryUtils.buildUrl(sortBy);


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
}
