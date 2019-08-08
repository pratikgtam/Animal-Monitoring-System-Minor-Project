package com.example.wildlifemonitoringsystem.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wildlifemonitoringsystem.R;
import com.example.wildlifemonitoringsystem.activity.DetailsActivity;
import com.example.wildlifemonitoringsystem.activity.MainActivity;
import com.example.wildlifemonitoringsystem.activity.MapsActivity;
import com.example.wildlifemonitoringsystem.model.AllAnimals;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.wildlifemonitoringsystem.utils.Constants.DEGREE_CELCIUS;


public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.MyViewHolder> {
    private static final String TAG = "AnimalAdapter";
    private Context mcontex;
    private List<AllAnimals> allAnimals;
    private boolean condition = true;

    public AnimalAdapter(List<AllAnimals> allAnimals, Context mcontex) {
        this.allAnimals = allAnimals;
        this.mcontex = mcontex;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: msg: LayoutInflator");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: msg: OnBindViewHolderCalled");
        AllAnimals animals = allAnimals.get(position);
//  private TextView id, condition, normalPulsseRate, measuredPulseRate, pulseRateCondition,
//                normalBodyTempr, measuredBodyTempr, bodyTemprCondition,
//                surrTempr
//                location, timstramp;
//        private ImageView map, animal, refresh;
        holder.id.setText("ID: " + animals.getAnimalId());
        holder.normalPulsseRate.setText(animals.getHeartBeatRange().get(0) + " bpm" + " - " + animals.getHeartBeatRange().get(1) + " bpm");
        holder.measuredPulseRate.setText(animals.getHeartBeat() + " bpm");
        holder.normalBodyTempr.setText(animals.getBodyTemprRange().get(0) + DEGREE_CELCIUS + " - " + animals.getBodyTemprRange().get(1) + DEGREE_CELCIUS);
        holder.measuredBodyTempr.setText(animals.getBodyTempr() + DEGREE_CELCIUS);
        holder.surrTempr.setText("Surrounding Temperature " + animals.getSurrTempr() + DEGREE_CELCIUS);
        holder.location.setText("Animal Location");
        holder.humidity.setText("Relative Humidity: " + animals.getHumidity() + " %");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(String.valueOf(animals.getTimestramp())));
        String date = DateFormat.format("dd-MMM hh:mm:ss", calendar).toString();

        holder.v.setReferenceTime(animals.getTimestramp());
//        Calendar calendar = Calendar.getInstance();
//
//        Double doubleTimestramp = Double.valueOf(calendar.getTimeInMillis());
//
//        doubleTimestramp = doubleTimestramp / 1000 - animals.getTimestramp() / 1000;
//        String displayTime = doubleTimestramp + " seconds ago";
//        DecimalFormat df = new DecimalFormat();
//        df.setMaximumFractionDigits(1);
//        if (doubleTimestramp > 60) {
//
//            doubleTimestramp = doubleTimestramp / 60;
//            displayTime = df.format(doubleTimestramp) + " minutes ago";
//        }
//        if (doubleTimestramp > 60) {
//            doubleTimestramp = doubleTimestramp / 60;
//            displayTime = df.format(doubleTimestramp) + " hours ago";
//        }
//        if (doubleTimestramp > 24) {
//            doubleTimestramp = doubleTimestramp / 24;
//            displayTime = df.format(doubleTimestramp) +
//                    " days ago";
//        }
//        holder.timstramp.setText(date);

        if (animals.getHeartBeat() < animals.getHeartBeatRange().get(0) || animals.getHeartBeat() > animals.getHeartBeatRange().get(1)) {
            holder.pulseRateCondition.setText("Not Normal");
            holder.pulseRateCondition.setTextColor(mcontex.getResources().getColor(R.color.red));
            condition = false;
        }
        if (animals.getBodyTempr() < animals.getBodyTemprRange().get(0) || animals.getBodyTempr() > animals.getBodyTemprRange().get(1)) {
            holder.bodyTemprCondition.setText("Not Normal");
            holder.bodyTemprCondition.setTextColor(mcontex.getResources().getColor(R.color.red));
            condition = false;
        }

        if (!condition) {
            holder.overallCondition.setText("Condition: Not Normal");
            holder.condition.setImageDrawable(mcontex.getResources().getDrawable(R.drawable.ic_cancel_black_24dp));
            condition = true;
        }
        Glide.with(mcontex)
                .asBitmap()
                .load(animals.getImageSrc())
                .into(holder.animal);


    }

    @Override
    public int getItemCount() {
        return allAnimals.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MyViewHolder";

        private TextView id, overallCondition, normalPulsseRate, measuredPulseRate, pulseRateCondition,
                normalBodyTempr, measuredBodyTempr, bodyTemprCondition,
                surrTempr, location, humidity;
        private ImageView map, animal, refresh, condition;
        private RelativeTimeTextView v;


        MyViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: msg: itemView");
            id = itemView.findViewById(R.id.id);
            overallCondition = itemView.findViewById(R.id.tv_condition);
            normalPulsseRate = itemView.findViewById(R.id.tv_normal_pulse_rate);
            measuredPulseRate = itemView.findViewById(R.id.tv_measured_pulse_rate);
            pulseRateCondition = itemView.findViewById(R.id.tv_pulse_rate_condition);
            normalBodyTempr = itemView.findViewById(R.id.tv_body_tempr_range);
            measuredBodyTempr = itemView.findViewById(R.id.tv_measured_body_tempr);
            bodyTemprCondition = itemView.findViewById(R.id.tv_bodyTemprCondition);
            surrTempr = itemView.findViewById(R.id.tv_surr_tempr);
            location = itemView.findViewById(R.id.tv_animal_location);
            //timstramp = itemView.findViewById(R.id.tv_timestramp);
            map = itemView.findViewById(R.id.iv_map);
            animal = itemView.findViewById(R.id.iv_animal);
            refresh = itemView.findViewById(R.id.iv_refresh);
            condition = itemView.findViewById(R.id.iv_condition);
            humidity = itemView.findViewById(R.id.tv_humidity);
            Log.d(TAG, "MyViewHolder: msg: All view are set");
            v = (RelativeTimeTextView) itemView.findViewById(R.id.timestamp);
            refresh.setOnClickListener(view -> {
                Intent intent = new Intent(mcontex, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontex.startActivity(intent);

            });
            map.setOnClickListener(view -> {
                Intent intent = new Intent(mcontex, MapsActivity.class);
                intent.putExtra("ID", allAnimals.get(getAdapterPosition()).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontex.startActivity(intent);
            });
            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                    //     List<DetailsWithTopic> topic = mFilteredTopics.get(getAdapterPosition()).getDetailsWithTopic();
                    Bundle args = new Bundle();
                    // args.putParcelableArrayList("Topic", (ArrayList<? extends Parcelable>) topic);
                    Intent intent = new Intent(mcontex, DetailsActivity.class);
                    intent.putExtras(args);
                    intent.putExtra("ID", allAnimals.get(getAdapterPosition()).getId());

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontex.startActivity(intent);


                }
            });
        }

    }


}