package com.example.azim_pc.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class getReview extends AsyncTask<Integer, Void, ArrayList<String>>{
    ArrayAdapter <String >reviewAadapter;
    Intent intent;
    private static String LOG_TAG = "sso";
    String ReviewJsonStr;
    ListView ReviewListView;
    View rootView;
    int id;
    Context context;

    //to get activity
public getReview(Context context,View rootview){
    this.context=context;
    this.rootView=rootview;

}
    @Override
    protected ArrayList<String> doInBackground(Integer... params) {
        if (params.length == 0) {
          //  Log.v(LOG_TAG, "doInBackground is Null");
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
          //  Log.v(LOG_TAG, "inttttttttto");
            Uri buildUri;

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            buildUri = Uri.parse(MOVIE_BASE_URL + params[0] + "/reviews?api_key=67c3e9a8357661792e5956106c3de3f6").buildUpon().build();
          //  Log.v(LOG_TAG,"Id is :"+params[0] );
                    URL url = new URL(buildUri.toString());
        //    Log.v(LOG_TAG, "Built URI of reviews " +buildUri.toString());

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
            ReviewJsonStr = buffer.toString();
         //   Log.v(LOG_TAG, "review Json String  " +  ReviewJsonStr);

        } catch (IOException e) {

            Log.e(LOG_TAG, "review Json String Not found", e);
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
            return getReview_DataFromJson( ReviewJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "back to doInBackGround !");
            // e.printStackTrace();
        }
        return null;
    }


    public ArrayList<String> getReview_DataFromJson(String TrailerJsonStr) throws JSONException {

        JSONObject BigJsonObject2 = new JSONObject(TrailerJsonStr);
        JSONObject BlockIntoArray;
        ArrayList<String> Auther_content = new ArrayList<>();
        int result = BigJsonObject2.getJSONArray("results").length();
        String[] ArrayAuther = new String[result];
        String []Arraycontent=new String[result] ;

     //   Log.v(LOG_TAG, "res : " + result);
        try {
            for (int i = 0; i < result; i++) {
                BlockIntoArray = BigJsonObject2.getJSONArray("results").getJSONObject(i);

                ArrayAuther[i]=(String) BlockIntoArray.get("author");
                Arraycontent[i]=(String) BlockIntoArray.get("content");
                Auther_content.add("Author :" + ArrayAuther[i]+ "\n" + "Review :" + Arraycontent[i]);

            }

          //  Log.v(LOG_TAG, "Hello :" + Auther_content);
        } catch (Exception e) {
            Log.e(LOG_TAG, "error in getting trailer", e);
            e.printStackTrace();

        }
        return Auther_content;

    }
    @Override
    protected void onPostExecute(ArrayList Auther_content) {
        // super.onPostExecute(strings);
        reviewAadapter = new ArrayAdapter(context, R.layout.list_item_review, R.id.TextOfReview, Auther_content);
        ReviewListView = (ListView) rootView.findViewById(R.id.reviewList);
        ReviewListView.setAdapter(reviewAadapter);
        setListViewHeightBasedOnChildren(ReviewListView);
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
