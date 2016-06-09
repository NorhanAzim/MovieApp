package com.example.azim_pc.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Receive the selected namesent Bundle.
        Bundle extras = getIntent().getExtras();
        if (null == savedInstanceState) {
            DetailActivityFragment mDetailsFragment = new DetailActivityFragment();
            //Pass the "extras" Bundle that contains the selected "name" to the fragment
            mDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().add(R.id.container, mDetailsFragment).commit();
        }


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

}
