package com.example.penup.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Date;

public class FileHelper {
    private static FileHelper INSTANCE = null;
    private FileHelper(){};
    public static synchronized FileHelper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FileHelper();
        }
        return INSTANCE;
    }
    public String sizeToString(long bytes){
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
    public String widthHeightToString(int w, int h){
        return String.format("%d x %d",h,w);
    }
    public String durationToString(long duration){
        Date d = new Date(duration);
        Log.d("Vector",duration+"");
        return (String) DateFormat.format("hh:mm:ss",d);
    }
    public File getFileFromPath(String path){
        return new File(path);
    }
    public boolean isExist(File file){
        return file.exists();
    }
    public boolean deleteFolder(String path){
        File file = new File(path);
        for(File child: file.listFiles()){
            if(child.isFile()){
                child.delete();
            }
        }
        return true;
    }
}
