package com.example.azim_pc.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements NameListener {
    boolean mTwoPane;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrameLayout flPanel2 = (FrameLayout) findViewById(R.id.flPanel_Two);
        if (null == flPanel2) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }
        if (null == savedInstanceState) {
            MovieFragment MovFragment = new MovieFragment();
            MovFragment.setNameListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.flPanel_One, MovFragment).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override    ///the name that had been selected
    //int MenuItemId,int postion,String tiltle,String Date,String overview,String Id,String poster, String MovieJsonString
    public void setTabJsonAndPosition(int MenuItemId, int position, String Name, String Date , String Overview, String Id,String poster_path, String MovieJsonString) {
        ////single pane
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("pos", position).putExtra("str", MovieJsonString).putExtra("Names : ", Name).putExtra("Dates : ", Date)
                    .putExtra("Overviews : ", Overview).putExtra("Poster_path : ", poster_path).putExtra("Ids : ", Id)
                    .putExtra("menuu",MenuItemId);
            startActivity(intent);
        } else {
            ////double pane UI   Tablets
            DetailActivityFragment DetailsTab = new DetailActivityFragment();
            Bundle extras = new Bundle();

            extras.putInt("menuu",MenuItemId);
            extras.putString("Names : ", Name);
            extras.putString("Dates : ", Date);
            extras.putString("Overviews : ", Overview);
            extras.putString("Poster_path : ", poster_path);
            extras.putString("Ids : ", Id);
            extras.putInt("pos", position);
            extras.putString("str", MovieJsonString);

            DetailsTab.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.flPanel_Two, DetailsTab).commit();
        }
    }
}