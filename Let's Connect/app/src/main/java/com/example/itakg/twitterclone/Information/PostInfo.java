package com.example.itakg.twitterclone.Information;

/**
 * Created by itakg on 10/26/2017.
 */

public class PostInfo {
    public String imageUrl;
    public String postId;
    public String postText;
    public String uid;

    public PostInfo(String imageUrl, String postId, String postText, String uid) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.postText = postText;
        this.uid = uid;
    }
}
