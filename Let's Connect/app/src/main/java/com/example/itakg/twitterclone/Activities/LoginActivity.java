package com.example.itakg.twitterclone.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.itakg.twitterclone.Extras.MyAlertDialogs;
import com.example.itakg.twitterclone.Extras.MyProgressDialog;
import com.example.itakg.twitterclone.Extras.MyToast;
import com.example.itakg.twitterclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static EditText email, password;
    private FirebaseAuth mAuth;
    private static final int REQUEST_CODE = 123;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Intent intent;
    private static int flag = 0;

    @Override
    public void onBackPressed() {
        if (flag == 0) {
            MyToast.make(this, "Press again to exit");
            flag=1;
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        checkPermission();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    intent = new Intent(LoginActivity.this, NewsFeeds.class);
                    startActivity(intent);

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    public void LoginClicked(View view) {
        final String myEmail = email.getText().toString();
        final String myPassword = password.getText().toString();
        if (myEmail.equals("") || myPassword.equals("")) {
            MyAlertDialogs.myAlert(LoginActivity.this, "Message", "Invalid details");
        } else {
            final ProgressDialog dialog = MyProgressDialog.myDialog(LoginActivity.this, "Loading..");
            mAuth.signInWithEmailAndPassword(myEmail, myPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                MyAlertDialogs.myAlert(LoginActivity.this, "Error", "Something went wrong..");

                            } else {
                                intent = new Intent(LoginActivity.this, NewsFeeds.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MyAlertDialogs.myAlert(LoginActivity.this,"Error","Something went wrong.");
                }
            });

        }

    }

    public void RegisterMeClicked(View view) {
        intent = new Intent(LoginActivity.this, RegisterUser.class);
        startActivity(intent);

    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED&&grantResults[2] == PackageManager.PERMISSION_GRANTED);

        } else MyAlertDialogs.myAlert(LoginActivity.this, "Message", "Something went wrong!!");
    }
}
