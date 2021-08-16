package com.example.penup.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.viewmodelutil.ObservableViewModel;

import java.util.List;

public class ListMediaViewModel extends ObservableViewModel {
    private MutableLiveData<List<GridMediaByDate>> listMedia;

    public ListMediaViewModel(){
        listMedia = new MutableLiveData<>();
    }
    public void requestData(Context mContext){
        listMedia.setValue(MediaConverter.getInstance().getListMediaByDate(mContext));
    }
    public LiveData<List<GridMediaByDate>> getListMedia(){
        return listMedia;
    }
}
