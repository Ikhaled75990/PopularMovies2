package com.example.ikki.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ikki on 10/04/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTv;
    private ImageView mPosterIv;
    private TextView mUserRatingTv;
    private TextView mReleaseDateTv;
    private TextView mSynopsisTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        mTitleTv = findViewById(R.id.movie_title_tv);
        mPosterIv = findViewById(R.id.movie_poster_iv);
        mUserRatingTv = findViewById(R.id.movie_rating_tv);
        mReleaseDateTv = findViewById(R.id.movie_date_tv);
        mSynopsisTv = findViewById(R.id.movie_synopsis_tv);


        Intent intent = getIntent();

        if (intent.hasExtra("TITLE"))
            mTitleTv.setText(intent.getStringExtra("TITLE"));
        if (intent.hasExtra("RATING"))
            mUserRatingTv.setText(intent.getStringExtra("RATING"));
        if (intent.hasExtra("RELEASE"))
            mReleaseDateTv.setText(intent.getStringExtra("RELEASE"));
        if (intent.hasExtra("SYNOPSIS"))
            mSynopsisTv.setText(intent.getStringExtra("SYNOPSIS"));
        if (intent.hasExtra("POSTER"))
            Picasso.with(this).load(intent.getStringExtra("POSTER")).into(mPosterIv);

    }
}
