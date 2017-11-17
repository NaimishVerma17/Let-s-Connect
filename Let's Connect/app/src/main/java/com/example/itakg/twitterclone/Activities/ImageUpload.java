package com.example.itakg.twitterclone.Activities;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.example.itakg.twitterclone.Extras.MyAlertDialogs;
import com.example.itakg.twitterclone.Extras.MyProgressDialog;
import com.example.itakg.twitterclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImageUpload extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView profilePic;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_GALLERY_CODE = 10;
    private static final int REQUEST_CAMERA_CODE = 20;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private StorageReference mStorage;
    private static String uid;
    private Uri uri1;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        getSupportActionBar().setTitle("UPLOAD PROFILE PICTURE");
        profilePic = (ImageView) findViewById(R.id.profilePic);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid().toString();

    }
    public void SavePicClicked(View view) {
        loadImages();

    }

    public void CameraClicked(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), uid.toString());
        uri1 = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);

    }

    private void loadImages() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY_CODE) {
            if (data != null) {
                uri1 = data.getData();
                profilePic.setImageURI(uri1);
            }
        } else if (requestCode == REQUEST_CAMERA_CODE) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (file.exists()) {
                        profilePic.setImageURI(uri1);
                    } else {
                        MyAlertDialogs.myAlert(ImageUpload.this, "Message", "No pic selected!!");

                    }
                    break;
                case Activity.RESULT_CANCELED:
                    uri1 = null;
                    break;
            }
        }
    }


    public void upload(View view) {
        if (uri1 != null) {
            final ProgressDialog dialog = MyProgressDialog.myDialog(ImageUpload.this, "Loading..");
            StorageReference fileName = mStorage.child("ProfilePhotos/" + uid.toString() + ".png");
            fileName.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    try {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        rootRef.child("All Users").child(uid).child("ProfileURL").setValue(url);
                        Intent intent = new Intent(ImageUpload.this, NewsFeeds.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } catch (Exception ex) {

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    MyAlertDialogs.myAlert(ImageUpload.this, "Message", "Failed");
                }
            });

        } else MyAlertDialogs.myAlert(this, "Message", "No image selected!!");
    }


}
