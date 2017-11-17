package com.example.itakg.twitterclone.Information;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itakg.twitterclone.Activities.Community;
import com.example.itakg.twitterclone.Activities.NewsFeeds;
import com.example.itakg.twitterclone.Extras.MyToast;
import com.example.itakg.twitterclone.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerView extends RecyclerView.Adapter<MyRecyclerView.MyViewHolder> {
    private LayoutInflater inflator;
    private Context context;
    ArrayList<UsersInfo> list = new ArrayList<>();


    public MyRecyclerView(Context context, ArrayList<UsersInfo> data) {
        this.context = context;
        inflator = LayoutInflater.from(context);
        this.list = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.recycle_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UsersInfo current = list.get(position);
        holder.title.setText(current.title);
        Picasso.with(context).load(current.image).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.individualName);
            image = itemView.findViewById(R.id.individualPic);
        }
    }

}