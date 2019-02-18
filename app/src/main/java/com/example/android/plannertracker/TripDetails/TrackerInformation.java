package com.example.android.plannertracker.TripDetails;

public class TrackerInformation {
    private String id;
    private String StartPosition;
    private String destination;
    private String TripName;
    private String time;
    private String date;
    private String Note;
    private String tripType;

    public TrackerInformation(String note) {
        this.Note = note;
    }

    public TrackerInformation() {}


    public TrackerInformation(String id, String startPosition, String destination,
                              String tripName, String time, String date,
                              String tripType, String Note) {
        this.id = id;
        this.StartPosition = startPosition;
        this.destination = destination;
        TripName = tripName;
        this.time = time;
        this.date = date;
        this.tripType = tripType;
        this.Note = Note;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setStartPosition(String startPosition) {
        StartPosition = startPosition;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getStartPosition() {
        return StartPosition;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }
}
