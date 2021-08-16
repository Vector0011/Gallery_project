package com.example.penup.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlbumDataDAO {
    @Insert
    void insertAlbumData(AlbumData albumData);

    @Query("select * from album_data_table")
    List<AlbumData> getAlbumDatas();

    @Query("select * from album_data_table where album_id = :albumid")
    List<AlbumData> getAlbumDataFromId(int albumid);

    @Query("delete from album_data_table where album_id = :albumid and media_id = :mediaid")
    void deleteAlbumData(int albumid, int mediaid);

    @Query("select EXISTS(select * from album_data_table where album_id = :albumId and media_id = :mediaId)")
    boolean isExist(int albumId, int mediaId);
}
