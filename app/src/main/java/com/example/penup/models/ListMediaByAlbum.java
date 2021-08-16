package com.example.penup.models;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

public class ListMediaByAlbum extends BaseObservable {
    private int albumId;
    private String albumName;
    private List<MediaModel> listMedia;

    public ListMediaByAlbum() {
        listMedia = new ArrayList<>();
        albumName = new String();
    }

    public ListMediaByAlbum(int albumId, String albumName, List<MediaModel> listMedia) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.listMedia = listMedia;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<MediaModel> getListMedia() {
        return listMedia;
    }

    public void setListMedia(List<MediaModel> listMedia) {
        this.listMedia = listMedia;
    }
}
