package com.example.penup.responsitory;

import android.content.Context;
import android.util.Log;

import com.example.penup.activities.MainActivity;
import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.MediaModel;
import com.example.penup.models.RawImage;
import com.example.penup.models.RawVideo;
import com.example.penup.utils.MediaSorter;

import java.util.ArrayList;
import java.util.List;

public class MediaResponsitory {
    private static MediaResponsitory INSTANCE = null;
    private MediaResponsitory(){};
    public static synchronized MediaResponsitory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MediaResponsitory();
        }
        return INSTANCE;
    }
    private ArrayList<MediaModel> listMediaCacheData;
    public ArrayList<MediaModel> getListMedia(Context mContext){
        ArrayList<MediaModel> list;
        if(listMediaCacheData == null){
            ArrayList<RawImage> listImage = ExternalStorageResponsitory.getInstance().getRawImages(mContext );
            ArrayList<RawVideo> listVideo = ExternalStorageResponsitory.getInstance().getRawVideos(mContext );

            list = MediaConverter.getInstance().mergeVideoAndImage(listImage,listVideo);
            MediaSorter.getInstance().SortByDate(list);

            Log.d("Vector","Size:" + list.size());
            listMediaCacheData = list;
        }else{
            list = listMediaCacheData;
            Log.d("Vector","Size:" + list.size() + " (Using cache)");
        }
        return list;
    }
    public void clearCache(){
        listMediaCacheData = null;
    }
}
