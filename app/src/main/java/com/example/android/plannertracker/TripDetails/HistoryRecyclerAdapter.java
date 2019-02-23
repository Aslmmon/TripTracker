package com.example.android.plannertracker.TripDetails;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.android.plannertracker.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private ArrayList<HistoryList> historyLists;
    private HistoryList historyList ;

    public HistoryRecyclerAdapter() {
    }

    HistoryRecyclerAdapter(Context context , ArrayList<HistoryList> historyLists )
    {
        this.mInflater = LayoutInflater.from(context);
        this.historyLists = historyLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_layout_single_view, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        historyList = historyLists.get(position);
        holder.trip.setText(historyList.getTripName());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                ////////////////////////////DELETE HERE

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
{
    TextView trip ;
    CheckBox checkBox ;
    public  ViewHolder(View itemView)
    {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkFinished);
        trip = itemView.findViewById(R.id.tripHome);
    }
}






}
