package com.example.ikki.popularmovies;


import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ikki on 30/03/2018.
 */

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesAdapterViewHolder> {

    private PopularMovies[] movieList;
    private final PopularMoviesAdapterOnClickHandler mPopularMoviesClickHandler;

    public void setPosterList(PopularMovies[] set_poster_list) {
        this.movieList = set_poster_list;
        notifyDataSetChanged();
    }


    public interface PopularMoviesAdapterOnClickHandler {
        void OnClick(PopularMovies popularMovies);

    }

    public PopularMoviesAdapter(PopularMoviesAdapterOnClickHandler moviesAdapterOnClickHandler) {
        this.mPopularMoviesClickHandler = moviesAdapterOnClickHandler;

    }


    public class PopularMoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;


        public PopularMoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.poster_iv);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mPopularMoviesClickHandler.OnClick(movieList[position]);
        }
    }

    @Override
    public PopularMoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int ViewHolderLayout = R.layout.poster;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(ViewHolderLayout, parent, false);
        return new PopularMoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularMoviesAdapterViewHolder holder, int position) {
        String imageList = movieList[position].getmPoster();
        Picasso.with(holder.itemView.getContext()).load(imageList).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (movieList != null) {
            ret = movieList.length;
        }
        return ret;
    }
}
