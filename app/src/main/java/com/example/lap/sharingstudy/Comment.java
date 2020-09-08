package com.example.lap.sharingstudy;



public class Comment {
    private String mComment;
    private String mUsername;

    public Comment(String comment, String username) {
        mComment = comment;
        mUsername = username;
    }

    public String getComment() {
        return mComment;
    }

    public String getUsername() {
        return mUsername;
    }
}
