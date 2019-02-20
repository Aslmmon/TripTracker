package com.example.android.plannertracker.TripDetails;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.plannertracker.ChatHeadService;
import com.example.android.plannertracker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


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
                //List<String> NoteAdded = new ArrayList<>();
                //NoteAdded.add(note);
                String note = addNote.getText().toString();
                String IDClicked = getIntent().getStringExtra("id");
                String key = databaseReference.child(IDClicked).child("note").push().getKey();
                NoteClass noteClass = new NoteClass(key,note);
                databaseReference.child(IDClicked).child("note").child(key).setValue(noteClass);
                Toast.makeText(AddNote.this, "Done added", Toast.LENGTH_SHORT).show();
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
