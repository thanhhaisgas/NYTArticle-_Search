package com.drowsyatmidnight.utils;

import com.drowsyatmidnight.model.ApiResponse;
import com.drowsyatmidnight.nytarticle.BuildConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haint on 24/06/2017.
 */

public class RetrofitUtils {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String URL = "https://api.nytimes.com/svc/search/v2/";
    private static final Gson GSON = new Gson();
    public static Retrofit GET(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static Interceptor apiResponseInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                ApiResponse apiResponse = GSON.fromJson(response.body().string(), ApiResponse.class);
                apiResponse.getResponse();
                return response.newBuilder()
                        .body(ResponseBody.create(JSON, GSON.toJson(apiResponse.getResponse())))
                        .build();
            }
        };
    }

    private static Interceptor apiKeyInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url()
                        .newBuilder()
                        .addQueryParameter("api-key", BuildConfig.API_KEY)
                        .build();
                request = request.newBuilder()
                        .url(url)
                        .build();
                return chain.proceed(request);
            }
        };
    }

    private static OkHttpClient client(){
        return new  OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor())
                .addInterceptor(apiResponseInterceptor())
                .build();
    }
}
