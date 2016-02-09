package com.arahuul.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arahuul.moviesapp.MovieItem;
import com.arahuul.moviesapp.R;
import com.arahuul.moviesapp.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a rahuul on 1/30/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review>{
    private Context mContext;
    ArrayList<MovieItem> mMovies;

    public ReviewAdapter(Context context,List<Review> reviewItem){
        super(context, 0, reviewItem);
        this.mContext=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review=getItem(position);

        if(convertView==null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.review_item,parent,false);
        }

        TextView author_name=(TextView)convertView.findViewById(R.id.review_author);
        TextView review_content=(TextView)convertView.findViewById(R.id.review_matter);

        author_name.setText(review.getAuthor());
        review_content.setText(review.getContent());


        return convertView;
    }
}
