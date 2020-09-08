package com.example.lap.sharingstudy.News;

public class News {

    private String mTitle;
    private String mSection;
    private String mUrl;
    private String mAuthor;

    public News(String Title, String Section, String url, String Author) {
        mTitle = Title;
        mSection = Section;
        mUrl = url;
        mAuthor = Author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
