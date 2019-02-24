package com.example.android.plannertracker.TripDetails;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.plannertracker.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


public class StaticMap extends AppCompatActivity {
    private MapView mapView;
    private static final String token = "sk.eyJ1IjoibWlsa3lyYW5nZXIiLCJhIjoiY2pzOTBzOXlxMTZ6ZDN6czhiNTJjY2JrdCJ9.TVE3NN-juPXRMYr14hRBFA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, token);
        setContentView(R.layout.activity_static_map);

        mapView =  findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {





                    }
                });
            }
        });

    }


}
