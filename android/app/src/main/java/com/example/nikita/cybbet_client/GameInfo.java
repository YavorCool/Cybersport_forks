package com.example.nikita.cybbet_client;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nikita on 06.06.17.
 */

public class GameInfo {
    private String title;
    private int logoId;
    private int tag;

    public GameInfo(String title, int logoId, int tag) {
        this.title = title;
        this.logoId = logoId;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
