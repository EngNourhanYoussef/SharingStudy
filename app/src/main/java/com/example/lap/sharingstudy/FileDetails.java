package com.example.lap.sharingstudy;

public class FileDetails {
    private String mName;
    private String mBranch;
    private String mSize;

    public FileDetails(String name, String branch) {
        mName = name;
        mBranch = branch;
    }

    public String getFileName() {
        return mName;
    }

    public String getFileBranch() {
        return mBranch;
    }

    public String getSize() {
        return mSize;
    }
}
