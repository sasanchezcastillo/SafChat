package com.example.safder.snapchatclone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    // Creates camera accesibility
    Camera camera;
    Camera.PictureCallback jpegCallback;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;


    final int CAMERA_REQUEST_CODE = 1;

    //Constructs new instance of ChatFragment
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }


    //Inflates created view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        //Holder allows us to add camera into the view
        mSurfaceView = view.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();


        //If the device does not explicitly give permission, ask user for permission to use camera.
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }


        // OnClickListener for logout button
        Button mLogout = view.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LogOut(); //Calls function on click
            }
        });

        // On click listener for capturing photos
        Button mTakePhoto = view.findViewById(R.id.takePhoto);
        mTakePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                takePhoto();
            }

        });


        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //Sends taken photo data and sends it to the ShowPhotoActivity
                Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                intent.putExtra("capture",bytes);

            }
        };

        return view;
    }

    //Take photo method
    private void takePhoto() {
        camera.takePicture(null,null,jpegCallback);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open(); //opens camera


        Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90); //Vertical camera setting
        parameters.setPreviewFrameRate(30); //Refresh rate of preview
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); //Focus using vidoe


        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview(); //Sets camera functionality into surface
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    //If user denies permission display toast asking them to accept it
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                } else {
                    Toast.makeText(getContext(), "Please provide the permission required", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    //Log out functionality
    private void LogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), SplashScreenActivity.class); //Takes user back to splashscreen after logging out
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clears all fragments on top of the current activity
        startActivity(intent);
        return;
    }

}
