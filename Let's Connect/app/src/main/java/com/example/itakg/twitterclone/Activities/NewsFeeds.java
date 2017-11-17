package com.example.itakg.twitterclone.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.itakg.twitterclone.Extras.MyProgressDialog;
import com.example.itakg.twitterclone.Extras.MyToast;
import com.example.itakg.twitterclone.Fragments.About;
import com.example.itakg.twitterclone.Information.MyAdapter;
import com.example.itakg.twitterclone.Information.PostInfo;
import com.example.itakg.twitterclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NewsFeeds extends AppCompatActivity implements MyAdapter.LikeClicked {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid;
    private RecyclerView recyclerView;
    private ArrayList<PostInfo> arrayList;
    private MyAdapter myAdapter;
    private ProgressDialog dialog;
    private static int flag = 0;
    public FragmentManager manager;
    public FragmentTransaction transaction;

    @Override
    public void onBackPressed() {
        if (flag == 0) {
            MyToast.make(this, "Press again to exit");
            flag = 1;
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
        setContentView(R.layout.activity_news_feeds);
        getSupportActionBar().setTitle("NEWS FEEDS");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid().toString();
        recyclerView = (RecyclerView) findViewById(R.id.postRecycler);
        arrayList = new ArrayList<>();
        dialog = MyProgressDialog.myDialog(NewsFeeds.this, "Loading..");
        loadAllPosts();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.community:
                intent = new Intent(NewsFeeds.this, Community.class);
                startActivity(intent);
                break;
            case R.id.Signout:
                FirebaseAuth.getInstance().signOut();
                MyToast.make(this, "You are successfully logged out!!");
                intent = new Intent(NewsFeeds.this, LoginActivity.class);
                startActivity(intent);

                break;
            case R.id.changeProfilePic:
                intent = new Intent(NewsFeeds.this, ImageUpload.class);
                startActivity(intent);
                break;
            case R.id.addPost:
                intent = new Intent(NewsFeeds.this, AddPost.class);
                startActivity(intent);
                break;
            case R.id.location:
                intent = new Intent(NewsFeeds.this, Location.class);
                startActivity(intent);
                break;
            case R.id.about:
                About about = new About();
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.add(R.id.constrainLayoutForFragment, about, "About");
                transaction.commit();
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    private void loadAllPosts() {

        rootRef.child("NEWPOSTS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    arrayList.clear();
                    HashMap map1 = (HashMap) dataSnapshot.getValue();
                    Set<String> keys = map1.keySet();
                    for (String key : keys) {
                        HashMap map2 = (HashMap) map1.get(key);
                        arrayList.add(new PostInfo(map2.get("DOWNLOAD URL").toString(), "show", map2.get("POST TEXT").toString(), map2.get("UID").toString()));

                    }
                    myAdapter = new MyAdapter(NewsFeeds.this, arrayList);
                    myAdapter.setInstatce(NewsFeeds.this);
                    recyclerView.setAdapter(myAdapter);
                   // recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NewsFeeds.this));
                    dialog.dismiss();
                } catch (Exception ex) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void like_clicked(final int position, final ImageView view) {
       // MyToast.make(this,String.valueOf(position));
        rootRef.child("NEWPOSTS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int i=0;
                    HashMap map1 = (HashMap) dataSnapshot.getValue();
                    Set<String> keys = map1.keySet();
                     for (String key : keys) {
                         if(i==position)
                         {
                             rootRef.child("LIKES").child(key).child(uid).setValue("true");
                             view.setImageResource(R.drawable.like);
                             MyToast.make(NewsFeeds.this,"You liked this post");
                         }
                         i++;
                     }
//
                } catch (Exception ex) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
