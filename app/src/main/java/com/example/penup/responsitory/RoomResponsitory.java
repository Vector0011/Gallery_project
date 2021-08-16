package com.example.penup.responsitory;

import android.content.Context;

import com.example.penup.database.Album;
import com.example.penup.database.AlbumData;
import com.example.penup.database.AlbumDatabase;

import java.util.ArrayList;
import java.util.List;

public class RoomResponsitory {
    private static RoomResponsitory INSTANCE = null;
    private RoomResponsitory(){};
    public static synchronized RoomResponsitory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RoomResponsitory();
        }
        return INSTANCE;
    }
    private List<Album> listAlbumCacheData;
    private List<AlbumData> listAlbumDataCacheData;
    public List<Album> getListAlbum(Context context){
        List<Album> list;
        if(listAlbumCacheData==null) {
            list = AlbumDatabase.getInstance(context).albumDAO().getAlbums();
            listAlbumCacheData = list;
        }else{
            list = listAlbumCacheData;
        }
        return list;
    }
    public List<AlbumData> getListAlbumData(Context context){
        List<AlbumData> list;
        if(listAlbumDataCacheData == null) {
            list = AlbumDatabase.getInstance(context).albumDataDAO().getAlbumDatas();
            listAlbumDataCacheData = list;
        }else{
            list = listAlbumDataCacheData;
        }
        return list;
    }
    public void clearCache(){
        listAlbumCacheData = null;
        listAlbumDataCacheData = null;
    }
    public boolean addAlbum(Context context, Album album){
        if(AlbumDatabase.getInstance(context).albumDAO().isExist(album.getName())){
            return false;
        }else{
            AlbumDatabase.getInstance(context).albumDAO().insertAlbum(album);
            return true;
        }
    }

    public boolean deleteAlbumdata(Context context, int albumId, List<Integer> listMedia){
        for(int i = 0; i<listMedia.size(); i++){
            if(albumId == -1){
                ExternalStorageResponsitory.getInstance().setFavourite(context, listMedia.get(i),false);
            }else{
                AlbumDatabase.getInstance(context).albumDataDAO().deleteAlbumData(albumId,listMedia.get(i));
            }
        }
        if(albumId == -1){
            MediaResponsitory.getInstance().clearCache();
        }else
            clearCache();
        return true;
    }
    public boolean deleteAlbum(Context context, int albumId){
        if(albumId == -1){
            return false;
        }else{
            AlbumDatabase.getInstance(context).albumDAO().deleteAlbum(albumId);
        }
        return  true;
    }
    public void addAlbumData(Context context, int albumId, int mediaId){
        if(!AlbumDatabase.getInstance(context).albumDataDAO().isExist(albumId,mediaId)){
            AlbumData data = new AlbumData();
            data.setAlbum_id(albumId);
            data.setMedia_id(mediaId);
            AlbumDatabase.getInstance(context).albumDataDAO().insertAlbumData(data);
        }
    }
    public boolean isCacheNull(){
        if(listAlbumCacheData == null || listAlbumDataCacheData==null){
            return true;
        }
        return  false;
    }
}
