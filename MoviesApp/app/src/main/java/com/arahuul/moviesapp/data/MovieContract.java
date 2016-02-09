package com.arahuul.moviesapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by a rahuul on 1/27/2016.
 */
public class MovieContract {

    public static String CONTENT_AUTHORITY="com.arahuul.moviesapp";
    public static Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);

    public static String PATH_MOVIE="movie";


    public static class MovieEntry implements BaseColumns{

        public static Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME="movie";
        public static final String COLUMN_MOVIE_ID="movie_id";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_IMAGE="image";
        public static final String COLUMN_IMAGE2="image2";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_RATING="rating";


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;


        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
}