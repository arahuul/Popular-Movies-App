package com.arahuul.moviesapp;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.arahuul.moviesapp.Adapters.MovieAdapter;
import com.arahuul.moviesapp.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by a rahuul on 12/12/2015.
 */
public class MovieFragment extends Fragment {

    MovieAdapter movieadapter;
    GridView grid;

    MovieItem[] movieitem={/*
            new MovieItem("/fYzpM9GmpBlIC893fNjoWCwE24H.jpg","MY MOVIE","","")*/
    };


    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_IMAGE2,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DATE
    };


    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_IMAGE2 = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;
    public static final int COL_DATE = 7;


    public MovieFragment(){

    }

    public interface Callback {
        void onItemSelected(MovieItem movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.popularity_desc)
        {
            FetchMoviesTask movietask=new FetchMoviesTask();
            movietask.execute("POPULARITY.desc");
            return true;
        }

        if(id==R.id.rating)
        {
            FetchMoviesTask moviestask=new FetchMoviesTask();
            moviestask.execute("RATING");
        }

        if(id==R.id.favourite)
        {
            FetchFavMoviesTask favMoviesTask=new FetchFavMoviesTask();
            favMoviesTask.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fragment_main, container, false);

        //GridView grid=(GridView)rootview.findViewById(R.id.grid);

        movieadapter=new MovieAdapter(getActivity(), Arrays.asList(movieitem));
        grid=(GridView)rootview.findViewById(R.id.grid);
        grid.setAdapter(movieadapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MovieItem movie = movieadapter.getItem(position);
                ((Callback)getActivity()).onItemSelected(movie);
            }
        });

            //ArrayList<MovieItem> al=new ArrayList<>();
       // movieadapter=new MovieAdapter(getActivity(),al);

        return rootview;
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,MovieItem[] > {
        String LOG_TAG=FetchMoviesTask.class.getSimpleName();

        private MovieItem[] getMoviesDataFromJson(String moviesJsonStr)
        throws JSONException{
            final String OWM_RESULTS="results";
            final String OWM_POSTER="poster_path";
            final String OWM_TITLE="title";
            final String OWM_OVERVIEW="overview";
            final String OWM_RELEASE_DATE="release_date";
            final String OWN_VOTE_COUNT="vote_count";

            JSONObject moviesJSON=new JSONObject(moviesJsonStr);
            JSONArray moviesArray=moviesJSON.getJSONArray(OWM_RESULTS);

           // String[] resultsStr=new String[20];
           // ArrayList resultsStr =new ArrayList<>();
            MovieItem[] resultsStr=new MovieItem[20];
            for(int i=0;i<moviesArray.length();i++)
            {
                String title;
                String img;
                String image2;
                String overview;
                String release_date;
                int vote_count;
                int id;
                JSONObject movieObject=moviesArray.getJSONObject(i);

                //title=movieObject.getString(OWM_TITLE);
                id=movieObject.getInt("id");
                img=movieObject.getString(OWM_POSTER);
                title=movieObject.getString(OWM_TITLE);
                overview=movieObject.getString(OWM_OVERVIEW);
                release_date=movieObject.getString(OWM_RELEASE_DATE);
                vote_count=movieObject.getInt(OWN_VOTE_COUNT);
                image2=movieObject.getString("backdrop_path");

                MovieItem movie=new MovieItem(id,img,image2,title,overview,release_date,vote_count);

                resultsStr[i]=movie;

            }

          /*  for(String s:resultsStr){
                Log.v(LOG_TAG,"MOVIE ICON : "+s);
            }*/

            return resultsStr;
        }

        @Override
        protected MovieItem[] doInBackground(String... params) {

            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String moviesJsonStr=null;


            try {
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?";
                String SORT_KEY="sort_by="+params.toString();
                String API_KEY="&api_key=61dcaa3782425fc16e8c6014f7b0aade";

                String urlString=baseUrl.concat(SORT_KEY).concat(API_KEY);

                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine())!= null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG,"The Movies Json String : "+moviesJsonStr);
            }catch (IOException e){
                Log.e(LOG_TAG,"Error",e);

                return null;
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try{
                        reader.close();
                    }
                    catch (final IOException e)
                    {
                        Log.e(LOG_TAG,"ERROR Closing stream",e);
                    }
                }
            }

            try{
                return getMoviesDataFromJson(moviesJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage());
                e.printStackTrace();
            }
                return  null;
        }

        //post results after do in background

        //@Override
        //MovieAdapter movieadapter=new MovieAdapter(mContext,"");
        protected void onPostExecute(MovieItem[] resultStr) {


            if(resultStr!=null)
            {

                        //lst.addAll(Arrays.asList(resultStr));
                        movieadapter=new MovieAdapter(getActivity(),Arrays.asList(resultStr));
                       // movieadapter.addAll(Arrays.asList(resultStr));
                //grid.invalidate();
                grid.setAdapter(movieadapter);
//                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        MovieItem movie = movieadapter.getItem(position);
//                        ((Callback) getActivity().onItemSelected(movie));
//                    }

//                        String imgurl="https://image.tmdb.org/t/p/w960_and_h540_bestv2"+movie.poster_path;
//                        String title=movie.title;
//                        String overview=movie.overview;
//                        String release_date=movie.release_date;
//                        String poster_path=movie.poster_path;
//                        int vote_count=movie.vote_count;
//                        int Id=movie.id;
//                        String img2url="https://image.tmdb.org/t/p/w960_and_h540_bestv2"+movie.image2;
//                        //String vote_count=String.valueOf(vc);
//
//                        Intent detailActivityIntent=new Intent(getActivity(),DetailActivity.class)
//                                .putExtra("id",Id)
//                                .putExtra("image",imgurl)
//                                .putExtra("image2",img2url)
//                                .putExtra("title", title)
//                                .putExtra("overview",overview)
//                                .putExtra("release_date",release_date)
//                                .putExtra("vote_count",vote_count)
//                                .putExtra("poster_path", poster_path);
//
//                        startActivity(detailActivityIntent);
//                    }
//                });


            }

        }
    }

    public class FetchFavMoviesTask extends AsyncTask<Void,Void,ArrayList<MovieItem>>{

        @Override
        protected ArrayList<MovieItem> doInBackground(Void... params) {

            Cursor cursor=getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);

            ArrayList<MovieItem> results=new ArrayList<MovieItem>(20);
            int i=0;

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MovieItem movie = new MovieItem(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> results) {

                if(results!=null)
                {
                    movieadapter=new MovieAdapter(getActivity(),results);
                    // movieadapter.addAll(Arrays.asList(resultStr));
                    //grid.invalidate();
                    if(movieadapter!=null) {
                        grid.setAdapter(movieadapter);
                    }
                }
            //super.onPostExecute(results);

        }
    }



}