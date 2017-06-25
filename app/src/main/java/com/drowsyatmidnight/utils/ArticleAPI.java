package com.drowsyatmidnight.utils;

import com.drowsyatmidnight.model.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by haint on 24/06/2017.
 */

public interface ArticleAPI {
    @GET("articlesearch.json")
    Call<SearchResult> search(@QueryMap(encoded = true) Map<String,String> options);
}
