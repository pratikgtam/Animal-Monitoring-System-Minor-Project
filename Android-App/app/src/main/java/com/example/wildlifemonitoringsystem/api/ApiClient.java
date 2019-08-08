package com.example.wildlifemonitoringsystem.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.wildlifemonitoringsystem.utils.Constants.BASE_URL;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create(gson)).
                    build();
        return retrofit;
    }
}
