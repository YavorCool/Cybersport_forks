package com.example.nikita.cybbet_client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MatchesListActivity extends AppCompatActivity {
    String LOG_TAG = "MatchesListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Matches List Activity", "OnCreate method called");
        setContentView(R.layout.activity_matches_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MatchesListActivityFragment matchesListActivityFragment =
                (MatchesListActivityFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.content_matches_list);

        matchesListActivityFragment.gameTag = getIntent().getIntExtra("game", R.string.DOTA_TAG);
        matchesListActivityFragment.updateMatches();
    }

}
