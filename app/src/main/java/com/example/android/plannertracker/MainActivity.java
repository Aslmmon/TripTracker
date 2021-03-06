package com.example.android.plannertracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.plannertracker.TripDetails.ArrayAdapter;
import com.example.android.plannertracker.TripDetails.History;
import com.example.android.plannertracker.TripDetails.NoteClass;
import com.example.android.plannertracker.TripDetails.TrackerInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TRIP_DATA = "Trip Data";
    String id;
     Context context;

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    TrackerInformation trackerInformation;
    NoteClass noteClass;
    ArrayList<TrackerInformation> trackerInformationList;
    boolean flagRound;

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Trip Data");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("trace", dataSnapshot.getKey());
                trackerInformationList.clear();
                for (DataSnapshot trackInfo : dataSnapshot.getChildren()) {
                    getTrackDetails(trackInfo);
                    trackerInformationList.add(trackerInformation);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(new ArrayAdapter(MainActivity.this, trackerInformationList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getTrackDetails(DataSnapshot trackInfo) {
        TrackerInformation values = trackInfo.getValue(TrackerInformation.class);
        trackerInformation = new TrackerInformation();
        String nameOfTrip = values.getTripName();
        String start = values.getStartPosition();
        String end = values.getDestination();
        String date = values.getDate();
        String time = values.getTime();
        String tripType = values.getTripType();
        id = values.getId();
        trackerInformation.setTripName(nameOfTrip);
        trackerInformation.setStartPosition(start);
        trackerInformation.setDestination(end);
        trackerInformation.setDate(date);
        trackerInformation.setTime(time);
        trackerInformation.setId(id);
        trackerInformation.setTripType(tripType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        intialize();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        androidStaff(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewPlan.class));
            }
        });
    }

    private void intialize() {
        recyclerView = findViewById(R.id.recycler);
        trackerInformationList = new ArrayList<>();
    }

    private void androidStaff(Toolbar toolbar)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.history)
        {
            startActivity(new Intent(MainActivity.this, History.class));


        }
        else if (id == R.id.logOut) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
