package com.example.ikki.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    Trailer[] mTrailer;

    public TrailerAdapter(Context context, Trailer[] trailers){
        super(context, 0, trailers);
        mTrailer = trailers;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }

        Trailer currentTrailer  = mTrailer[position];
        TextView trailerTextView = listItemView.findViewById(R.id.trailer_view);
        trailerTextView.setText(currentTrailer.getmName());
        return listItemView;
    }
}
