package com.example.android.plannertracker.TripDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.plannertracker.R;
import com.example.android.plannertracker.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.MyViewHolder> {
    public static final String CLICKED_ITEM_POSITION = "ClickedItemPoisiton";


    private Context context;
    private ArrayList<TrackerInformation> trackerInformations;
    private List<String> notes;
    NoteClass noteClass;
    TrackerInformation trackerInformation;



    DatabaseReference databaseReference,databaseReferenceTwo;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    SharedPreferences sharedPreferences;


    String uid;

    public ArrayAdapter(Context context, ArrayList<TrackerInformation> trackerInformations) {
        this.context = context;
        this.trackerInformations = trackerInformations;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.my_list_home, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        trackerInformation = trackerInformations.get(position);
        holder.trip.setText(trackerInformation.getTripName());
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopUp(holder);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        holder.checkBoxFinishedNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Toast.makeText(context, "Remove Note", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void saveToFinishedDatabase(int position) {
       String id = databaseReferenceTwo.push().getKey();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
         uid=mUser.getUid();

       // Log.v("xxxx",id);

        HistoryList historyList = new HistoryList(trackerInformations.get(position).getTripName(), trackerInformations.get(position).getStartPosition(), trackerInformations.get(position).getDestination(), id);
        databaseReferenceTwo.child(uid).child("Trip History").child(id).setValue(historyList);
    }

    public void moveToHistory(int position) {

        HistoryList historyList = new HistoryList(trackerInformations.get(position).getTripName(), trackerInformations.get(position).getStartPosition(), trackerInformations.get(position).getDestination(), trackerInformations.get(position).getId());
        Toast.makeText(context, trackerInformations.get(position).getStartPosition(), Toast.LENGTH_SHORT).show();



    }


    private void addPopUp(@NonNull final MyViewHolder holder) {
        PopupMenu popupMenu = new PopupMenu(context, holder.buttonView);
        popupMenu.inflate(R.menu.item_lists);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove:
                        int positionOfItem = holder.getAdapterPosition();
                        showDialog(positionOfItem);
                        break;
                    case R.id.start:
                        Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.view:
                        Toast.makeText(context, "view", Toast.LENGTH_SHORT).show();
                        goToDetailActivity(holder.getAdapterPosition());
                        break;
                    case R.id.addNote:
                        Toast.makeText(context, "Add Note", Toast.LENGTH_SHORT).show();
                        goToAddNoteActivity(holder.getAdapterPosition());
                        break;
                    case R.id.edit:
                        Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                        goToEditActivity(holder.getAdapterPosition());
                        break;
                }

                return false;
            }
        });
        popupMenu.show();
    }

    private void goToAddNoteActivity(int adapterPosition) {
        Intent intent = new Intent(context,AddNote.class);
        intent.putExtra("id",trackerInformations.get(adapterPosition).getId());
        context.startActivity(intent);
    }

    private void goToEditActivity(int adapterPosition) {
        Intent goToEdit = new Intent(context, EditActtivity.class);
        goToEdit.putExtra("tripName", trackerInformations.get(adapterPosition).getTripName());
        goToEdit.putExtra("start", trackerInformations.get(adapterPosition).getStartPosition());
        goToEdit.putExtra("end", trackerInformations.get(adapterPosition).getDestination());
        goToEdit.putExtra("date", trackerInformations.get(adapterPosition).getDate());
        goToEdit.putExtra("time", trackerInformations.get(adapterPosition).getTime());
        goToEdit.putExtra("id", trackerInformations.get(adapterPosition).getId());
        goToEdit.putExtra("tripType", trackerInformations.get(adapterPosition).getTripType());
      //  goToEdit.putExtra("tripType", trackerInformation.getTripType());
        Log.i("trace", "ID : " + trackerInformation.getId());
        context.startActivity(goToEdit);
    }


    private void showDialogForHistory(final int adapterpostion) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReferenceTwo = FirebaseDatabase.getInstance().getReference("users");
                saveToFinishedDatabase(adapterpostion);
                removeItem(adapterpostion);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
            }
        }).setTitle("Finished Trip").setMessage("Have you finished this trip ? ").create().show();
    }

    private void showDialog(final int adapterpostion) {
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
        }).setTitle("Remove item").setMessage("Are you sure ? ").create().show();
    }

    private void goToDetailActivity(int adapterPosition) {
        Intent goToDetail = new Intent(context, DetailActivity.class);
        goToDetail.putExtra("tripName", trackerInformations.get(adapterPosition).getTripName());
        goToDetail.putExtra("start", trackerInformations.get(adapterPosition).getStartPosition());
        goToDetail.putExtra("end", trackerInformations.get(adapterPosition).getDestination());
        goToDetail.putExtra("date", trackerInformations.get(adapterPosition).getDate());
        goToDetail.putExtra("time", trackerInformations.get(adapterPosition).getTime());
        goToDetail.putExtra("tripType", trackerInformations.get(adapterPosition).getTripType());
        //goToDetail.putExtra("typeOftrip",trackerInformations.get(adapterPosition).getTripName());
        context.startActivity(goToDetail);

        Log.i("trace", trackerInformations.get(adapterPosition).getDestination());
        Log.i("trace", trackerInformation.getDestination());

    }

    private void removeItem(int adapterPosition) {
        // trackerInformation = new TrackerInformation(trackerInformation.getId());
        String id = trackerInformations.get(adapterPosition).getId();
        databaseReference = FirebaseDatabase.getInstance().getReference("Trip Data");
        //  String id = trackerInformation.getId();
        Log.i("trace", "removeItem: " + id);
        databaseReference.child(id).removeValue();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Log.i("magdy", "removeItem: " + id);
        databaseReference.child(uid).child(id).removeValue();
        Log.v("omar",id);
        Log.v("omar2",uid);
        trackerInformations.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, trackerInformations.size());
        Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
    }



    @Override
    public int getItemCount() {
        return trackerInformations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView start, end, date, time, buttonView, trip,noteTaken;
        LinearLayout myLayout;
        CheckBox checkBox,checkBoxFinishedNotes;

        public MyViewHolder(View itemView) {
            super(itemView);
            trip = itemView.findViewById(R.id.tripHome);
            buttonView = itemView.findViewById(R.id.textViewOptions);
            myLayout = itemView.findViewById(R.id.Linear);
            checkBox = itemView.findViewById(R.id.checkFinished);
            noteTaken = itemView.findViewById(R.id.tripHomeNotes);
            checkBoxFinishedNotes = itemView.findViewById(R.id.checkFinishedNotes);
        }
    }
}
