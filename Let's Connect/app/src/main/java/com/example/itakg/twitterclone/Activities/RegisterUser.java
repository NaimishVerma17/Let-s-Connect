package com.example.itakg.twitterclone.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.itakg.twitterclone.Extras.MyAlertDialogs;
import com.example.itakg.twitterclone.Extras.MyProgressDialog;
import com.example.itakg.twitterclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {
    private EditText name, email, password, username;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private FirebaseUser user;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();
        name = (EditText) findViewById(R.id.candid_name);
        email = (EditText) findViewById(R.id.candid_email);
        password = (EditText) findViewById(R.id.candid_password);
        username = (EditText) findViewById(R.id.candid_username);
        mAuth = FirebaseAuth.getInstance();
    }

    public void RegisterNewUser(View view) {
        final String myName, myPassword, myEmail, myUserName;
        myName = name.getText().toString();
        myEmail = email.getText().toString();
        myPassword = password.getText().toString();
        myUserName = username.getText().toString();
        if (myEmail.equals("") || myName.equals("") || myPassword.equals("") || myUserName.equals("")) {
            MyAlertDialogs.myAlert(RegisterUser.this, "Message", "Invalid Details!!");
        } else {
            final ProgressDialog dialog = MyProgressDialog.myDialog(RegisterUser.this, "Loading..");
            mAuth.createUserWithEmailAndPassword(myEmail, myPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                user = mAuth.getCurrentUser();
                                String uid = user.getUid().toString();
                                rootRef.child("All Users").child(uid).child("Name").setValue(myName);
                                rootRef.child("All Users").child(uid).child("UserName").setValue(myUserName.toString());
                                intent = new Intent(RegisterUser.this, ImageUpload.class);
                                intent.putExtra("Name", myName);
                                startActivity(intent);
                                dialog.dismiss();

                            } else {
                                MyAlertDialogs.myAlert(RegisterUser.this, "Message", "Something went wrong..");
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }


}
