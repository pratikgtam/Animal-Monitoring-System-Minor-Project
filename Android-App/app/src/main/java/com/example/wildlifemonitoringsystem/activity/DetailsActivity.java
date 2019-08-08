package com.example.wildlifemonitoringsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wildlifemonitoringsystem.R;
import com.example.wildlifemonitoringsystem.adapter.AnimalAdapter;
import com.example.wildlifemonitoringsystem.adapter.AnimalDetailsAdapter;
import com.example.wildlifemonitoringsystem.api.ApiClient;
import com.example.wildlifemonitoringsystem.api.ApiInterface;
import com.example.wildlifemonitoringsystem.model.AllAnimals;
import com.example.wildlifemonitoringsystem.model.AnimalDetails;
import com.example.wildlifemonitoringsystem.model.BodyTemprAll;
import com.example.wildlifemonitoringsystem.model.Display;
import com.example.wildlifemonitoringsystem.model.GeoLocationAll;
import com.example.wildlifemonitoringsystem.model.HeartBeatAll;
import com.example.wildlifemonitoringsystem.model.SurrTemprAll;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wildlifemonitoringsystem.utils.Constants.DEGREE_CELCIUS;

public class DetailsActivity extends AppCompatActivity {
    String[] all = {"All Parameter", "Pulse Rate", "Body Temperature", "Surrounding Temperature", "Humidity"};
    String[] max = {"10", "20", "50", "100", "500", "All"};

    private RecyclerView recyclerViewPulseRate, recyclerViewBodyTempr, recyclerViewSurrTempr, recyclerViewRelativeHumidiy;
    private TextView textViewRangePulseRate, textViewBodyTemprRange;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private Spinner spinnerall, spinnerMax;
    private LinearLayout linearLayoutPulseRate, linearLayoutSurroundingTemperature, linearLayoutBodyTemperature, linearLayoutHumidity;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        recyclerViewPulseRate = findViewById(R.id.rv_pulse_rate);
        recyclerViewBodyTempr = findViewById(R.id.rv_body_tempr);
        recyclerViewSurrTempr = findViewById(R.id.rv_surr_tempr);
        textViewBodyTemprRange = findViewById(R.id.tv_range_body_tempr);
        textViewRangePulseRate = findViewById(R.id.tv_range_pulse_rate);
        recyclerViewRelativeHumidiy = findViewById(R.id.rv_relative_humidity);
        linearLayoutPulseRate = findViewById(R.id.ll_pulse_rate);
        linearLayoutBodyTemperature = findViewById(R.id.ll_body_tempr);
        linearLayoutHumidity = findViewById(R.id.ll_relative_humidity);
        linearLayoutSurroundingTemperature = findViewById(R.id.ll_surr_tempr);
        floatingActionButton = findViewById(R.id.fab);


