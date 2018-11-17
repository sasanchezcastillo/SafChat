package com.example.safder.snapchatclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowPhotoActivity extends AppCompatActivity {

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


    }

    private Bitmap rotate(Bitmap decodedBitmap) {
        int width = decodedBitmap.getWidth();
        int height = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodedBitmap,0,0,width,height,matrix, true);
    }
}
