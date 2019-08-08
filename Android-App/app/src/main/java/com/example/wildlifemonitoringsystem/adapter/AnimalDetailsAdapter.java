package com.example.wildlifemonitoringsystem.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wildlifemonitoringsystem.R;
import com.example.wildlifemonitoringsystem.activity.DetailsActivity;
import com.example.wildlifemonitoringsystem.activity.MainActivity;
import com.example.wildlifemonitoringsystem.activity.MapsActivity;
import com.example.wildlifemonitoringsystem.model.Display;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.wildlifemonitoringsystem.utils.Constants.DEGREE_CELCIUS;


public class AnimalDetailsAdapter extends RecyclerView.Adapter<AnimalDetailsAdapter.MyViewHolder> {
    private static final String TAG = "AnimalAdapter";
    private Context mcontex;
    private List<Display> displays = new ArrayList<>();
    private boolean condition = true;
    private double minRange, maxRange;
    private String unit;

    public AnimalDetailsAdapter(List<Display> displays, Context mcontex, double minRange, double maxRange, String unit) {
        this.displays = displays;
        this.mcontex = mcontex;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.unit = unit;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: msg: LayoutInflator");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_details, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: msg: OnBindViewHolderCalled");
        //  private TextView value, timestramp, condition;
        //
        //        private ImageView imageViewCondition;
        Display display = displays.get(position);

        holder.value.setText("" + display.getValue() + unit);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(String.valueOf(display.getTimestramp())));
        String date = DateFormat.format("dd-MMM hh:mm:ss a", calendar).toString();
        holder.timestramp.setText(date);
//        Double doubleTimestramp;
//
//        doubleTimestramp = display.getTimestramp() / 1000;
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
//            displayTime = df.format(doubleTimestramp) + " days ago";
//        }
//        holder.timestramp.setText(displayTime);
        if (minRange != maxRange) {
            if (display.getValue() < minRange || display.getValue() > maxRange) {
                holder.condition.setText("Not Normal");
                holder.condition.setTextColor(mcontex.getResources().getColor(R.color.red));
                holder.imageViewCondition.setImageDrawable(mcontex.getResources().getDrawable(R.drawable.ic_cancel_black_24dp));

            }
        } else {
            holder.condition.setVisibility(View.GONE);
            holder.imageViewCondition.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return displays.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MyViewHolder";

        private TextView value, timestramp, condition;

        private ImageView imageViewCondition;

        MyViewHolder(View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.tv_details_particular);
            timestramp = itemView.findViewById(R.id.tv_time);
            condition = itemView.findViewById(R.id.tv_condition);
            imageViewCondition = itemView.findViewById(R.id.iv_condition);


        }

    }
}