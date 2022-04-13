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
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {
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
    Button mLoginBtn;
    FirebaseAuth fAuth;

    TextView passwordInput, emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // giving values to attributes from activity
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);



        passwordInput = findViewById(R.id.passwordInput);
        emailInput = findViewById(R.id.emailInput);

        //validate the user input
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (!validateEmail(email) | !validatePassword(password)) {
                    return;
                }

                //authenticate the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            emailInput.setText(task.getException().getMessage());
                        }
                    }
                });
            }
        });

        //Making TextView clickable
        TextView textRegister = findViewById(R.id.Register);
        TextView textPassword = findViewById(R.id.PasswordText);

        String register = "First-time User? Register Here";
        String password = "Forgotten Password? Click Here";

        SpannableString sRegister = new SpannableString(register);
        SpannableString sPassword = new SpannableString(password);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(Login.this, "One", Toast.LENGTH_SHORT).show();

                //Going from Login to Registration when clicked.
                Intent registrationIntent = new Intent(Login.this, Register.class);

                startActivity(registrationIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(Login.this, "Two", Toast.LENGTH_SHORT).show();

//                Going from Login to password reset when clicked.
                Intent passwordIntent = new Intent(Login.this, ForgotPassword.class);

                startActivity(passwordIntent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);
            }
        };

        sRegister.setSpan(clickableSpan1, 17, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sPassword.setSpan(clickableSpan2, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //Set new registration text now clickable
        textRegister.setText(sRegister);
        textRegister.setMovementMethod(LinkMovementMethod.getInstance());

        // Set new forgot password text now clickable
        textPassword.setText(sPassword);
        textPassword.setMovementMethod(LinkMovementMethod.getInstance());

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
}