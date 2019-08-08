package com.example.wildlifemonitoringsystem.api;

import com.example.wildlifemonitoringsystem.model.AllAnimals;
import com.example.wildlifemonitoringsystem.model.AnimalDetails;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("api") //Your end point is here
    Call<List<AllAnimals>> getAllAnimals();

    @GET("api/{id}") //Your end point is here
    Call<AnimalDetails> getDetails(@Path("id") String id);



}
