package com.example.itakg.twitterclone.Information;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itakg.twitterclone.Activities.NewsFeeds;
import com.example.itakg.twitterclone.Extras.MyToast;

import com.example.itakg.twitterclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Context context;
    LayoutInflater inflator;
    ArrayList<PostInfo> arrayList = new ArrayList<>();
    private String profileURL;
    private LikeClicked likeClicked;

    public void setInstatce(LikeClicked likeClicked) {
        this.likeClicked = likeClicked;
    }


    public MyAdapter(Context context, ArrayList<PostInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.post_layouts, parent, false);
        MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PostInfo current = arrayList.get(position);
        holder.title.setText(current.postText);
        checkLikes(position,holder.like,current.uid);
        Picasso.with(context).load(current.imageUrl)
                .into(holder.postimage);
        rootRef.child("All Users").child(current.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    HashMap map = (HashMap) dataSnapshot.getValue();
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        if (key.equals("ProfileURL")) {
                            profileURL = map.get(key).toString();
                            Picasso.with(context).load(profileURL).into(holder.profilePicture);
                        } else if (key.equals("Name")) {
                            holder.userName.setText(map.get(key).toString());
                        }
                    }

                } catch (Exception e) {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, userName;
        ImageView postimage, profilePicture, like;


        public MyViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTextInPost);
            userName = itemView.findViewById(R.id.userNameInPost);
            postimage = itemView.findViewById(R.id.postImageInPost);
            profilePicture = itemView.findViewById(R.id.profilePictureInPost);
            like = itemView.findViewById(R.id.like);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeClicked.like_clicked(getPosition(),like);

                }
            });

        }
    }

    public interface LikeClicked {
        public void like_clicked(int position,ImageView view);
    }
public void checkLikes(final int position , final ImageView imageView , final String uid)
{
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
                        rootRef.child("LIKES").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try{
                                HashMap map1 = (HashMap) dataSnapshot.getValue();
                                Set<String> keys = map1.keySet();
                                for (String key : keys) {
                                    if(key.equals(uid))
                                    {
                                        if(map1.get(key).equals("true"))
                                        {
                                            imageView.setImageResource(R.drawable.like);
                                        }
                                    }
                                }
                            }catch(Exception e){}}


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


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