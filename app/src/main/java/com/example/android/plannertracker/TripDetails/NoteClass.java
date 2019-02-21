package com.example.android.plannertracker.TripDetails;

public class NoteClass {
    private String Id;
    private String myNotes;

    public NoteClass(){

    }

    public NoteClass(String id, String myNotes) {
        Id = id;
        this.myNotes = myNotes;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMyNotes() {
        return myNotes;
    }

    public void setMyNotes(String myNotes) {
        this.myNotes = myNotes;
    }
}
