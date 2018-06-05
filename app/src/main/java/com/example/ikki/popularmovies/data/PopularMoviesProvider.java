package com.example.ikki.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PopularMoviesProvider extends ContentProvider {

    private PopularMoviesDbHelper mDbHelper;

    private static final int POPULARMOVIES = 200;
    private static final int POPULARMOVIES_ID = 201;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_MOVIES, POPULARMOVIES);
        mUriMatcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_MOVIES + "/#", POPULARMOVIES_ID);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = mUriMatcher.match(uri);
        switch (match) {
            case POPULARMOVIES:
                cursor = sqLiteDatabase.query(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
                break;
            case POPULARMOVIES_ID:
                selection = PopularMoviesContract.PopularMoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot search unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case POPULARMOVIES:
                return PopularMoviesContract.PopularMoviesEntry.CONTENT_LIST_TYPE;
            case POPULARMOVIES_ID:
                return PopularMoviesContract.PopularMoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case POPULARMOVIES:
                SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
                long id = sqLiteDatabase.insert(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Unable to insert: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case POPULARMOVIES:
                rowsDeleted = sqLiteDatabase.delete(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case POPULARMOVIES_ID:
                selection = PopularMoviesContract.PopularMoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Unable to delete: " + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        int rowsUpdated;

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case POPULARMOVIES:
                rowsUpdated = sqLiteDatabase.update(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            case POPULARMOVIES_ID:
                selection = PopularMoviesContract.PopularMoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = sqLiteDatabase.update(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Unable to Update: " + uri);
        }
    }
}
