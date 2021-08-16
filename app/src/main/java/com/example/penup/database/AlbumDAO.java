package com.example.penup.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlbumDAO {
    @Insert
    void insertAlbum(Album album);

    @Query("select * from album_table")
    List<Album> getAlbums();

    @Query("select EXISTS(select * from album_table where name = :name)")
    boolean isExist(String name);

    @Query("delete from album_table where id = :albumid")
    void deleteAlbum(int albumid);
}
