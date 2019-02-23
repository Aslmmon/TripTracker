package com.example.android.plannertracker.TripDetails;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryList {
private String tripName ;
private String startPlace ;
private String endPlace ;


    public HistoryList() {
    }

    public HistoryList(String tripName, String startPlace, String endPlace)
    {
        this.tripName = tripName;
        this.startPlace = startPlace;
        this.endPlace = endPlace;

    }



    public String getTripName() {
        return tripName;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }


}
