package com.drowsyatmidnight.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by haint on 24/06/2017.
 */

public class Article implements Parcelable {
    private String web_url;
    private String snippet;
    private List<Multimedium> multimedia = null;

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    protected Article(Parcel in) {
        web_url = in.readString();
        snippet = in.readString();
        multimedia = in.createTypedArrayList(Multimedium.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(web_url);
        dest.writeString(snippet);
        dest.writeTypedList(multimedia);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
