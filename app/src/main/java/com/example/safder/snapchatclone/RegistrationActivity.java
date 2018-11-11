package com.example.safder.snapchatclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //Store buttons from Registration page into variables
        Button mLogin = findViewById(R.id.login);
        Button mRegister = findViewById(R.id.register);


        //Set OnClickListeners onto the two buttons
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create new instance of Login Activity on click of Login
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new instance of Register Activity on click of Login
                Intent intent = new Intent(getApplication(), RegisterActivity.class);
                startActivity(intent);
                return;

            }
        });


    }
}
