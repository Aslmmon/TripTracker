package com.example.android.plannertracker.TripDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.plannertracker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private ArrayList<HistoryList> historyLists;
    private HistoryList historyList;
    DatabaseReference databaseReference;
    private Context context;

    public HistoryRecyclerAdapter() {
    }

    HistoryRecyclerAdapter(Context context, ArrayList<HistoryList> historyLists) {
        this.mInflater = LayoutInflater.from(context);
        this.historyLists = historyLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_layout_single_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        historyList = historyLists.get(position);
        holder.trip.setText(historyList.getTripName());


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                ////////////////////////////DELETE HERE
                int positionOfItem = holder.getAdapterPosition();
                showDialogForHistory(positionOfItem);
                Log.v("a7a", historyList.getTripName());

            }
        });

    }

    private void showDialogForHistory(final int adapterpostion) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                removeItem(adapterpostion);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
            }
        }).setTitle("Remove item").setMessage("Remove item from history ?").create().show();
    }

    private void removeItem(int adapterPosition) {
        String id = historyLists.get(adapterPosition).getId();
        databaseReference = FirebaseDatabase.getInstance().getReference("Trip History");

        Log.i("zzzz", "removeItem: " + id);
        databaseReference.child(id).removeValue();
        historyLists.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, historyLists.size());
        Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return historyLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trip;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkFinished);
            trip = itemView.findViewById(R.id.tripHome);
        }
    }


}
