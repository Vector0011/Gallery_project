package com.example.penup.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.penup.models.ListMediaByFolder;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;

import java.util.List;

public class FolderMediaViewModel extends ViewModel {
    private MutableLiveData<ListMediaByFolder> listMedia;

    public FolderMediaViewModel(){
        listMedia = new MutableLiveData<>();
    }
    public void requestData(Context mContext, int pathId){
        listMedia.setValue(MediaConverter.getInstance().getListMediaFromPath(mContext,pathId));
    }
    public LiveData<ListMediaByFolder> getListMedia(){
        return listMedia;
    }

    public boolean delete(Context context, List<Integer> selected, int pathId){
        for(int i = 0; i< selected.size(); i++){
            MediaModel media = listMedia.getValue().getListMedia().get(selected.get(i));
            ExternalStorageResponsitory.getInstance().deleteMediaFromStorage(context, media.getId());
        }
        MediaResponsitory.getInstance().clearCache();
        requestData(context, pathId);
        return true;
    }
}
