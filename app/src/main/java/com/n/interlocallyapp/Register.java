package com.n.interlocallyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.android.gms.location.LocationRequest;


public class Register extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mRegisterBtn;
    Button locationBtn;
    FirebaseAuth fAuth;


//    TextView addressText;
//    Button locationButton;
//    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // giving values to attributes from activity
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerBtn);
        locationBtn = findViewById(R.id.locationBtn);

        fAuth = FirebaseAuth.getInstance();

        //check if the user has already created one account
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        // location button to be worked on
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "Working", Toast.LENGTH_SHORT).show();
            }
        });

        // validate user input
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // if the user enter an empty value for email or password,it will display an error message
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required!");
                    return;
                }
                //defining the length of password, if is less than 6 characters it will display an error message
                if (password.length() < 6) {
                    mPassword.setError("Password Must be equal to 6 characters");
                    return;
                }

                //register the user into the firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //display error to the user or the result if the user was created
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Register.class));
                        } else {
                            Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


        // making text clickable
        TextView textLogin = findViewById(R.id.Login);

        String login = "Already Registered? Click Here";

        SpannableString sLogin = new SpannableString(login);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(Register.this, "One", Toast.LENGTH_SHORT).show();

                //Going from Registration to Login when clicked.
                Intent loginIntent = new Intent(Register.this, Login.class);

                startActivity(loginIntent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);
            }
        };

        sLogin.setSpan(clickableSpan1, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // change textView to new clickable text
        textLogin.setText(sLogin);
        textLogin.setMovementMethod(LinkMovementMethod.getInstance());
    }

}


//        addressText = findViewById(R.id.addressText);
//        locationButton = findViewById(R.id.locationBtn);
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(2000);
//
//        locationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                        if (isGPSEnabled()) {
//                            LocationServices.getFusedLocationProviderClient(Register.this).requestLocationUpdates(locationRequest, new LocationCallback() {
//                                @Override
//                                public void onLocationResult(@NonNull LocationResult locationResult) {
//                                    super.onLocationResult(locationResult);
//
//                                    LocationServices.getFusedLocationProviderClient(Register.this).removeLocationUpdates(this);
//
//                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
//                                        int index = locationResult.getLocations().size() - 1;
//                                        double latitude = locationResult.getLocations().get(index).getLatitude();
//                                        double longitude = locationResult.getLocations().get(index).getLongitude();
//
//                                        addressText.setText("Latitude: " + latitude + " /n" + "Longitude: " + longitude);
//
//
//                                    }
//                                }
//                            }, Looper.getMainLooper());
//
//                        } else {
//                            turnOnGPS();
//                        }
//                    } else {
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                    }
//                }
//            }
//        });


//    private void turnOnGPS() {
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
//                .checkLocationSettings(builder.build());
//
//        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//
//                try {
//                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(Register.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
//
//                } catch (ApiException e) {
//
//                    switch (e.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//
//                            try {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                resolvableApiException.startResolutionForResult(Register.this, 2);
//                            } catch (IntentSender.SendIntentException ex) {
//                                ex.printStackTrace();
//                            }
//                            break;
//
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            //Device does not have location
//                            break;
//                    }
//                }
//            }
//        });
//
//    }
//
//    private boolean isGPSEnabled() {
//        LocationManager locationManager = null;
//        boolean isEnabled = false;
//
//        if (locationManager == null) {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        }
//
//        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return isEnabled;
//
//    }