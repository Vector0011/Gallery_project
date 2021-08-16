package com.example.penup.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "album_data_table")
public class AlbumData {
    @PrimaryKey (autoGenerate = true)
    int id;
    int album_id;
    int media_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }
}
