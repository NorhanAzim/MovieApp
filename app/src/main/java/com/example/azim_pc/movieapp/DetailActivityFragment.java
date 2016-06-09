package com.example.azim_pc.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivityFragment extends Fragment {

    String poster, str, title, date, overvieww, d, y, m, Ndate, Nadults, TrailerJsonStr;
    double vote;
    ArrayList<String> TrailerNames = new ArrayList<>();
    int id, pos;
    ArrayAdapter<String> TrailerAdapter;
    View rootView;
    Intent intent;
    String FvName, FvDate, FvOverview, FvPoster, FvId, Fvcheck;
    int countOfPosters;
    int result;
    ImageView star, ss;
    JSONObject BigJsonObject;
    JSONObject BlockIntoArray;
    boolean adults;
    ArrayList<Object> arr = new ArrayList<>();
    ListView TrailerListView;
    private final String LOG_TAG = "sso";
    String Dates, Overviews, poster_path, Ids, Names, check;
    int MenuItemId;

    public DetailActivityFragment() {
    }

//position,String Title,String Date,String Overview,String Name, String MovieJsonString

    @Override //Bundle for two pane (tablet)
    public void setArguments(Bundle args) {
        super.setArguments(args);

        MenuItemId = getArguments().getInt("menuu");
        str = getArguments().getString("str");
        Dates = getArguments().getString("Dates : ");
        Names = getArguments().getString("Names : ");
        poster_path = getArguments().getString("Poster_path : ");
        pos = getArguments().getInt("pos");
        Ids = getArguments().getString("Ids : ");
        Overviews = getArguments().getString("Overviews : ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        //to make the screen rotate
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        intent = getActivity().getIntent();
        final ImageView imageView;
        ImageView imageView1;


        ////for phones  when transfer between two activties
        if (intent != null && intent.hasExtra("str") && intent.hasExtra("Ids : ")) {
            MenuItemId = intent.getIntExtra("menuu", 0);
            pos = intent.getIntExtra("pos", -1);
            str = intent.getStringExtra("str");            //Movie Json String
            Dates = intent.getStringExtra("Dates : ");
            Names = intent.getStringExtra("Names : ");
            Overviews = intent.getStringExtra("Overviews : ");
            poster_path = intent.getStringExtra("Poster_path : ");
            Ids = intent.getStringExtra("Ids : ");


        }
       // Log.v(LOG_TAG, "check intents for phone: " + MenuItemId + " " + Dates + " " + Names + " " + Overviews + " " + poster_path + " " + Ids);
        // 2131493012
        // 2131493011
        //Log.v(LOG_TAG, "check MenuId out : " + MenuItemId);

        if (MenuItemId == 0 || MenuItemId == 2131493012 || MenuItemId == 2131493011) {     ///it is top_rated or popular3
          //  Log.v(LOG_TAG, "check MenuId ito : " + MenuItemId);
            try {
                title = (String) getPoster_PathDataFromJson(pos, str).get(0);
                poster = (String) getPoster_PathDataFromJson(pos, str).get(1);
                overvieww = (String) getPoster_PathDataFromJson(pos, str).get(2);
                date = (String) getPoster_PathDataFromJson(pos, str).get(3);
                adults = (boolean) getPoster_PathDataFromJson(pos, str).get(4);
                vote = (double) getPoster_PathDataFromJson(pos, str).get(5);
                id = (int) getPoster_PathDataFromJson(pos, str).get(6);

                if (date.contains("-")) {
                    y = "" + date.charAt(0) + date.charAt(1) + date.charAt(2) + date.charAt(3);
                    m = "" + date.charAt(5) + date.charAt(6);
                    d = "" + date.charAt(8) + date.charAt(9);
                }
                Ndate = d + "-" + m + "-" + y;
                if (adults == true) {
                    Nadults = "+18";
                } else {
                    Nadults = "-18";
                }

            } catch (JSONException e) {
                Log.v(LOG_TAG, "Eroooooor in getting details !! ");
                e.printStackTrace();
            }


            try {
              //  Log.v(LOG_TAG, "Hello into Details11 " + check + " " + date + " " + id + " " + poster + " " + title + " " + overvieww);

                ((TextView) rootView.findViewById(R.id.title)).setText(title);

                imageView1 = ((ImageView) rootView.findViewById(R.id.image_Poster));
                Picasso.with(getActivity()).load(poster).into(imageView1);

                ((TextView) rootView.findViewById(R.id.overView)).setText(overvieww);
                ((TextView) rootView.findViewById(R.id.date)).setText("Realse date :" + Ndate);
                ((TextView) rootView.findViewById(R.id.adults)).setText("Adults :" + Nadults);
                ((TextView) rootView.findViewById(R.id.vote)).setText("Average rate :" + String.valueOf(vote));


                star = (ImageView) rootView.findViewById(R.id.fvimag);


                new geTrailer().execute(id);
                new getReview(getActivity(), rootView).execute(id);

            } catch (Exception e) {
                Log.v(LOG_TAG, "errrrrrrrrrrrror in executing ", e);
                e.printStackTrace();
            }
            star.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //check = "true";
                    star.setImageResource(R.drawable.yello);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPref.edit();

                    //sharedPref.edit().clear().commit();
                    countOfPosters = sharedPref.getInt("countOfPosters : ", 0);
                    countOfPosters++;
                    editor.putInt("countOfPosters : ", countOfPosters);
                    editor.remove("countOfPosters : ").apply();

                    FvId = sharedPref.getString("Ids : ", " ");
                    FvId += id + "--";
                    editor.putString("Ids : ", FvId);
                    Log.v(LOG_TAG, "List of editor Ids " + FvId);

                    FvOverview = sharedPref.getString("Overviews : ", " ");
                    FvOverview += overvieww + "&&&&";
                    editor.putString("Overviews : ", FvOverview);
                    Log.v(LOG_TAG, "List of editor Overviews " + FvOverview);

                    FvDate = sharedPref.getString("Dates : ", " ");
                    FvDate += Ndate + "--";
                    editor.putString("Dates : ", FvDate);
                    Log.v(LOG_TAG, "List of editor Dates " + FvDate);

                    Fvcheck = sharedPref.getString("check : ", "");
                    Fvcheck += check + "--";
                    editor.putString("check : ", Fvcheck);
                    Log.v(LOG_TAG, "List of editor Check " + Fvcheck);

                    FvName = sharedPref.getString("Names : ", " ");
                    FvName += title + "--";
                    editor.putString("Names : ", FvName);
                    Log.v(LOG_TAG, "List of editor name " + FvName);

                    FvPoster = sharedPref.getString("Poster_path : ", " ");
                    FvPoster += poster + "--";
                    editor.putString("Poster_path : ", FvPoster);
                    Log.v(LOG_TAG, "List of editor Posters " + FvPoster);

                    //  Log.v(LOG_TAG, "hello : " + countOfPosters + "\n hh  " + FvName);
                    editor.commit();

                }
            });

        } else {    ///if favourite

            ((TextView) rootView.findViewById(R.id.title)).setText(Names);

            imageView = ((ImageView) rootView.findViewById(R.id.image_Poster));
            Picasso.with(getActivity()).load(poster_path).into(imageView);

            ((TextView) rootView.findViewById(R.id.overView)).setText(Overviews);
            ((TextView) rootView.findViewById(R.id.date)).setText("Realse date :" + Dates);

            int t = Integer.parseInt(Ids);
            Log.v(LOG_TAG, "Hello into Parsing t " + t);

            new geTrailer().execute(t);
            new getReview(getActivity(), rootView).execute(t);

        }


        return rootView;

    }

    public ArrayList<Object> getPoster_PathDataFromJson(int pos, String MovieJsonStr) throws
            JSONException {

        //Log.v(LOG_TAG, "Iam  Json ");
        BigJsonObject = new JSONObject(MovieJsonStr);

        BlockIntoArray = BigJsonObject.getJSONArray("results").getJSONObject(pos); ///get the block that contains poster paths,titles,....
        arr.add(BlockIntoArray.get("title"));
        arr.add("http://image.tmdb.org/t/p/w185" + BlockIntoArray.get("poster_path"));
        arr.add(BlockIntoArray.get("overview"));
        arr.add(BlockIntoArray.get("release_date"));
        arr.add(BlockIntoArray.get("adult"));
        arr.add(BlockIntoArray.get("vote_average"));
        arr.add(BlockIntoArray.get("id"));

        return arr;
    }


    public class geTrailer extends AsyncTask<Integer, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Integer... params) {

            if (params.length == 0) {
                Log.v(LOG_TAG, "doInBackground is Null");
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

//http://api.themoviedb.org/3/movie/209112/videos?api_key=67c3e9a8357661792e5956106c3de3f6
                Uri buildUri;
                final String MOVIE_BASE_URL = "http://api.themoviedb.org//3/movie/";

                buildUri = Uri.parse(MOVIE_BASE_URL + params[0] + "/videos?api_key=67c3e9a8357661792e5956106c3de3f6").buildUpon().build();

                URL url = new URL(buildUri.toString());
                Log.v(LOG_TAG, "Built URI of trailer " + url);

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

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                TrailerJsonStr = buffer.toString();
                //   Log.v(LOG_TAG, "Trailer Json String  " + TrailerJsonStr);

            } catch (IOException e) {

                Log.e(LOG_TAG, "Trailer Json String Not found", e);
                return null;
            } finally {

                if (urlConnection != null) {
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
                return getTrailer_DataFromJson(TrailerJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "back to doInBackGround !");
                // e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList ArrayTrailer) {

            if (ArrayTrailer != null) {
                // Log.v(LOG_TAG, "Into onPost");
                try {
                    TrailerNames = getTrailerName(TrailerJsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TrailerAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_trailer, R.id.TextOfTrailer, TrailerNames);
                TrailerListView = (ListView) rootView.findViewById(R.id.TrailerListView);
                TrailerListView.setAdapter(TrailerAdapter);
                setListViewHeightBasedOnChildren(TrailerListView);
                TrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(ArrayTrailer.get(position).toString()));
                        startActivity(intent);
                    }
                });
            }
        }

        public ArrayList<String> getTrailerName(String TrailerJsonStr) throws JSONException {

            JSONObject BigJsonObject2 = new JSONObject(TrailerJsonStr);
            JSONObject BlockIntoArray;

            ArrayList<String> ArrayName = new ArrayList<>();

            result = BigJsonObject2.getJSONArray("results").length();
            Log.v(LOG_TAG, "res : " + result);
            try {
                for (int i = 0; i < result; i++) {
                    BlockIntoArray = BigJsonObject2.getJSONArray("results").getJSONObject(i);
                    ArrayName.add((String) BlockIntoArray.get("name"));
                }
                // Log.v(LOG_TAG, "Trailers : " + ArrayName);
            } catch (Exception e) {
                Log.e(LOG_TAG, "error in getting trailer", e);
                e.printStackTrace();

            }
            return ArrayName;

        }

        public ArrayList<String> getTrailer_DataFromJson(String TrailerJsonStr) throws JSONException {

            JSONObject BigJsonObject2 = new JSONObject(TrailerJsonStr);
            JSONObject BlockIntoArray;

            ArrayList<String> ArrayName = new ArrayList<>();
            ArrayList<String> ArrayTrailer = new ArrayList<>();
            ArrayList<String> ArrayKeys = new ArrayList<>();

            int result = BigJsonObject2.getJSONArray("results").length();
            // Log.v(LOG_TAG, "res : " + result);
            try {
                for (int i = 0; i < result; i++) {
                    BlockIntoArray = BigJsonObject2.getJSONArray("results").getJSONObject(i);

                    ArrayName.add((String) BlockIntoArray.get("name"));
                    ArrayKeys.add((String) BlockIntoArray.get("key"));
                    ArrayTrailer.add("https://www.youtube.com/watch?v=" + BlockIntoArray.get("key"));

                }
                //  Log.v(LOG_TAG, "Trailers : " + ArrayName);
            } catch (Exception e) {
                Log.e(LOG_TAG, "error in getting trailer", e);
                e.printStackTrace();

            }
            return ArrayTrailer;


        }
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = ListView.MeasureSpec.makeMeasureSpec(listView.getWidth(), ListView.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, ListView.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

}


