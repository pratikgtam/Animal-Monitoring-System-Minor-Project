package com.example.wildlifemonitoringsystem.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.wildlifemonitoringsystem.R;
import com.example.wildlifemonitoringsystem.adapter.AnimalAdapter;
import com.example.wildlifemonitoringsystem.api.ApiClient;
import com.example.wildlifemonitoringsystem.api.ApiInterface;
import com.example.wildlifemonitoringsystem.model.AllAnimals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);
        floatingActionButton = findViewById(R.id.fab);
        loadFeed();
        toolbar();

        floatingActionButton.setOnClickListener(view -> {
            loadFeed();
            //startActivity(new Intent(MainActivity.this, GraphActivity.class));
        });
    }


    private void loadFeed() {
        loading();
        try {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//            HashMap<String, String> headerMap = new HashMap<>();
//            headerMap.put("access-token", getString(R.string.access_token));

            Call<List<AllAnimals>> call = apiInterface.getAllAnimals();
            call.enqueue(new Callback<List<AllAnimals>>() {

                @Override
                public void onResponse(@NonNull Call<List<AllAnimals>> call, @NonNull Response<List<AllAnimals>> response) {
                    assert response.body() != null;
                    generateFeed(response.body());
                    loaded();
                }

                @Override
                public void onFailure(@NonNull Call<List<AllAnimals>> call, @NonNull Throwable t) {
                    loaded();
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            //  Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            loaded();
        }
    }

    private void generateFeed(List<AllAnimals> body) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AnimalAdapter(body, getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }

    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Animal Monitoring System");
        setSupportActionBar(toolbar);
    }

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void loaded() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        floatingActionButton.show();

    }


}
