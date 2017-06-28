package com.example.nikita.cybbet_client;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class GamesAdapter extends ArrayAdapter<GameInfo> {

    private String LOG_TAG = this.getClass().getSimpleName();

    Context ctx;


    public GamesAdapter(Context context, List<GameInfo> games) {
        super(context, 0, games);
        ctx = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        GameInfo gameInfo = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gameslist_item, parent, false);
        }

        ImageView gameLogoImageView = (ImageView) convertView.findViewById(R.id.games_list_logo_imageView);
        TextView gameTitleTextView = (TextView) convertView.findViewById(R.id.games_list_textView);

        assert gameInfo != null;
        Log.d(LOG_TAG, gameInfo.getTitle());
        gameTitleTextView.setText(gameInfo.getTitle());
        gameLogoImageView.setImageResource(gameInfo.getLogoId());

        return convertView;
    }
}
