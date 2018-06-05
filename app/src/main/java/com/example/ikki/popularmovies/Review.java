package com.example.ikki.popularmovies;

public class Review {

    private String mAuthor;
    private String mReview;

    public Review(String author, String review) {
        this.mAuthor = author;
        this.mReview = review;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String author) {
        this.mAuthor = author;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String review) {
        this.mReview = review;
    }
}
