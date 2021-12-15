package com.example.googlemapstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {


    EditText email;
    EditText password;

    String emailinput;
    String passwordinput;

    private FirebaseAuth mAuth;

    boolean locationPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);




        mAuth = FirebaseAuth.getInstance();

        checkPermissions();

        if(locationPermission){
            if(checkGooglePlayServices()){
                Toast.makeText(MainActivity.this, "Google Play Services Available", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this, "Google Services Not Available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkGooglePlayServices(){

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(MainActivity.this);

        if(result == ConnectionResult.SUCCESS){
            return true;
        }else if(googleApiAvailability.isUserResolvableError(result)){
            Dialog dialog = googleApiAvailability.getErrorDialog(MainActivity.this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(MainActivity.this, "Google Services Not Available", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }

        return false;
    }

    private void checkPermissions(){
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                locationPermission = true;
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package",getPackageName(), null);

                intent.setData(uri);

                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();

            }
        }).check();
    }




    //SIGN UP
    public void onClickSignUp(View view){

        emailinput = email.getText().toString();
        passwordinput = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailinput,  passwordinput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Email and Password stored",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Storing Email and Password Failed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //SIGN IN
    public void onClickSignin(View view){

        emailinput = email.getText().toString();
        passwordinput = password.getText().toString();

        mAuth.signInWithEmailAndPassword(emailinput,  passwordinput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Sign in Successful",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, Mapstest.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this,"Sign in Failed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}