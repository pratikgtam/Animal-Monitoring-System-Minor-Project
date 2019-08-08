package com.example.wildlifemonitoringsystem.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wildlifemonitoringsystem.R;
import com.example.wildlifemonitoringsystem.api.ApiClient;
import com.example.wildlifemonitoringsystem.api.ApiInterface;
import com.example.wildlifemonitoringsystem.model.AnimalDetails;
import com.example.wildlifemonitoringsystem.model.BodyTemprAll;
import com.example.wildlifemonitoringsystem.model.GeoLocationAll;
import com.example.wildlifemonitoringsystem.model.HeartBeatAll;
import com.example.wildlifemonitoringsystem.model.SurrTemprAll;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphActivity extends AppCompatActivity {
    private static final String TAG = "GraphActivity";
    private int date;
    private String dayAndMonth;
    private GraphView graph;
    private GraphView graphRecent;
    private TextView textViewData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graph = findViewById(R.id.graph);
        graphRecent = findViewById(R.id.graphRecent);
        textViewData = findViewById(R.id.tv_date);
        Bundle bundle = getIntent().getExtras();
        toolbar();
        if (bundle != null) {
            // String category = bundle.getString("CATEGORY");

            loadFeed(bundle.getString("ID"));

        }

    }

    private void loadFeed(String id) {
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
                }

                @Override
                public void onFailure(@NonNull Call<AnimalDetails> call, @NonNull Throwable t) {
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            //  Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateFeed(AnimalDetails body) {
        Calendar calendar = Calendar.getInstance();

        List<BodyTemprAll> bodyTemprAlls = new ArrayList<>(body.getBodyTemprAll());
        List<HeartBeatAll> heartBeatAlls = new ArrayList<>(body.getHeartBeatAll());
        List<SurrTemprAll> surrTemprAlls = new ArrayList<>(body.getSurrTemprAll());

        Collections.reverse(bodyTemprAlls);
        Collections.reverse(heartBeatAlls);
        Collections.reverse(surrTemprAlls);

        List<Integer> graphXBodyTemperature = new ArrayList<>();
        List<Double> graphYBodyTemperature = new ArrayList<>();
        List<Integer> graphXHeartBeat = new ArrayList<>();
        List<Double> graphYHeartBeat = new ArrayList<>();
        List<Integer> graphXSurrTempr = new ArrayList<>();
        List<Double> graphYSurrTempr = new ArrayList<>();

        List<Double> recentXaxis = new ArrayList<>();
        List<Double> recentYaxis = new ArrayList<>();

        int recentbodytemprSize = 50;
        int recentSurrTemprSize = 50;
        int recentHeartBeatSize = 50;
        if (bodyTemprAlls.size() < 50) {
            recentbodytemprSize = bodyTemprAlls.size();
        }
        if (surrTemprAlls.size() < 50) {
            recentSurrTemprSize = surrTemprAlls.size();
        }
        if (heartBeatAlls.size() < 50) {
            recentHeartBeatSize = heartBeatAlls.size();
        }
        recentXaxis = new ArrayList<>();
        recentYaxis = new ArrayList<>();
        for (int i = 0; i < recentbodytemprSize; i++) {

            calendar.setTimeInMillis(Long.parseLong(String.valueOf(bodyTemprAlls.get(i).getTimestramp())));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            double time = hour + minute / 100;
            recentYaxis.add(bodyTemprAlls.get(i).getBodyTempr());
            recentXaxis.add(time);

        }
        if (recentbodytemprSize == 50) {
            setGraph(recentXaxis, recentYaxis, Color.GREEN, "Body Temperature");
        } else if (recentbodytemprSize >= 20) {
            setGraphLessData(recentXaxis, recentYaxis, Color.GREEN, "Body Temperature");

        }
        recentXaxis = new ArrayList<>();
        recentYaxis = new ArrayList<>();
        for (int i = 0; i < recentSurrTemprSize; i++) {

            calendar.setTimeInMillis(Long.parseLong(String.valueOf(surrTemprAlls.get(i).getTimestramp())));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            double time = hour + minute / 100;
            recentYaxis.add(surrTemprAlls.get(i).getSurrTempr());
            recentXaxis.add(time);
        }
        if (recentSurrTemprSize == 50) {
            setGraph(recentXaxis, recentYaxis, Color.YELLOW, "Surrounding  Temperature");
        } else if (recentSurrTemprSize >= 20) {
            setGraphLessData(recentXaxis, recentYaxis, Color.YELLOW, "Surrounding Temperature");

        }
        recentXaxis = new ArrayList<>();
        recentYaxis = new ArrayList<>();
        for (int i = 0; i < recentHeartBeatSize; i++) {

            calendar.setTimeInMillis(Long.parseLong(String.valueOf(heartBeatAlls.get(i).getTimestramp())));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            double time = hour + minute / 100;
            recentYaxis.add(heartBeatAlls.get(i).getHeartBeat());
            recentXaxis.add(time);
        }
        //private void setGraph(List<Integer> xAxis, List<Integer> yAxis, int color, String title) {
        if (recentHeartBeatSize == 50) {
            setGraph(recentXaxis, recentYaxis, Color.CYAN, "Pulse Rate");
        } else if (recentHeartBeatSize >= 20) {
            setGraphLessData(recentXaxis, recentYaxis, Color.CYAN, "Pulse Rate");

        }

        Calendar calendarCurrent = Calendar.getInstance();
        long currentMillis = calendarCurrent.getTimeInMillis();
        calendarCurrent.setTimeInMillis(currentMillis);
        int currentDay = calendar.get(Calendar.DATE);
        int bodyTemprSize = bodyTemprAlls.size() / 10;
        int heartBeatSize = heartBeatAlls.size() / 10;
        int surrTemprSize = surrTemprAlls.size() / 10;

        for (int i = 0; i < bodyTemprAlls.size(); i = i + bodyTemprSize) {
            calendar.setTimeInMillis(Long.parseLong(String.valueOf(bodyTemprAlls.get(i).getTimestramp())));
            int dayOfData = calendar.get(Calendar.DATE);

            if (i == 0) {
                date = dayOfData;
                dayAndMonth = DateFormat.format("dd-MMM", calendar).toString();

            }
            if (dayOfData == date) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                graphXBodyTemperature.add(hour);
                graphYBodyTemperature.add(bodyTemprAlls.get(i).getBodyTempr());
            }
        }

        textViewData.setText("All data of " + dayAndMonth + " in graph");


        for (int i = 0; i < heartBeatAlls.size(); i = i + heartBeatSize) {
            calendar.setTimeInMillis(Long.parseLong(String.valueOf(heartBeatAlls.get(i).getTimestramp())));
            int dayOfData = calendar.get(Calendar.DATE);

            if (i == 0) {
                date = dayOfData;
            }
            if (dayOfData == date) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                graphXHeartBeat.add(hour);
                graphYHeartBeat.add(heartBeatAlls.get(i).getHeartBeat());
            }
        }

        for (int i = 0; i < surrTemprAlls.size(); i = i + surrTemprSize) {
            calendar.setTimeInMillis(Long.parseLong(String.valueOf(surrTemprAlls.get(i).getTimestramp())));
            int dayOfData = calendar.get(Calendar.DATE);

            if (i == 0) {
                date = dayOfData;
            }
            if (dayOfData == date) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                graphXSurrTempr.add(hour);
                graphYSurrTempr.add(surrTemprAlls.get(i).getSurrTempr());
            }
        }
        if (graphYBodyTemperature.size() < 10 || graphYHeartBeat.size() < 10 || graphYSurrTempr.size() < 10) {
            graph.setVisibility(View.GONE);
            textViewData.setVisibility(View.GONE);
            findViewById(R.id.tv_time_day).setVisibility(View.GONE);
        }

        if (graphYBodyTemperature.size() > 9) {
            Collections.sort(graphYBodyTemperature);
            Collections.sort(graphXBodyTemperature);
            LineGraphSeries<DataPoint> seriesPulseRat = new LineGraphSeries<>
                    (new DataPoint[]{
                            new DataPoint(graphXBodyTemperature.get(0), graphYBodyTemperature.get(0)),
                            new DataPoint(graphXBodyTemperature.get(1), graphYBodyTemperature.get(1)),
                            new DataPoint(graphXBodyTemperature.get(2), graphYBodyTemperature.get(2)),
                            new DataPoint(graphXBodyTemperature.get(3), graphYBodyTemperature.get(3)),
                            new DataPoint(graphXBodyTemperature.get(4), graphYBodyTemperature.get(4)),
                            new DataPoint(graphXBodyTemperature.get(5), graphYBodyTemperature.get(5)),
                            new DataPoint(graphXBodyTemperature.get(6), graphYBodyTemperature.get(6)),
                            new DataPoint(graphXBodyTemperature.get(7), graphYBodyTemperature.get(7)),
                            new DataPoint(graphXBodyTemperature.get(8), graphYBodyTemperature.get(8)),
                            new DataPoint(graphXBodyTemperature.get(9), graphYBodyTemperature.get(9)),
                    });
            graph.addSeries(seriesPulseRat);
            seriesPulseRat.setColor(Color.GREEN);
            seriesPulseRat.setTitle("Body Temperature");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        }

        if (graphYHeartBeat.size() > 9) {
            Collections.sort(graphXHeartBeat);
            Collections.sort(graphYHeartBeat);
            LineGraphSeries<DataPoint> seriesHeartBeat = new LineGraphSeries<>
                    (new DataPoint[]{
                            new DataPoint(graphXHeartBeat.get(0), graphYHeartBeat.get(0)),
                            new DataPoint(graphXHeartBeat.get(1), graphYHeartBeat.get(1)),
                            new DataPoint(graphXHeartBeat.get(2), graphYHeartBeat.get(2)),
                            new DataPoint(graphXHeartBeat.get(3), graphYHeartBeat.get(3)),
                            new DataPoint(graphXHeartBeat.get(4), graphYHeartBeat.get(4)),
                            new DataPoint(graphXHeartBeat.get(5), graphYHeartBeat.get(5)),
                            new DataPoint(graphXHeartBeat.get(6), graphYHeartBeat.get(6)),
                            new DataPoint(graphXHeartBeat.get(7), graphYHeartBeat.get(7)),
                            new DataPoint(graphXHeartBeat.get(8), graphYHeartBeat.get(8)),
                            new DataPoint(graphXHeartBeat.get(9), graphYHeartBeat.get(9)),
                    });
            graph.addSeries(seriesHeartBeat);
            seriesHeartBeat.setColor(Color.CYAN);
            seriesHeartBeat.setTitle("Pulse Rate");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        }

        if (graphYSurrTempr.size() > 9) {
            Collections.sort(graphYSurrTempr);
            Collections.sort(graphXSurrTempr);
            LineGraphSeries<DataPoint> seriesSurrTempr = new LineGraphSeries<>

                    (new DataPoint[]{
                            new DataPoint(graphXSurrTempr.get(0), graphYSurrTempr.get(0)),
                            new DataPoint(graphXSurrTempr.get(1), graphYSurrTempr.get(1)),
                            new DataPoint(graphXSurrTempr.get(2), graphYSurrTempr.get(2)),
                            new DataPoint(graphXSurrTempr.get(3), graphYSurrTempr.get(3)),
                            new DataPoint(graphXSurrTempr.get(4), graphYSurrTempr.get(4)),
                            new DataPoint(graphXSurrTempr.get(5), graphYSurrTempr.get(5)),
                            new DataPoint(graphXSurrTempr.get(6), graphYSurrTempr.get(6)),
                            new DataPoint(graphXSurrTempr.get(7), graphYSurrTempr.get(7)),
                            new DataPoint(graphXSurrTempr.get(8), graphYSurrTempr.get(8)),
                            new DataPoint(graphXSurrTempr.get(9), graphYSurrTempr.get(9)),
                    });
            graph.addSeries(seriesSurrTempr);
            seriesSurrTempr.setColor(Color.YELLOW);
            seriesSurrTempr.setTitle("Surrounding Temperature");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        }

    }

    private void setGraph(List<Double> xAxis, List<Double> yAxis, int color, String title) {
        Collections.sort(xAxis);
        Collections.sort(yAxis);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>
                (new DataPoint[]{
                        new DataPoint(xAxis.get(0), yAxis.get(0)),
                        new DataPoint(xAxis.get(1), yAxis.get(1)),
                        new DataPoint(xAxis.get(2), yAxis.get(2)),
                        new DataPoint(xAxis.get(3), yAxis.get(3)),
                        new DataPoint(xAxis.get(4), yAxis.get(4)),
                        new DataPoint(xAxis.get(5), yAxis.get(5)),
                        new DataPoint(xAxis.get(6), yAxis.get(6)),
                        new DataPoint(xAxis.get(7), yAxis.get(7)),
                        new DataPoint(xAxis.get(8), yAxis.get(8)),
                        new DataPoint(xAxis.get(9), yAxis.get(9)),
                        new DataPoint(xAxis.get(10), yAxis.get(10)),
                        new DataPoint(xAxis.get(11), yAxis.get(11)),
                        new DataPoint(xAxis.get(12), yAxis.get(12)),
                        new DataPoint(xAxis.get(13), yAxis.get(13)),
                        new DataPoint(xAxis.get(14), yAxis.get(14)),
                        new DataPoint(xAxis.get(15), yAxis.get(15)),
                        new DataPoint(xAxis.get(16), yAxis.get(16)),
                        new DataPoint(xAxis.get(17), yAxis.get(17)),
                        new DataPoint(xAxis.get(18), yAxis.get(18)),
                        new DataPoint(xAxis.get(19), yAxis.get(19)),
                        new DataPoint(xAxis.get(20), yAxis.get(20)),
                        new DataPoint(xAxis.get(21), yAxis.get(21)),
                        new DataPoint(xAxis.get(22), yAxis.get(22)),
                        new DataPoint(xAxis.get(23), yAxis.get(23)),
                        new DataPoint(xAxis.get(24), yAxis.get(24)),
                        new DataPoint(xAxis.get(25), yAxis.get(25)),
                        new DataPoint(xAxis.get(26), yAxis.get(26)),
                        new DataPoint(xAxis.get(27), yAxis.get(27)),
                        new DataPoint(xAxis.get(28), yAxis.get(28)),
                        new DataPoint(xAxis.get(29), yAxis.get(29)),
                        new DataPoint(xAxis.get(30), yAxis.get(30)),
                        new DataPoint(xAxis.get(31), yAxis.get(31)),
                        new DataPoint(xAxis.get(32), yAxis.get(32)),
                        new DataPoint(xAxis.get(33), yAxis.get(33)),
                        new DataPoint(xAxis.get(34), yAxis.get(34)),
                        new DataPoint(xAxis.get(35), yAxis.get(35)),
                        new DataPoint(xAxis.get(36), yAxis.get(36)),
                        new DataPoint(xAxis.get(37), yAxis.get(37)),
                        new DataPoint(xAxis.get(38), yAxis.get(38)),
                        new DataPoint(xAxis.get(39), yAxis.get(39)),
                        new DataPoint(xAxis.get(40), yAxis.get(40)),
                        new DataPoint(xAxis.get(41), yAxis.get(41)),
                        new DataPoint(xAxis.get(42), yAxis.get(42)),
                        new DataPoint(xAxis.get(43), yAxis.get(43)),
                        new DataPoint(xAxis.get(44), yAxis.get(44)),
                        new DataPoint(xAxis.get(45), yAxis.get(45)),
                        new DataPoint(xAxis.get(46), yAxis.get(46)),
                        new DataPoint(xAxis.get(47), yAxis.get(47)),
                        new DataPoint(xAxis.get(48), yAxis.get(48)),
                        new DataPoint(xAxis.get(49), yAxis.get(49))
                });
        graphRecent.addSeries(series);
        series.setColor(color);
        series.setTitle(title);
        graphRecent.getLegendRenderer().setVisible(true);
        graphRecent.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphRecent.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graphRecent.getViewport().setScalableY(true); // enables vertical zooming and scrolling
    }

    private void setGraphLessData(List<Double> xAxis, List<Double> yAxis, int color, String title) {
        Collections.sort(xAxis);
        Collections.sort(yAxis);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>
                (new DataPoint[]{
                        new DataPoint(xAxis.get(0), yAxis.get(0)),
                        new DataPoint(xAxis.get(1), yAxis.get(1)),
                        new DataPoint(xAxis.get(2), yAxis.get(2)),
                        new DataPoint(xAxis.get(3), yAxis.get(3)),
                        new DataPoint(xAxis.get(4), yAxis.get(4)),
                        new DataPoint(xAxis.get(5), yAxis.get(5)),
                        new DataPoint(xAxis.get(6), yAxis.get(6)),
                        new DataPoint(xAxis.get(7), yAxis.get(7)),
                        new DataPoint(xAxis.get(8), yAxis.get(8)),
                        new DataPoint(xAxis.get(9), yAxis.get(9)),
                        new DataPoint(xAxis.get(10), yAxis.get(10)),
                        new DataPoint(xAxis.get(11), yAxis.get(11)),
                        new DataPoint(xAxis.get(12), yAxis.get(12)),
                        new DataPoint(xAxis.get(13), yAxis.get(13)),
                        new DataPoint(xAxis.get(14), yAxis.get(14)),
                        new DataPoint(xAxis.get(15), yAxis.get(15)),
                        new DataPoint(xAxis.get(16), yAxis.get(16)),
                        new DataPoint(xAxis.get(17), yAxis.get(17)),
                        new DataPoint(xAxis.get(18), yAxis.get(18)),
                        new DataPoint(xAxis.get(19), yAxis.get(19))

                });
        graphRecent.addSeries(series);
        series.setColor(color);
        series.setTitle(title);
        series.setThickness(15);

        graphRecent.getLegendRenderer().setVisible(true);
        graphRecent.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphRecent.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graphRecent.getViewport().setScalableY(true); // enables vertical zooming and scrolling
    }

    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Graph");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }
}
