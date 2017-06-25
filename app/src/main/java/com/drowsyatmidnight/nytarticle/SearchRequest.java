package com.drowsyatmidnight.nytarticle;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haint on 24/06/2017.
 */

public class SearchRequest implements Parcelable {

    private int page = 0;
    private String Query;
    private String beginDate;
    private String order = "Newest";
    private String fq;

    protected SearchRequest(Parcel in) {
        page = in.readInt();
        Query = in.readString();
        beginDate = in.readString();
        order = in.readString();
        fq = in.readString();
    }

    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFq() {
        return fq;
    }

    public void setFq(String fq) {
        this.fq = fq;
    }

    public SearchRequest() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeString(Query);
        dest.writeString(beginDate);
        dest.writeString(order);
        dest.writeString(fq);
    }

    public Map<String, String> toQueryMap(){
        Map<String, String> options = new HashMap<>();
        if(Query != null) options.put("q", Query);
        else options.put("q", "android");
        if(beginDate != null) options.put("begin_date", beginDate);
        if(order != null) options.put("sort", order.toLowerCase());
        else options.put("sort", "newest");
        if(fq != null) options.put("fq", "new_desk:("+fq+")");
        options.put("page", String.valueOf(page));
        Log.d("filtersearch", options.toString());
        return options;
    }

    public String getNewDesk(Boolean hasArts, Boolean hasFashionAndStyle, boolean hasSports) {
        String value="";
        if(!(hasArts && hasFashionAndStyle && hasSports)) return null;
        if(hasArts) value += "\"Arts\"";
        if(hasSports) value += "\"Sports\"";
        if(hasFashionAndStyle) value += "\"Fashion & Style\"";
        return value.trim();
    }

    public void resetPage(){
        page = 0;
    }

    public void nextPage(){
        page += 1;
    }
}
