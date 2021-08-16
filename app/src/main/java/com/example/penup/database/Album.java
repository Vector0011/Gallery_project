package com.example.penup.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "album_table")
public class Album {
    @PrimaryKey (autoGenerate = true)
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
