package com.n.interlocallyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
//import com.google.android.gms.location.LocationRequest;


public class Register extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[!@#$%^&+=])" +    //at least 1 special character
                    ".{8,}" +               //at least 8 characters
                    "$");

        EditText mEmail, mPassword;
    Button mRegisterBtn;
    FirebaseAuth fAuth;

    TextView passwordInput, emailInput, locationView;

    // location variables
    Button locationBtn;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // giving values to attributes from activity
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerBtn);
        fAuth = FirebaseAuth.getInstance();

        passwordInput = findViewById(R.id.passwordInput);
        emailInput = findViewById(R.id.emailInput);
        locationView = findViewById(R.id.locationText);

        //Assign location variables
        locationBtn = findViewById(R.id.locationBtn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Register.this);


        //check if the user has already created one account
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        // validate user input
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if (!validateEmail(email) | !validatePassword(password)) {
                    return;
                }

                //register the user into the firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //display error to the user or the result if the user was created
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Register.class));
                        } else {
                            emailInput.setText(task.getException().getMessage());
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


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "working", Toast.LENGTH_SHORT).show();
                //check permission
                if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // When permission granted
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    private boolean validateEmail(String email) {

        if (email.isEmpty()) {
            emailInput.setText("Field can't be empty");//changes the selected item text to this
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setText("Enter a valid email address Eg: user@user.com"); //changes the selected item text to this
            return false;
        } else {
            emailInput.setText("");
            return true;
        }
    }

    private boolean validatePassword(String password) {

        if (password.isEmpty()) {
            passwordInput.setText("Field can't be empty");
            return false;
        //defining the length of password, if is less than 8 characters it will display an error message
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
        passwordInput.setText("Password must have at least: \n" +
                "8 characters \n " +
                "One number \n" +
                "One upper case letter \n" +
                "One lower case letter \n" +
                "One special character (!@#$%^&+=)");
        return false;
    } else {
        passwordInput.setText("");
        return true;
    }
}

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize geoCoder
                        Geocoder geocoder = new Geocoder(Register.this, Locale.getDefault());
                        // Initialize Address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        // Set Latitude and Longitude on TextView
//                        textView1.setText("Latitude" + addresses.get(0).getLatitude() + " Longitude " + addresses.get(0).getLongitude());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}