package com.example.penup.utils;

import android.provider.MediaStore;

import com.example.penup.models.MediaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MediaSorter {
    private static MediaSorter INSTANCE = null;
    private MediaSorter(){};
    public static synchronized MediaSorter getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MediaSorter();
        }
        return INSTANCE;
    }
    public class LongComparator implements Comparator<MediaModel> {
        @Override
        public int compare(MediaModel o1, MediaModel o2) {
            if (o1.getDate()>o2.getDate()) {
                return -1;
            }else if(o1.getDate()<o2.getDate()){
                return 1;
            }else{
                return 0;
            }
        }
    }
    public void SortByDate(ArrayList<MediaModel> list){
        Collections.sort(list, new LongComparator());
    }
}
