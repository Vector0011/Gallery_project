package com.example.penup.models;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

public class ListMediaByFolder extends BaseObservable {
    String path;
    String name;
    List<MediaModel> listMedia;

    public ListMediaByFolder() {
        listMedia = new ArrayList<>();
    }

    public ListMediaByFolder(String path, String name) {
        this.path = path;
        this.name = name;
        listMedia = new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MediaModel> getListMedia() {
        return listMedia;
    }

    public void setListMedia(List<MediaModel> listMedia) {
        this.listMedia = listMedia;
    }
}
