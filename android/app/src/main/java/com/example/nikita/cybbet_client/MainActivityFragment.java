package com.example.nikita.cybbet_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public GamesAdapter gamesAdapter;

    public int gameTag;

    GameInfo[] games = new GameInfo[2];

    List<GameInfo> games_list;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        games[0] = new GameInfo("Dota 2", R.drawable.dota2_logo, R.string.DOTA_TAG);
        games[1] = new GameInfo("Counter Strike: Global offensive", R.drawable.csgo_logo, R.string.CSGO_TAG);
        games_list = new ArrayList<>(Arrays.asList(games));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gamesAdapter =
                new GamesAdapter(
                        getActivity(),
                        games_list
                );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.games_listView);
        listView.setAdapter(gamesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MatchesListActivity.class);
                intent.putExtra("game", gamesAdapter.getItem(position).getTag());
                startActivity(intent);
            }
        });

        return rootView;
    }
}
