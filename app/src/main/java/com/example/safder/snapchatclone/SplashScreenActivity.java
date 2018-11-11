package com.example.safder.snapchatclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    //Checks if user is Logged in or not. Then displays accordingly
    public static Boolean started = false;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        //If user is not logged in, take them to registration screen
        if (mAuth.getCurrentUser() != null) {
            //Create new instance of Main Activity if user does not exist
            Intent intent = new Intent(getApplication(), MainActivity.class);
            //Reset user activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }else{
            //Create new instance of Register Activity
            Intent intent = new Intent(getApplication(), RegistrationActivity.class);
            //Reset user activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }

    }
}
