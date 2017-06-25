package com.drowsyatmidnight.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created by haint on 24/06/2017.
 */

public class ApiResponse implements Parcelable {
    private JsonObject response;
    private String status;

    protected ApiResponse(Parcel in) {
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApiResponse> CREATOR = new Creator<ApiResponse>() {
        @Override
        public ApiResponse createFromParcel(Parcel in) {
            return new ApiResponse(in);
        }

        @Override
        public ApiResponse[] newArray(int size) {
            return new ApiResponse[size];
        }
    };

    public JsonObject getResponse() {
        if (response != null)
            return response;
        else return new JsonObject();
    }

    public String getStatus() {
        return status;
    }
}
