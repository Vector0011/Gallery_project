package com.example.penup.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Album.class, AlbumData.class}, version = 1)
public abstract class AlbumDatabase extends RoomDatabase {
    private static String DATABASE_NAME = "album.db";
    private static AlbumDatabase instance;

    public static synchronized AlbumDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AlbumDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AlbumDAO albumDAO();
    public abstract AlbumDataDAO albumDataDAO();
}
