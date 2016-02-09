package com.arahuul.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arahuul.moviesapp.Adapters.ReviewAdapter;
import com.arahuul.moviesapp.Adapters.TrailerAdapter;
import com.arahuul.moviesapp.data.MovieContract;
import com.squareup.picasso.Picasso;

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
import java.util.List;

/**
 * Created by a rahuul on 2/5/2016.
 */
public class DetailActivityFragment extends Fragment {

    private MovieItem movie;
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;
    public ListView mTrailersView;
    ListView mReviewsView;
    public static String DETAIL_MOVIE="movie";

    public DetailActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        //return super.onCreateOptionsMenu(menu);
        if(movie!=null) {
            inflater.inflate(R.menu.menu_details, menu);
            MenuItem item = menu.findItem(R.id.mark_fav);

            if (Utility.isFavorited(getActivity(), movie.getId())) {
                item.setIcon(R.drawable.light_on);
            } else {
                item.setIcon(R.drawable.light_off);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.mark_fav)
        {
            // int b=(1-isFavorite);
            if(Utility.isFavorited(getActivity(),movie.getId())) {
                //delete movie entry from database
                new AsyncTask<Void,Void,Integer>(){

                    @Override
                    protected Integer doInBackground(Void... params) {

                        return getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                new String[]{Integer.toString(movie.getId())});
                    }

                    @Override
                    protected void onPostExecute(Integer rowsDeleted) {
                        item.setIcon(R.drawable.light_off);

                        Toast mToast = Toast.makeText(getContext(), "Movie Deleted", Toast.LENGTH_SHORT);
                        mToast.show();

                    }
                }.execute();

            }
            else {

                new AsyncTask<Void,Void,Uri>(){
                    @Override
                    protected Uri doInBackground(Void... params) {
                        ContentValues values=new ContentValues();

                        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE, movie.getImage());
                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE2, "aaaaaa");
                        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                        values.put(MovieContract.MovieEntry.COLUMN_RATING,"bbbbb");
                        values.put(MovieContract.MovieEntry.COLUMN_DATE, movie.getDate());


                        return getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);
                    }

                    @Override
                    protected void onPostExecute(Uri uri) {
                        item.setIcon(R.drawable.light_on);
                        Toast mToast = Toast.makeText(getContext(),"Movie Entry Inserted", Toast.LENGTH_SHORT);
                        mToast.show();
                    }
                }.execute();
            }
        }
        return true;

        //return super.onOptionsItemSelected(item);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.activity_detail, container, false);


        Bundle arguments=getArguments();

        if(arguments!=null){
            movie=arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }

        Intent i=getActivity().getIntent();

        TextView title=(TextView)rootView.findViewById(R.id.MovieTitle);
        ImageView icon=(ImageView)rootView.findViewById(R.id.DetailIcon);
        TextView release_date=(TextView)rootView.findViewById(R.id.release_date);
        TextView overview=(TextView)rootView.findViewById(R.id.overview);
        //TextView vote_count=(TextView)findViewById(R.id.vote_count);
        ListView mTrailersView=(ListView)rootView.findViewById(R.id.detail_trailers);

        mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        mTrailersView.setAdapter(mTrailerAdapter);
        mTrailersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = mTrailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(intent);

            }
        });

        mReviewsView=(ListView)rootView.findViewById(R.id.detail_reviews);
        mReviewAdapter=new ReviewAdapter(getActivity(),new ArrayList<Review>());
        mReviewsView.setAdapter(mReviewAdapter);

//        title.setText(i.getExtras().getString("title"));
//        Picasso.with(getContext())
//                .load(i.getExtras().getString("image"))
//                .into(icon);
//
//        release_date.setText(i.getExtras().getString("release_date"));
//        overview.setText(i.getExtras().getString("overview"));
//        //vote_count.setText(i.getExtras().getString("vote_count"));
//
//        int id=i.getExtras().getInt("id");
//        String img=i.getExtras().getString("image");
//        String Title=i.getExtras().getString("title");
//        String Overview=i.getExtras().getString("overview");
//        String Release_date=i.getExtras().getString("release_date");
//        int Vote_count=i.getExtras().getInt("vote_count");
//        String poster_path=i.getExtras().getString("poster_path");
//        String img2=null;
//        movie=new MovieItem(id,poster_path,img2,Title,Overview,Release_date,Vote_count);

        if(movie!=null) {
            title.setText(movie.getTitle());
            Picasso.with(getContext())
                    .load("https://image.tmdb.org/t/p/w960_and_h540_bestv2" + movie.getImage())
                    .into(icon);

            release_date.setText(movie.getDate());
            overview.setText(movie.getOverview());
        }


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (movie != null) {
            new FetchTrailersTask().execute(Integer.toString(movie.getId()));
            new FetchReviewsTask().execute(Integer.toString(movie.getId()));
        }
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {


        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> results = new ArrayList<>();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                }
            }

            return results;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM,"61dcaa3782425fc16e8c6014f7b0aade")
                        .build();

                URL url = new URL(builtUri.toString());

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
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    //mTrailersCardview.setVisibility(View.VISIBLE);
                    if (mTrailerAdapter != null) {
                        mTrailerAdapter.clear();
                        for (Trailer trailer : trailers) {
                            mTrailerAdapter.add(trailer);
                        }
                    }

                }
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String,Void,List<Review> >{
        String LOG_TAG=getClass().getSimpleName();

        List<Review> getReviewsDataFromJson(String reviewJsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(reviewJsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            List<Review> results=new ArrayList<>() ;
            for (int i=0;i<reviewArray.length();i++)
            {
                JSONObject review=reviewArray.getJSONObject(i);
                Review reviewItem=new Review(review);

                results.add(reviewItem);
            }
            return results;
        }

        @Override
        protected List<Review> doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String reviewJsonStr=null;

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, "61dcaa3782425fc16e8c6014f7b0aade")
                        .build();

                URL url = new URL(builtUri.toString());



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

                reviewJsonStr = buffer.toString();
                Log.v(LOG_TAG,"The Reviews Json String : "+reviewJsonStr);
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
                return getReviewsDataFromJson(reviewJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage());
                e.printStackTrace();
            }
            return  null;
        }

        //post results after do in background

        //@Override
        //MovieAdapter movieadapter=new MovieAdapter(mContext,"");
        protected void onPostExecute(List<Review> resultStr) {


            if(resultStr!=null)
            {

                //lst.addAll(Arrays.asList(resultStr));
                // mReviewsView=(ListView)findViewById(R.id.detail_reviews);
                //mReviewAdapter=new ReviewAdapter(getApplication(),resultStr);
                // movieadapter.addAll(Arrays.asList(resultStr));
                //grid.invalidate();
                for (Review review : resultStr) {
                    mReviewAdapter.add(review);
                }

            }

        }
    }
}