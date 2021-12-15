package com.example.googlemapstest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class Mapstest extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;

    GoogleMap googleMap1;

    LocationRequest locationRequest;



    FusedLocationProviderClient fusedLocationProviderClient;


    LatLng latLng;


    double lat, lng;


    FirebaseFirestore db;

    Users user;

    Userlocations userlocations;
    Userlocations userlocs;


    ArrayList<Userlocations> arrayListUserLoc;

    ArrayList<Marker> markers;


    ArrayList<Marker> mMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapstest);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        mapView = findViewById(R.id.mapView);


        mapView.getMapAsync(Mapstest.this);

        mapView.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();


    }

    /*
    public void getUsermarkers(View view){


         userlocs = new Userlocations();



            db.collection("User locations")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()){

                                for(QueryDocumentSnapshot document : task.getResult()){
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    userlocs = document.toObject(Userlocations.class);

                                    arrayListUserLoc.add(userlocs);




                                }

                            }else{

                                Log.d(TAG,"Error getting documents ", task.getException());

                            }



                        }
                    });






       // Toast.makeText(Mapstest.this, "Array list:"+ arrayListUserLoc.get(0).toString(), Toast.LENGTH_LONG).show();

    }
    */


    public void getuserdata(View view){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){

            String email = firebaseUser.getEmail();
            String uid = firebaseUser.getUid();

            Toast.makeText(Mapstest.this, "Email: "+email+" uID:"+uid, Toast.LENGTH_LONG).show();



        }else{

            Toast.makeText(Mapstest.this, "NO user data found!", Toast.LENGTH_LONG).show();
        }

    }



    public void onClickGetCurrentLocation(View view){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        user = new Users();

        user.setUserid(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());


        displaycurrentLocation();



    }

    @SuppressLint("MissingPermission")
    public void displaycurrentLocation(){


        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

                        latLng = new LatLng(location.getLatitude(), location.getLongitude());


                        lat = location.getLatitude();
                        lng = location.getLongitude();

                        userlocations = new Userlocations();


                        userlocations.setGeoPoint(geoPoint);
                        userlocations.setUser(user);


                        db.collection("User locations").add(userlocations);

                        //Toast.makeText(Mapstest.this, "Location: "+location.getLongitude()+" "+location.getLatitude(),Toast.LENGTH_LONG).show();


                        if (location != null) {
                            // Logic to handle location object

                        }
                    }
                });


    }





    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap1 = googleMap;



        googleMap1.getUiSettings().setZoomControlsEnabled(true);
        googleMap1.getUiSettings().setCompassEnabled(true);
        googleMap1.setMyLocationEnabled(true);


        //GET CURRENT LOCATION FOR CAMERA
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

                            latLng = new LatLng(location.getLatitude(), location.getLongitude());


                            lat = location.getLatitude();
                            lng = location.getLongitude();




                            //Toast.makeText(Mapstest.this, "Location: "+location.getLongitude()+" "+location.getLatitude(),Toast.LENGTH_LONG).show();

                            //Toast.makeText(Mapstest.this, "Location NOT ON MAP: "+latLng,Toast.LENGTH_LONG).show();


                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            googleMap.animateCamera(cameraUpdate);

                        }
                    }
                });


        //Userlocations mUserLocations = new Userlocations();
        mMarkers = new ArrayList<>();


        db.collection("User locations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        ArrayList<Userlocations> mUserLocArrayList = new ArrayList<>();

                        for(QueryDocumentSnapshot doc : value){

                            if(doc.get("geoPoint") != null){
                                mUserLocArrayList.add(doc.toObject(Userlocations.class));



                                for(int i = 0; i < mUserLocArrayList.size(); i++){

                                    LatLng newlatlng = new LatLng(mUserLocArrayList.get(i).getGeoPoint().getLatitude(),
                                                                    mUserLocArrayList.get(i).getGeoPoint().getLongitude());

                                    MarkerOptions markerOptions = new MarkerOptions();

                                    markerOptions.title("Position");
                                    markerOptions.position(newlatlng);

                                    Marker tempMarker = googleMap.addMarker(markerOptions);

                                    mMarkers.add(tempMarker);
                                }

                                //Toast.makeText(Mapstest.this,"Gikan db:"+mMarkers.size(),Toast.LENGTH_LONG).show();
                            }
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Toast.makeText(Mapstest.this,"New Location:"+dc.getDocument().getGeoPoint("geoPoint"),Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "New Location: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified Location: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    for(int i = 0; i < mMarkers.size(); i++){

                                        if(mMarkers.get(i).getPosition().latitude == dc.getDocument().getGeoPoint("geoPoint").getLatitude() &&
                                           mMarkers.get(i).getPosition().longitude ==  dc.getDocument().getGeoPoint("geoPoint").getLongitude()){

                                            mMarkers.get(i).remove();
                                            //mMarkers.remove(i);

                                            //Toast.makeText(Mapstest.this,"Removed db:"+dc.getDocument().getGeoPoint("geoPoint"),Toast.LENGTH_LONG).show();
                                            //Toast.makeText(Mapstest.this,"Pila e remove:"+mMarkers.size(),Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "Removed Location: " + dc.getDocument().getData());
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });










/*
        db.collection("User locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                userlocs = document.toObject(Userlocations.class);

                                arrayListUserLoc.add(userlocs);





                            }

                        }else{

                            Log.d(TAG,"Error getting documents ", task.getException());

                        }


                        for(int i = 0; i < arrayListUserLoc.size(); i++){





                        }





                    }
                });



*/


       // LatLng newlatlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

       // Toast.makeText(Mapstest.this,"onMAP latlng:"+newlatlng, Toast.LENGTH_LONG).show();

        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(, 15);
        //googleMap.animateCamera(cameraUpdate);

/*
        LatLng latLng = new LatLng(7.152256,125.650057);

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.title("My Position");
        markerOptions.position(latLng);

        googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);

        googleMap.animateCamera(cameraUpdate);


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);




 */


    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Mapstest.this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Toast.makeText(Mapstest.this, "Location: "+locationResult.getLastLocation().getLongitude()+" "+locationResult.getLastLocation().getLatitude(),Toast.LENGTH_LONG).show();
            }
        }, Looper.getMainLooper());

    }

    private void checkGPS() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse>locationSettingsResponseTask = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                    Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_LONG).show();

                } catch (ApiException e) {

                    if(e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(Mapstest.this,101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }
                    if(e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE){
                        Toast.makeText(getApplicationContext(), "GPS NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(requestCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "GPS is Enabled", Toast.LENGTH_LONG).show();
            }
            if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "GPS is Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mapView.onLowMemory();
    }


}