        spinnerall = findViewById(R.id.spinner_all);
        spinnerMax = findViewById(R.id.spinner_max);
        progressBar = findViewById(R.id.progressbar);
        linearLayout = findViewById(R.id.layout_main);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, all);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerall.setAdapter(aa);


        spinnerall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(getApplicationContext(), all[i], Toast.LENGTH_LONG).show();
                //"All Parameter", "Pulse Rate", "Body Temperature", "Surrounding Temperature", "Humidity"
                switch (i) {
                    case 0:
                        showAll();
                        break;
                    case 1:
                        showlinearLayoutPulseRateOnly();
                        break;
                    case 2:
                        showlinearLayoutBodyTemperatureOnly();
                        break;
                    case 3:
                        showlinearLayoutSurroundingTemperatureOnly();
                        break;
                    case 4:
                        showlinearLayoutHumidityOnly();
                        break;
                    default:
                        //do nothing
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // String category = bundle.getString("CATEGORY");

            loadFeed(bundle.getString("ID"));
            floatingActionButton.setOnClickListener(view -> {
                loadFeed(bundle.getString("ID"));

            });
            findViewById(R.id.b_graph).setOnClickListener(view -> {
                Bundle args = new Bundle();
                // args.putParcelableArrayList("Topic", (ArrayList<? extends Parcelable>) topic);
                Intent intent = new Intent(DetailsActivity.this, GraphActivity.class);
                intent.putExtras(args);
                intent.putExtra("ID", bundle.getString("ID"));
                startActivity(intent);
            });

        }
        toolbar();

    }

    private void loadFeed(String id) {
        loading();
        try {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//            HashMap<String, String> headerMap = new HashMap<>();
//            headerMap.put("access-token", getString(R.string.access_token));

            Call<AnimalDetails> call = apiInterface.getDetails(id);
            call.enqueue(new Callback<AnimalDetails>() {

                @Override
                public void onResponse(@NonNull Call<AnimalDetails> call, @NonNull Response<AnimalDetails> response) {
                    assert response.body() != null;
                    generateFeed(response.body());

                    loaded();
                }

                @Override
                public void onFailure(@NonNull Call<AnimalDetails> call, @NonNull Throwable t) {
                    loaded();
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            //  Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            loaded();
        }
    }

    private void generateFeed(AnimalDetails body) {


        ArrayAdapter max1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, max);
        max1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMax.setAdapter(max1);

        spinnerMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(getApplicationContext(), max[i], Toast.LENGTH_LONG).show();
                //{"10", "20", "50", "100", "500", "All"};
                int number = 0;
                switch (i) {
                    case 0:
                        number = 10;
                        setRecyclerView(body, number);
                        break;
                    case 1:
                        number = 20;
                        setRecyclerView(body, number);
                        break;


                    case 2:
                        number = 50;
                        setRecyclerView(body, number);
                        break;


                    case 3:
                        number = 100;
                        setRecyclerView(body, number);
                        break;


                    case 4:
                        number = 500;
                        setRecyclerView(body, number);
                        break;


                    case 5:
                        number = 1000;
                        setRecyclerView(body, number);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        findViewById(R.id.ll_spinner).setVisibility(View.GONE);
    }

    private void loaded() {
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        findViewById(R.id.ll_spinner).setVisibility(View.VISIBLE);
        floatingActionButton.show();

    }

    //linearLayoutPulseRate, linearLayoutSurroundingTemperature, linearLayoutBodyTemperature, linearLayoutHumidity;
    private void showlinearLayoutPulseRateOnly() {
        linearLayoutSurroundingTemperature.setVisibility(View.GONE);
        linearLayoutBodyTemperature.setVisibility(View.GONE);
        linearLayoutHumidity.setVisibility(View.GONE);
        linearLayoutPulseRate.setVisibility(View.VISIBLE);
    }

    private void showlinearLayoutSurroundingTemperatureOnly() {
        linearLayoutSurroundingTemperature.setVisibility(View.VISIBLE);
        linearLayoutBodyTemperature.setVisibility(View.GONE);
        linearLayoutHumidity.setVisibility(View.GONE);
        linearLayoutPulseRate.setVisibility(View.GONE);
    }

    private void showlinearLayoutBodyTemperatureOnly() {
        linearLayoutSurroundingTemperature.setVisibility(View.GONE);
        linearLayoutBodyTemperature.setVisibility(View.VISIBLE);
        linearLayoutHumidity.setVisibility(View.GONE);
        linearLayoutPulseRate.setVisibility(View.GONE);
    }

    private void showlinearLayoutHumidityOnly() {
        linearLayoutSurroundingTemperature.setVisibility(View.GONE);
        linearLayoutBodyTemperature.setVisibility(View.GONE);
        linearLayoutHumidity.setVisibility(View.VISIBLE);
        linearLayoutPulseRate.setVisibility(View.GONE);
    }

    private void showAll() {
        linearLayoutSurroundingTemperature.setVisibility(View.VISIBLE);
        linearLayoutBodyTemperature.setVisibility(View.VISIBLE);
        linearLayoutHumidity.setVisibility(View.VISIBLE);
        linearLayoutPulseRate.setVisibility(View.VISIBLE);
    }

    private void setRecyclerView(AnimalDetails body, int mainSize) {
        int sizeBodyTempr = mainSize;
        int sizeHeartBeat = mainSize;
        int sizeSurrTempr = mainSize;

        List<BodyTemprAll> bodyTemprAlls = new ArrayList<>(body.getBodyTemprAll());
        List<GeoLocationAll> geoLocationAlls = new ArrayList<>(body.getGeoLocationAll());
        List<HeartBeatAll> heartBeatAlls = new ArrayList<>(body.getHeartBeatAll());
        List<SurrTemprAll> surrTemprAlls = new ArrayList<>(body.getSurrTemprAll());

        Collections.reverse(bodyTemprAlls);
        Collections.reverse(heartBeatAlls);
        Collections.reverse(surrTemprAlls);

        List<Display> displays = new ArrayList<>();
        textViewRangePulseRate.setText("Normal Range: " + body.getHeartBeatRange().get(0) + " bpm - " + body.getHeartBeatRange().get(1) + " bpm ");
        textViewBodyTemprRange.setText("Normal Range: " + body.getBodyTemprRange().get(0) + DEGREE_CELCIUS + " - " + body.getBodyTemprRange().get(1) + DEGREE_CELCIUS);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        if (mainSize == 1000 || mainSize > heartBeatAlls.size()) {
            sizeHeartBeat = heartBeatAlls.size();

        }

        for (int i = 0; i < sizeHeartBeat; i++) {
            displays.add(new Display(heartBeatAlls.get(i).getHeartBeat(), heartBeatAlls.get(i).getTimestramp()));
        }
        recyclerViewPulseRate.setLayoutManager(layoutManager);
        recyclerViewPulseRate.setAdapter(new AnimalDetailsAdapter(displays, getApplicationContext(), body.getHeartBeatRange().get(0), body.getHeartBeatRange().get(1), " bpm"));
        recyclerViewPulseRate.smoothScrollToPosition(0);
        recyclerViewPulseRate.addItemDecoration(new DividerItemDecoration(recyclerViewPulseRate.getContext(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        displays = new ArrayList<>();

        if (mainSize == 1000 || mainSize > bodyTemprAlls.size()) {
            sizeBodyTempr = bodyTemprAlls.size();

        }
        for (int i = 0; i < sizeBodyTempr; i++) {
            displays.add(new Display(bodyTemprAlls.get(i).getBodyTempr(), bodyTemprAlls.get(i).getTimestramp()));
        }
        recyclerViewBodyTempr.setLayoutManager(layoutManager1);
        recyclerViewBodyTempr.setAdapter(new AnimalDetailsAdapter(displays, getApplicationContext(), body.getBodyTemprRange().get(0), body.getBodyTemprRange().get(1), DEGREE_CELCIUS));
        recyclerViewBodyTempr.smoothScrollToPosition(0);
        recyclerViewBodyTempr.addItemDecoration(new DividerItemDecoration(recyclerViewPulseRate.getContext(), DividerItemDecoration.VERTICAL));


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        displays = new ArrayList<>();
        if (mainSize == 1000 || mainSize > surrTemprAlls.size()) {
            sizeSurrTempr = surrTemprAlls.size();

        }
        for (int i = 0; i < sizeSurrTempr; i++) {
            displays.add(new Display(surrTemprAlls.get(i).getSurrTempr(), surrTemprAlls.get(i).getTimestramp()));
        }
        recyclerViewSurrTempr.setLayoutManager(layoutManager2);
        recyclerViewSurrTempr.setAdapter(new AnimalDetailsAdapter(displays, getApplicationContext(), 0, 0, DEGREE_CELCIUS));
        recyclerViewSurrTempr.smoothScrollToPosition(0);
        recyclerViewSurrTempr.addItemDecoration(new DividerItemDecoration(recyclerViewPulseRate.getContext(), DividerItemDecoration.VERTICAL));


        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext());
        displays = new ArrayList<>();
        for (int i = 0; i < sizeSurrTempr; i++) {
            displays.add(new Display(surrTemprAlls.get(i).getHumidity(), surrTemprAlls.get(i).getTimestramp()));
        }
        recyclerViewRelativeHumidiy.setLayoutManager(layoutManager3);
        recyclerViewRelativeHumidiy.setAdapter(new AnimalDetailsAdapter(displays, getApplicationContext(), 0, 0, "%"));
        recyclerViewRelativeHumidiy.smoothScrollToPosition(0);
        recyclerViewRelativeHumidiy.addItemDecoration(new DividerItemDecoration(recyclerViewPulseRate.getContext(), DividerItemDecoration.VERTICAL));
    }

}
