package com.arahuul.moviesapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.arahuul.moviesapp.data.MovieContract;

/**
 * Created by a rahuul on 12/12/2015.
 */
public class MovieItem implements Parcelable{
    int id;
    public String poster_path;
    String title;
    String overview;
    String release_date;
    int vote_count;
    String image2;
    int rating;

    public MovieItem(){

    }

    public MovieItem(int id,String poster_path,String image2,String title,String overview,String release_date,int vote_count)
    {
        this.id=id;
        this.poster_path=poster_path;
        this.image2=image2;
        this.title=title;
        this.overview=overview;
        this.release_date=release_date;
       // this.vote_count=vote_count;
    }

     public MovieItem(Cursor cursor) {
        this.id = cursor.getInt(MovieFragment.COL_MOVIE_ID);
        this.title = cursor.getString(MovieFragment.COL_TITLE);
        this.poster_path = cursor.getString(MovieFragment.COL_IMAGE);
        this.image2 = cursor.getString(MovieFragment.COL_IMAGE2);
        this.overview = cursor.getString(MovieFragment.COL_OVERVIEW);
        this.rating = cursor.getInt(MovieFragment.COL_RATING);
        this.release_date = cursor.getString(MovieFragment.COL_DATE);
    }

    int getId()
    {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getImage() {
        return poster_path;
    }


    public String getOverview() {
        return overview;
    }

//    public int getRating() {
//        return rating;
//    }

    public String getDate() {
        return release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
       // dest.writeString(image);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(release_date);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR
            = new Parcelable.Creator<MovieItem>() {
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    private MovieItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        //image = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        rating = in.readInt();
        release_date = in.readString();
    }


}