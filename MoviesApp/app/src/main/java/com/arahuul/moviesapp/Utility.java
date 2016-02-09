package com.arahuul.moviesapp;

import android.content.Context;
import android.database.Cursor;

import com.arahuul.moviesapp.data.MovieContract;

/**
 * Created by a rahuul on 1/28/2016.
 */
public class Utility {

    public static boolean isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        if(numRows>0)
        {
            return true;
        }
        else {
            return false;
        }
    }

}
