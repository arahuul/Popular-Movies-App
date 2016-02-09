package com.arahuul.moviesapp.Adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.arahuul.moviesapp.MovieItem;
import com.arahuul.moviesapp.R;
import com.squareup.picasso.Picasso;
import static android.provider.Settings.System.AIRPLANE_MODE_ON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by a rahuul on 12/12/2015.
 */
public class MovieAdapter extends ArrayAdapter<MovieItem> {
    private Context mContext;
    ArrayList<MovieItem> mMovies;


    public MovieAdapter(Context context,List<MovieItem> movieitem){
        super(context, 0, movieitem);
        this.mContext=context;
    }
/*
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }
    /*
    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
    }*/


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieItem movieitem=getItem(position);

        if(convertView==null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.grid_item,parent,false);
        }

        ImageView movieIcon=(ImageView)convertView.findViewById(R.id.movieIcon);

        //movieIcon.setImageResource(movieitem.image);

        String BASE_URL="https://image.tmdb.org/t/p";
        String size="/w500"+movieitem.poster_path;
        Uri posterUri =Uri.parse(BASE_URL.concat(size));

        Log.v("MYTAG",BASE_URL.concat(size));


            Picasso.with(mContext)
                    .load(posterUri)
                    .into(movieIcon);

        return movieIcon;
    }
}
