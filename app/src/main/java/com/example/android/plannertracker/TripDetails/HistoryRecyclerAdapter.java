package com.example.android.plannertracker.TripDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.plannertracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private ArrayList<HistoryList> historyLists;
    private HistoryList historyList;
    DatabaseReference databaseReference;
    private Context context;
    FirebaseUser fu;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String uid;


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
        holder.startPoint.setText(historyList.getStartPlace());
        holder.endPoint.setText(historyList.getEndPlace());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                ////////////////////////////DELETE HERE
                int positionOfItem = holder.getAdapterPosition();
                showDialogForHistory(positionOfItem);


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("qwert","clicked on item");
                Intent toStaticMapIntent = new Intent(context, StaticMap.class);
                toStaticMapIntent.putExtra("start",historyList.getStartPlace());
                toStaticMapIntent.putExtra("end",historyList.getEndPlace());
                context.startActivity(toStaticMapIntent);
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
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        uid=mUser.getUid();


        databaseReference.child(uid).child("Trip History").child(id).removeValue();
        Log.i("zzzz", "removeItem: " + id);
        Log.i("xxxx", "removeItem: " + uid);
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
        TextView startPoint;
        TextView endPoint;
        CheckBox checkBox;
        ImageView mapImage;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkFinished);
            trip = itemView.findViewById(R.id.tripHome);
            mapImage = itemView.findViewById(R.id.mapIconID);
            startPoint = itemView.findViewById(R.id.source);
            endPoint = itemView.findViewById(R.id.destination);
        }
    }


}
