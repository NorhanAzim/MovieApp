package com.example.azim_pc.movieapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {
    String n, FvId, v;
    String PosterItem, IdItem, DateItem, NameItem, OverviewItem, CheckItem;
    HashSet<String> hashSet = new HashSet<>();
    String FvName, FvDate, FvOverview, FvPoster, FvCheck;
    ArrayList<String> FvPosterlist = new ArrayList<>();
    ArrayList<String> FvIdList = new ArrayList<>();
    ArrayList<String> FvCheckList = new ArrayList<>();
    ArrayList<String> FvDateList = new ArrayList<>();
    ArrayList<String> FvNameList = new ArrayList<>();
    ArrayList<String> FvOverviewList = new ArrayList<>();
    int countOfPosters;
    public final String LOG_TAG = "sso";
    GridView gridView;
    Uri buildUri = null;
    ImageAdapter imageAdapter;
    NameListener nameListener = null;
    View rootView;
    String MovieJsonStr = null;
    String check;
    String poster;
    String title, Date, overview, Id;
    int MenuItemId;


    public MovieFragment() {
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new MovieTask().execute("popular");

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gv_posters);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //int MenuItemId,int postion,String tiltle,String Date,String overview,String Id,String poster, String MovieJsonString
                nameListener.setTabJsonAndPosition(MenuItemId, position, title, Date, overview, Id, poster, MovieJsonStr);

                //((MainActivity) (getActivity())).setTabJsonAndPosition(PosTab, MovieStringTab);
                // Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("pos", position).putExtra("str", MovieJsonStr);
                // startActivity(intent);
            }
        });

        return rootView;
    }

    public void setNameListener(NameListener nameListener) {
        this.nameListener = nameListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
        MenuItemId = item.getItemId();

        if (MenuItemId == R.id.menu_most_likely) {
            new MovieTask().execute("Most Likely");
            return true;

        } else if (MenuItemId == R.id.menu_popular) {
            new MovieTask().execute("popular");
            return true;

        } else if (MenuItemId == R.id.Favorites) {
            //sh.edit().clear().commit();

            //String FvName, FvDate, FvOverview, FvPoster,FvId,countOfPosters;
            ///they are big strings
            FvName = sh.getString("Names : ", " ");
            FvDate = sh.getString("Dates : ", " ");
            FvOverview = sh.getString("Overviews : ", " ");
            FvPoster = sh.getString("Poster_path : ", " ");
            FvId = sh.getString("Ids : ", " ");
            countOfPosters = sh.getInt("countOfPosters : ", 0);


            Log.v(LOG_TAG, "long names " + FvOverview);
            // int MenuItemId,int postion,String tiltle,String Date,String overview,String Id,String poster, String MovieJsonString
            AddToFavorite(MenuItemId, FvName, FvDate, FvOverview, FvPoster, FvId, countOfPosters);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddToFavorite(int Menu, String FvName, final String FvDate, final String FvOverview, String FvPoster, final String FvId, int countOfPosters) {

        // countOfPosters += countOfPosters;
        for (String r : FvId.split("--", countOfPosters)) {
            v = r.replace("--", "");
            hashSet.add(v);
        }
        FvIdList.addAll(hashSet);
        Log.v(LOG_TAG, "List of Ids : " + FvIdList);
        hashSet.clear();

        for (String s : FvPoster.split("--", countOfPosters)) {
            n = s.replace("--", "");
            hashSet.add(n);
        }
        FvPosterlist.addAll(hashSet);
        Log.v(LOG_TAG, "List of FvPoster : " + FvPosterlist);
        hashSet.clear();


        for (String r : FvName.split("--", countOfPosters)) {
            v = r.replace("--", "");
            hashSet.add(v);
        }
        FvNameList.addAll(hashSet);
        Log.v(LOG_TAG, "List of names : " + FvNameList);
        hashSet.clear();

        for (String r : FvDate.split("--", countOfPosters)) {
            v = r.replace("--", "");
            hashSet.add(v);
        }
        FvDateList.addAll(hashSet);
        Log.v(LOG_TAG, "List of Dates : " + FvDateList);
        hashSet.clear();

       for (String r : FvOverview.split("&&&&", countOfPosters)) {
            v = r.replace("&&&&", "");
           hashSet.add(v);
        }
        FvOverviewList.addAll(hashSet);
        Log.v(LOG_TAG, "List of FvOverew : " + FvOverviewList);
        hashSet.clear();

        imageAdapter = new ImageAdapter(getActivity(), FvPosterlist);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                DateItem = FvDateList.get(position);
              //  Log.v(LOG_TAG, "Date into fava " + DateItem);
                IdItem = FvIdList.get(position);
              //  Log.v(LOG_TAG, "Date into fava " + IdItem);
                PosterItem = FvPosterlist.get(position);
              //  Log.v(LOG_TAG, "Date into fava " + PosterItem);
                NameItem = FvNameList.get(position);
             //   Log.v(LOG_TAG, "Date into fava " + NameItem);
                OverviewItem = FvOverviewList.get(position);
                Log.v(LOG_TAG,"Date into fava " +OverviewItem);

                Log.v(LOG_TAG, "Into Item :");
                Log.v(LOG_TAG, DateItem +  " " + PosterItem + " " + NameItem + " " + OverviewItem);
                //int MenuItemId,int postion,String tiltle,String Date,String overview,String Id,String poster, String MovieJsonString
                nameListener.setTabJsonAndPosition(MenuItemId, position, NameItem, DateItem, OverviewItem, IdItem, PosterItem, MovieJsonStr);
            }
        });
    }

    ////to start rotation
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        nameListener = (NameListener) activity;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //// Async consists of Parmaters , progress ,result
    class MovieTask extends AsyncTask<String, Void, ArrayList> {
        @Override
        protected ArrayList doInBackground(String... params) {

            if (params.length == 0) {
                //     Log.v(LOG_TAG, "doInBackground is Null");
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/";   ///from the url
                final String MOVPopular_BASE_URL = "3/movie/popular?";
                final String MOVMostLikely_BASE_URL = "3/movie/top_rated?";
                final String API_KEY_PARAM = "api_key";
//http://api.themoviedb.org/3/movie/popular?api_key=30e0eb4c9a04152e0620151e5d67943c


                if (params[0].equals("popular")) {
                    buildUri = Uri.parse(MOVIE_BASE_URL + MOVPopular_BASE_URL).buildUpon()
                            //.appendPath(MOVPopular_BASE_URL)
                            .appendQueryParameter(API_KEY_PARAM, BuildConfig.OPEN_MovieApp_API_KEY)
                            .build();
                } else {
                    buildUri = Uri.parse(MOVIE_BASE_URL + MOVMostLikely_BASE_URL).buildUpon()
                            //.appendPath(MOVPopular_BASE_URL)
                            .appendQueryParameter(API_KEY_PARAM, BuildConfig.OPEN_MovieApp_API_KEY)
                            .build();
                }

                URL url = new URL(buildUri.toString());
                // Log.v(LOG_TAG, "Built URI " + buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");


                urlConnection.connect();
                //////Read the input stream into a String
                /////////Read every thing in that Url into buffer
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                /////////Put all the data of the Url int from the inputStream into reader then into buffer to make the debugging easier.
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
////////////// get all the jsonstring here
                MovieJsonStr = buffer.toString();
                //  Log.v(LOG_TAG, "MOVIE Json String  " + MovieJsonStr);

            } catch (IOException e) {

                Log.e(LOG_TAG, "Movie Json String Not found", e);
                return null;
            } finally {

                if (urlConnection != null) {                                                        ///////////////////////Mean?
                    urlConnection.disconnect();
                }
                if (reader != null) {

                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error connection", e);
                    }
                }
            }
            try {
                return getPoster_PathDataFromJson(MovieJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;


        }

        @Override
        protected void onPostExecute(ArrayList arr) {
            //  super.onPostExecute(arr);
            if (arr != null) {
                imageAdapter = new ImageAdapter(getActivity(), arr);
                gridView.setAdapter(imageAdapter);
            } else {
                Log.v(LOG_TAG, "error ");
            }
        }

    }

    public ArrayList<String> getPoster_PathDataFromJson(String MovieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        // title, release date, movie poster, vote average, and plot synopsis.

        final String Movie_result = "results";
        final String Movie_poster = "poster_path";


        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONObject BlockIntoArray;

        ArrayList<String> Poster_PathsList = new ArrayList<>();
        int result = BigJsonObject.getJSONArray(Movie_result).length();

        for (int i = 0; i < result; i++) {     // 20= no of blocks into the result_array/////Into that result_array it has many blocks

            BlockIntoArray = BigJsonObject.getJSONArray(Movie_result).getJSONObject(i); ///get the block that contains poster paths,titles,....
            Poster_PathsList.add("http://image.tmdb.org/t/p/w185" + (String) BlockIntoArray.get(Movie_poster));  //the name it self we should get

        }
        //Log.v(LOG_TAG, "I got the Posters");

        return Poster_PathsList;        //////list contains all the poster_paths

    }
}