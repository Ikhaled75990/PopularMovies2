package com.example.ikki.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<Review> {

    Review[] mReviews;

    public ReviewAdapter(Context context, Review[] reviews){
        super(context, 0, reviews);
        mReviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }

        Review currentReview = mReviews[position];

        TextView authorTv = itemView.findViewById(R.id.author_tv);
        authorTv.setText(currentReview.getmAuthor());

        TextView reviewTv = itemView.findViewById(R.id.review_tv);
        reviewTv.setText(currentReview.getmReview());

        return itemView;
    }

}
