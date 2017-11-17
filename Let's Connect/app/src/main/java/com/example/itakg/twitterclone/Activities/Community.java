package com.example.itakg.twitterclone.Activities;

import android.app.ProgressDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itakg.twitterclone.Extras.MyProgressDialog;
import com.example.itakg.twitterclone.Extras.MyToast;
import com.example.itakg.twitterclone.Information.MyRecyclerView;
import com.example.itakg.twitterclone.Information.UsersInfo;
import com.example.itakg.twitterclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.example.itakg.twitterclone.R.id.individualName;

public class Community extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecyclerView myRecyclerView;
    ArrayList<UsersInfo> data = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        getSupportActionBar().setTitle("COMMUNITY");
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        dialog = MyProgressDialog.myDialog(Community.this, "Loading..");
        getData();


    }

    private void getData() {
        rootRef.child("All Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                HashMap map1 = (HashMap) dataSnapshot.getValue();
                Set<String> keys = map1.keySet();
                for (String key : keys) {
                    HashMap map2 = (HashMap) map1.get(key);
                    data.add(new UsersInfo(map2.get("Name").toString(), map2.get("ProfileURL").toString()));
//                    MyToast.make(Community.this,String.valueOf(data.size()));
                }
                myRecyclerView = new MyRecyclerView(Community.this, data);
                recyclerView.setAdapter(myRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(Community.this));
                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
