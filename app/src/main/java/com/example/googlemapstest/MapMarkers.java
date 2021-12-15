package com.example.googlemapstest;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapMarkers {


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    Marker marker;

    public MapMarkers(Marker marker) {
        this.marker = marker;
    }

    public MapMarkers() {

    }
}
