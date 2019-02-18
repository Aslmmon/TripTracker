package com.example.android.plannertracker.TripDetails;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.android.plannertracker.ChatHeadService;
import com.example.android.plannertracker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddNote extends AppCompatActivity {
    public static final String CLICKED_ITEM_POSITION = "ClickedItemPoisiton";
    EditText addNote;
    int position;
    String IDTaken;
    FloatingActionButton fab;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initialize();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NoteAdded = addNote.getText().toString();
                String IDClicked = getIntent().getStringExtra("id");
                Log.i("trace", IDClicked);
                databaseReference.child(IDClicked).child("note").setValue(NoteAdded);
                startService(new Intent(AddNote.this, ChatHeadService.class));
                finish();
            }
        });

    }

    private void initialize() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Trip Data");
        addNote = findViewById(R.id.editNote);
        fab = findViewById(R.id.fab);
    }
}
