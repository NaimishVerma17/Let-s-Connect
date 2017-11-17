package com.example.itakg.twitterclone.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.itakg.twitterclone.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class Splaash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
