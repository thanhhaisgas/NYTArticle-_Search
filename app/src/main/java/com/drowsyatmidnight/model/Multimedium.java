package com.drowsyatmidnight.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haint on 24/06/2017.
 */

public class Multimedium implements Parcelable {
    private int width;
    private String url;
    private int height;
    private static final String imgUrl = "http://www.nytimes.com/";

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrl() {
        return imgUrl+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static String getImgUrl() {
        return imgUrl;
    }

    protected Multimedium(Parcel in) {
        width = in.readInt();
        url = in.readString();
        height = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeString(imgUrl+url);
        dest.writeInt(height);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Multimedium> CREATOR = new Creator<Multimedium>() {
        @Override
        public Multimedium createFromParcel(Parcel in) {
            return new Multimedium(in);
        }

        @Override
        public Multimedium[] newArray(int size) {
            return new Multimedium[size];
        }
    };
}
