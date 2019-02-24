package com.example.android.plannertracker.TripDetails;

public class NoteClass {
    String id;
    String myNotes;

    public NoteClass(){}
    public NoteClass(String id,String myNotes)
    {
        this.id=id;
        this.myNotes=myNotes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMyNotes(String myNotes) {
        this.myNotes = myNotes;
    }

    public String getId() {
        return id;
    }

    public String getMyNotes() {
        return myNotes;
    }
}
