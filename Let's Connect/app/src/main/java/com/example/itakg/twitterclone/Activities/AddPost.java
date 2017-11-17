package com.example.itakg.twitterclone.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itakg.twitterclone.Extras.MyAlertDialogs;
import com.example.itakg.twitterclone.Extras.MyProgressDialog;
import com.example.itakg.twitterclone.Extras.MyToast;

import com.example.itakg.twitterclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

public class AddPost extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference mStorage;
    private static String uid;
    private ImageView profilePic, postPic;
    private EditText postText;
    private TextView name;
    private Uri uri;
    private File file;
    private Intent intent;
    private static final int REQUEST_GALLERY_CODE = 10;
    private static final int REQUEST_CAMERA_CODE = 20;
    private static final int IMAGE_QUALITY = 1;
    private String profileURL;
    private HashMap<String, String> postDATA = new HashMap<>();
    private Calendar calendar;
    private Date time;
    private String uniquePath,myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setTitle("Add Post");
        final ProgressDialog dialog = MyProgressDialog.myDialog(AddPost.this, "Loading..");
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid().toString();
        calendar = Calendar.getInstance();
        time = Calendar.getInstance().getTime();
        uniquePath = calendar.get(Calendar.DATE) + time.toString() + "";
        profilePic = (ImageView) findViewById(R.id.profile_picture);
        postPic = (ImageView) findViewById(R.id.post_image);
        postText = (EditText) findViewById(R.id.what_are_you_thinking);
        name = (TextView) findViewById(R.id.name_of_user);
        rootRef.child("All Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap map = (HashMap) dataSnapshot.getValue();
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        if (key.equals("ProfileURL")) {
                            profileURL = map.get(key).toString();
                            Picasso.with(AddPost.this).load(profileURL).into(profilePic);
                        } else if (key.equals("Name")) {
                            myName=map.get(key).toString();
                            name.setText(myName);
                        }
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void CameraImageClicked(View view) {
        uniquePath = calendar.get(Calendar.DATE) + time.toString() +myName+ "";
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), uniquePath + "");
        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, IMAGE_QUALITY);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    public void AttachFileClicked(View view) {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE) {
            if (data != null) {
                uri = data.getData();
                postPic.setImageURI(uri);
            } else
                MyAlertDialogs.myAlert(this, "Message", "No image selected!!");
        } else if (requestCode == REQUEST_CAMERA_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (file.exists()) {
                        postPic.setImageURI(uri);
                    }

                    break;
                case Activity.RESULT_CANCELED:

                    uri = null;
                    break;

            }
        }

    }

    public void PostTheImage(View view) {
        if (uri != null) {
            final String PostText = postText.getText().toString();

            final ProgressDialog dialog = MyProgressDialog.myDialog(AddPost.this, "Loading..");
            StorageReference fileName = mStorage.child("PostsPictures/" + uri.getLastPathSegment() + ".png");
            fileName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    try {
                        final String downloadURL = taskSnapshot.getDownloadUrl().toString();
                        postDATA.put("DOWNLOAD URL", downloadURL);
                        postDATA.put("POST TEXT", PostText);
                        postDATA.put("UID", uid);
                        rootRef.child("NEWPOSTS").push().setValue(postDATA);
                    } catch (Exception ex) {

                    }

                    Intent intent = new Intent(AddPost.this, NewsFeeds.class);
                    startActivity(intent);
                    dialog.dismiss();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    MyAlertDialogs.myAlert(AddPost.this, "Message", "Failed");
                    dialog.dismiss();
                }
            });
        } else
            MyAlertDialogs.myAlert(this, "Message", "No image selected!!");

    }

}
