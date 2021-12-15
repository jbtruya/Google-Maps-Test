package com.example.googlemapstest;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.auth.User;

import java.util.Date;

public class Userlocations {

    GeoPoint geoPoint;
    @ServerTimestamp Date timestamp;
    private Users user;

    public Userlocations() {
    }
    public Userlocations(GeoPoint geoPoint, Date timestamp, Users user) {
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
        this.user = user;
    }
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
