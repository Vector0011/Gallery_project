package com.example.penup.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.penup.database.Album;
import com.example.penup.interfaces.TaskCompleteCallback;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.ListMediaByFolder;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.responsitory.RoomResponsitory;
import com.example.penup.utils.FileHelper;
import com.example.penup.viewmodelutil.ObservableViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFolderViewModel extends ObservableViewModel{
    private MutableLiveData<List<ListMediaByFolder>> listMedia;

    public ListFolderViewModel(){
        listMedia = new MutableLiveData<>();
    }
    public void requestData(Context mContext){
        listMedia.setValue(MediaConverter.getInstance().getListMediaByFolder(mContext));
    }
    public LiveData<List<ListMediaByFolder>> getListMedia(){
        return listMedia;
    }

    public boolean deleteFolder(Activity activity, int position) {
        List<MediaModel> list = new ArrayList<>();
        for(int i = 0; i< listMedia.getValue().size(); i++){
            if(listMedia.getValue().get(i).getPath().hashCode() == position){
                list = listMedia.getValue().get(i).getListMedia();
            }
        }
        for(int i = 0; i< list.size(); i++){
            ExternalStorageResponsitory.getInstance().deleteMediaFromStorage(activity, list.get(i).getId());
        }
        MediaResponsitory.getInstance().clearCache();
        requestData(activity);
        return true;
    }
}