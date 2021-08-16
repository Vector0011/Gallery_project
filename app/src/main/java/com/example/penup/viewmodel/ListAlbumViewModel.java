package com.example.penup.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.penup.database.Album;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.responsitory.RoomResponsitory;
import com.example.penup.viewmodelutil.ObservableViewModel;

import java.util.List;

public class ListAlbumViewModel extends ObservableViewModel {
    private MutableLiveData<List<ListMediaByAlbum>> listMedia;

    public ListAlbumViewModel(){
        listMedia = new MutableLiveData<>();
    }
    public void requestData(Context mContext){
        listMedia.setValue(MediaConverter.getInstance().getListMediaByAlbum(mContext));
    }
    public LiveData<List<ListMediaByAlbum>> getListMedia(){
        return listMedia;
    }
    public boolean addAlbum(Context context, Album album){
        boolean result = RoomResponsitory.getInstance().addAlbum(context,album);
        if(result){
            RoomResponsitory.getInstance().clearCache();
            listMedia.setValue(MediaConverter.getInstance().getListMediaByAlbum(context));
        }
        return result;
    }

    public boolean deleteAlbum(Activity activity, int albumId) {
        boolean result = RoomResponsitory.getInstance().deleteAlbum(activity,albumId);
        if(result){
            RoomResponsitory.getInstance().clearCache();
            listMedia.setValue(MediaConverter.getInstance().getListMediaByAlbum(activity));
        }
        return result;
    }
}