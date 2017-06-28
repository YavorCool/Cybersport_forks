package com.example.nikita.cybbet_client;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    public String teams;
    public String datetime;
    public String[] fork;
    public String[] bets;
    public String[] urls;

    ListView betsListView;
    TextView forkProfitTextView;
    TextView forkAmountTextView;
    TextView forkCoefsTextView;
    TextView matchSummaryTextView;
    TextView datetimeTextView;

    ArrayAdapter<String> betsAdapter;
    List<String> betsList;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        teams = getActivity().getIntent().getStringExtra("teams");
        datetime = getActivity().getIntent().getStringExtra("datetime");
        fork = getActivity().getIntent().getStringArrayExtra("fork");
        bets = getActivity().getIntent().getStringArrayExtra("bets");
        urls = getActivity().getIntent().getStringArrayExtra("urls");


        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        matchSummaryTextView = (TextView) rootView.findViewById(R.id.match_title_textView);
        forkProfitTextView = (TextView) rootView.findViewById(R.id.fork_profit_textView);
        forkAmountTextView = (TextView) rootView.findViewById(R.id.fork_amount_textView);
        forkCoefsTextView = (TextView) rootView.findViewById(R.id.fork_coefs_textView);
        datetimeTextView = (TextView) rootView.findViewById(R.id.dateTime_TextView);

        matchSummaryTextView.setText(teams);
        datetimeTextView.setText(datetime);

        if(fork != null) {
            forkProfitTextView.setText(fork[0]);
            forkAmountTextView.setText(fork[1] + "\n" + fork[2]);
            forkCoefsTextView.setText(fork[3] + "\n" + fork[4]);
        }



        betsListView = (ListView) rootView.findViewById(R.id.bets_listView);
        if(bets != null) {
            betsList = new ArrayList<>(Arrays.asList(bets));

            Log.d("_", betsList.get(0));

            betsAdapter = new ArrayAdapter<>(
                    getActivity(),
                    R.layout.bets_list_item,
                    R.id.bet_textView,
                    betsList
            );

            betsListView.setAdapter(betsAdapter);
            betsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String baseUrl = "http://game-tournaments.com";
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + urls[position]));
                    startActivity(browserIntent);
                }
            });
        }
        return rootView;
    }
}
