package com.example.nikita.cybbet_client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class MatchesAdapter extends ArrayAdapter<MatchInfo> {
    private Context ctx;


    public MatchesAdapter(Context context, List<MatchInfo> matches) {
        super(context, 0, matches);
        ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        MatchInfo matchInfo = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.matches_list_item, parent, false);
        }
        TextView matchSummaryTextView = (TextView) convertView.findViewById(R.id.match_summary_textView);
        String fork = "-";

        assert matchInfo != null;
        if(matchInfo.getFork() != null){
            fork = "+";
        }
        matchSummaryTextView.setText(matchInfo.getTeams() + "\nВилка: " + fork);
        return convertView;
    }
}
