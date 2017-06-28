package com.example.nikita.cybbet_client;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchesListActivityFragment extends Fragment {
    private String EGB_TITLE = "EGB";
    private String GGBET_TITLE = "GGbet";

    MatchesAdapter matchesAdapter;

    public int gameTag;
    String gameParam;

    public MatchesListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final List<MatchInfo> matchesList = new ArrayList<>();

        matchesAdapter = new MatchesAdapter(
                getActivity(),
                matchesList
                );


        View rootView = inflater.inflate(R.layout.fragment_matches_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.matches_listView);
        listView.setAdapter(matchesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("fork", matchesAdapter.getItem(position).getFork());
                intent.putExtra("bets", matchesAdapter.getItem(position).getBets());
                intent.putExtra("teams", matchesAdapter.getItem(position).getTeams());
                intent.putExtra("datetime", matchesAdapter.getItem(position).getDatetime());
                intent.putExtra("urls", matchesAdapter.getItem(position).getUrls());
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateMatches(){
        switch (gameTag) {
            case R.string.DOTA_TAG:
                gameParam = "dota-2";
                break;
            case R.string.CSGO_TAG:
                gameParam = "csgo";
        }

        FetchMatchesTask matchesTask = new FetchMatchesTask();
        matchesTask.execute(gameParam);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            FetchMatchesTask matchesTask = new FetchMatchesTask();
            matchesTask.execute(gameParam);
        }
        return super.onOptionsItemSelected(item);
    }


    private class FetchMatchesTask extends AsyncTask<String, Void, MatchInfo[]> {

        @Override
        protected MatchInfo[] doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String jsonStr = null;
            try {
                MatchInfo[] matches;

                String baseUrl = "https://qldwhdfdgn.localtunnel.me/";
                URL url = new URL(baseUrl.concat(params[0]));

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
                JSONArray matchesJson = new JSONArray(jsonStr);
                matches = new MatchInfo[matchesJson.length()];


                for (int i = 0; i < matchesJson.length(); i++){
                    JSONObject match = matchesJson.getJSONObject(i);

                    String[] bets = null;
                    String[] urls = null;
                    String[] fork = null;

                    String teams = match.getJSONArray("teams").getString(0)
                            + " vs " + match.getJSONArray("teams").getString(1);


                    JSONArray betsJson = match.getJSONArray("bets");
                    JSONArray forkJson = match.getJSONArray("fork");
                    if(betsJson.length() != 0){
                        Log.d("_", "betsJson.length = " + String.valueOf(betsJson.length()));
                        bets = new String[betsJson.length()];
                        urls = new String[betsJson.length()];
                        for (int j = 0; j < betsJson.length(); j++){
                            bets[j] = betsJson.getJSONObject(j).get("title") + "  "
                                    + betsJson.getJSONObject(j).getJSONArray("coefs").get(0).toString() + "  "
                                    + betsJson.getJSONObject(j).getJSONArray("coefs").get(1).toString();

                            urls[j] = betsJson.getJSONObject(j).get("url").toString();
                        }
                    }

                    if (forkJson.length() != 0) {
                        fork = new String[5];
                        fork[0] = "Прибыль от суммы: " + forkJson.get(0);
                        fork[1] = "Ставка на команду 1: " + forkJson.get(1);
                        fork[2] = "Ставка на команду 2: " + forkJson.get(2);
                        fork[3] = "Коэфф. для ком. 1: " + forkJson.get(3);
                        fork[4] = "Коэфф. для ком. 2: " + forkJson.get(4);
                    }

                    String datetime = match.get("datetime").toString();

                    matches[i] = new MatchInfo(teams, datetime, fork, bets, urls);


                }
                return matches;

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(MatchInfo[] matches) {
            if (matches != null) {
                matchesAdapter.clear();
                for(MatchInfo matchInfo : matches) {
                    matchesAdapter.add(matchInfo);
                }
            }
        }
    }
}
