package com.example.safder.snapchatclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ShowPhotoActivity extends AppCompatActivity {

    String Uid;
    Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);


        //Fetches photo data from CameraFragment
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] data = extras.getByteArray("capture");

        if (data != null) {
            ImageView image = findViewById(R.id.photoCaptured);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Fixes rotation issue of camera
            Bitmap rotateBitmap = rotate(decodedBitmap);

            //Sets bitmap as an image
            image.setImageBitmap(rotateBitmap);
        }

        //Gets user ID
        Uid = FirebaseAuth.getInstance().getUid();
        Button mStory = findViewById(R.id.story);
        mStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStories();
            }
        });

    }

    //Function to save capture to stories
    private void saveToStories(){
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("story");
        final String key = userStoryDb.push().getKey();

        //Creates a folder in database, stores photos in it and then gives each photo a unique ID
        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        //Compressses photo for upload
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG,20,boas);
        byte[] dataToUpload = boas.toByteArray();
        //Uploads to Database
        UploadTask uploadTask = filePath.putBytes(dataToUpload);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri imageUrl = taskSnapshot.getStorage().getDownloadUrl().getResult();



                //Convert time to milliseconds
                Long currentTimestamp = System.currentTimeMillis();
                Long endTimestamp = currentTimestamp + (24*60*60*1000);

                Map<String,Object> mapToUpload = new HashMap<>();
                mapToUpload.put("imageUrl",imageUrl.toString());
                mapToUpload.put("timestamp",currentTimestamp);
                mapToUpload.put("timestampEnd",endTimestamp);

                userStoryDb.child(key).setValue(mapToUpload);

                finish();
                return;
            }
        });

        //Checks if upload was succesful
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });

    }



    //Decode imageData and store into matrix
    private Bitmap rotate(Bitmap decodedBitmap) {
        int width = decodedBitmap.getWidth();
        int height = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodedBitmap,0,0,width,height,matrix, true);
    }
}
