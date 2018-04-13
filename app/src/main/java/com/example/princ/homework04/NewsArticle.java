package com.example.princ.homework04;

import android.util.Log;

import java.util.ArrayList;

public class NewsArticle {

    String title;
    String pubDate;
    String link;
    String description;
    ArrayList<String> imageLink=new ArrayList<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String[] s=description.split("<");
        Log.d("demo", "setDescription: "+s[0]);
        this.description = s[0];
    }

    public String getImageLink() {
        if(imageLink!=null&&!imageLink.isEmpty()) {
            return imageLink.get(0);
        }else return null;
    }

    public void setImageLink(String imageLink) {
        this.imageLink.add(imageLink);
    }
}
