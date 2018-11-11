package com.example.safder.snapchatclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class RegisterActivity extends AppCompatActivity {


    private Button mRegister;
    private EditText mEmail, mPassword, mName;


    //Checks if there is a change in authentication state
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //If the user does exist
                if (user != null) {
                    //Create new instance of Main Activity if user exists
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    //Reset user activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        mAuth = FirebaseAuth.getInstance();

        //Import all views from the XML
        mRegister = findViewById(R.id.register);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);



        //On clicking register, store email and password.
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                //Use firebase method to authenticate
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If authentication fails then it shows error toast
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplication(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            //If successful get user ID
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                            //Map keys into values for the database
                            Map userInfo = new HashMap<>();
                            userInfo.put("email",email);
                            userInfo.put("name",name);
                            userInfo.put("profileImageUrl","default");

                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });

    }


    //When the activity starts the Firebase authenticator must also start
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    //When activity is closed the listener ends
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }


}
