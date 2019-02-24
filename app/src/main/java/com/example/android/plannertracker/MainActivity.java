package com.example.android.plannertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.example.android.plannertracker.TripDetails.ArrayAdapter;
import com.example.android.plannertracker.TripDetails.NoteClass;
import com.example.android.plannertracker.TripDetails.TrackerInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TRIP_DATA = "Trip Data";
    String id;


    DatabaseReference databaseReference, dataBaseNote;
    RecyclerView recyclerView;
    TrackerInformation trackerInformation;
    NoteClass noteClass;
    ArrayList<TrackerInformation> trackerInformationList;
    boolean flagRound;
    List<String> notesList;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;


    @Override
    protected void onStart() {
        super.onStart();
        dataBaseNote = FirebaseDatabase.getInstance().getReference("Trip Data");
      //  databaseReference = FirebaseDatabase.getInstance().getReference("Trip Data");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
        //dataBaseNote.keepSynced(true);
        databaseReference.keepSynced(true);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trackerInformationList.clear();
                for (DataSnapshot trackInfo : dataSnapshot.getChildren()) {
                    TrackerInformation values = trackInfo.getValue(TrackerInformation.class);
                    trackerInformation = new TrackerInformation();
                    String nameOfTrip = values.getTripName();
                    String start = values.getStartPosition();
                    String end = values.getDestination();
                    String date = values.getDate();
                    String time = values.getTime();
                    String tripType = values.getTripType();
                //    String notes = values.getTripNotes().getMyNotes();
                    id = values.getId();
                    trackerInformation.setTripName(nameOfTrip);
                    trackerInformation.setStartPosition(start);
                    trackerInformation.setDestination(end);
                    trackerInformation.setDate(date);
                    trackerInformation.setTime(time);
                    trackerInformation.setId(id);
                    trackerInformation.setTripType(tripType);

                    dataBaseNote.child(id).child("note").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                                NoteClass noteValues = noteSnapshot.getValue(NoteClass.class);
                                noteClass = new NoteClass();
                                String noteAdded = noteValues.getMyNotes();
                                String id = noteValues.getId();
                                noteClass.setId(id);
                                noteClass.setMyNotes(noteAdded);
                                Log.i("trace", noteAdded);
                                Log.i("trace", id);
                                trackerInformation.setTripNotes(noteClass);
                                trackerInformationList.add(trackerInformation);
//                                String notes = noteSnapshot.getKey();
//                                Log.i("trace", (noteSnapshot.getKey()));
//                                Log.i("trace", notes);
//                                Log.i("trace", ""+noteSnapshot.getValue());
//       //                         String notesValue = noteSnapshot.getValue();
//                                notesList.add(notes);
//                                trackerInformation.setTripNotes(notesList);
//                                trackerInformationList.add(trackerInformation);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        notesList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        trackerInformationList = new ArrayList<>();
    }

    private void androidStaff(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
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

        if (id == R.id.history) {


        } else if (id == R.id.logOut) {

        }
        else  if(id==R.id.profile_image){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkAnewUser(String id, final String email, String password) {


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
                //  User u= dataSnapshot.getValue(User.class);
                User u=new User();



                String userEmail=u.getUserEmail();
                if(userEmail==email){
                    Toast.makeText(MainActivity.this, userEmail, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}
