package com.example.penup.models;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GridMediaByDate extends BaseObservable {
    private Date date;
    private List<MediaModel> mListMedia;

    public GridMediaByDate() {
        date = new Date();
        mListMedia = new ArrayList<>();
    }

    public GridMediaByDate(Date date, List<MediaModel> mListMedia) {
        this.date = date;
        this.mListMedia = mListMedia;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<MediaModel> getmListMedia() {
        return mListMedia;
    }

    public void setmListMedia(List<MediaModel> mListMedia) {
        this.mListMedia = mListMedia;
    }

}
