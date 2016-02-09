package com.arahuul.moviesapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by a rahuul on 1/30/2016.
 */
public class Review {
    public String id;
    public String author;
    public String content;

    Review(JSONObject review)throws JSONException{
        this.id = review.getString("id");
        this.author = review.getString("author");
        this.content = review.getString("content");

    }

    public String getId(){return id;}

    public String getAuthor(){return author;}

    public String getContent(){return content;}
}