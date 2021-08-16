package com.example.penup.responsitory;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.penup.models.MediaModel;
import com.example.penup.models.RawImage;
import com.example.penup.models.RawVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalStorageResponsitory {
    private static ExternalStorageResponsitory INSTANCE = null;
    private ExternalStorageResponsitory(){};
    public static synchronized ExternalStorageResponsitory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ExternalStorageResponsitory();
        }
        return INSTANCE;
    }
    public ArrayList<RawImage> getRawImages(Context mContext) {
        Uri uri;
        ArrayList<RawImage> listOfAllImages = new ArrayList<RawImage>();
        Cursor cursor;
        String      selection     = MediaStore.Images.Media.DATA + " NOT NULL";
        String      sortBy        = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = getProjectionImage();

        cursor = mContext.getContentResolver().query(uri, projection, selection,
                null, sortBy);
        while (cursor.moveToNext()) {
            listOfAllImages.add(convertToImage(cursor));
        }
        return listOfAllImages;
    }
    private String[] getProjectionImage() {
            String projection[] = {
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.IS_FAVORITE,
                    //MediaStore.Video.Media.IS_TRASHED,
                    //MediaStore.Video.Media.IS_DOWNLOAD
            };
            return projection;
    }
    private RawImage convertToImage(Cursor cursor){
        RawImage image = new RawImage();
        image.setMineType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));
        image.setDateModified(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)));
        image.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)));
        image.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
        image.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
        image.setBucketDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
        image.setWidtd(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)));
        image.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)));
        image.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
        image.setOriantation(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)));
        image.setIsFavourite(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_FAVORITE)));
        //image.setIsTrash(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_TRASHED)));
        //image.setIsDownload(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_DOWNLOAD)));
        return image;
    }

    public ArrayList<RawVideo> getRawVideos(Context mContext) {
        Uri uri;
        ArrayList<RawVideo> listOfAllVideos = new ArrayList<RawVideo>();
        Cursor cursor;
        String      selection     =  MediaStore.Video.Media.DATA + " NOT NULL";
        String      sortBy        = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = getProjectionVideo();

        cursor = mContext.getContentResolver().query(uri, projection, selection,
                null, sortBy);
        while (cursor.moveToNext()) {
            listOfAllVideos.add(convertToVideo(cursor));
        }
        return listOfAllVideos;
    }
    private String[] getProjectionVideo() {
        String projection[] = {
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.IS_FAVORITE,
                //MediaStore.Video.Media.IS_TRASHED,
                //MediaStore.Video.Media.IS_DOWNLOAD
        };
        return projection;
    }
    private RawVideo convertToVideo(Cursor cursor){
        RawVideo video = new RawVideo();
        video.setMineType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
        video.setDateModified(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)));
        video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
        video.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
        video.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));
        video.setBucketDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)));
        video.setWidtd(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)));
        video.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)));
        video.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
        video.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
        video.setIsFavourite(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_FAVORITE)));
        //video.setIsTrash(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_TRASHED)));
        //video.setIsDownload(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_DOWNLOAD)));
        return video;
    }
    public int setIntField(Context context, long mediaID, String field, int value){
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        baseUri = Uri.withAppendedPath(baseUri, ""+ mediaID);

        ContentValues values = new ContentValues();
        values.put(field, value);
        String sIMAGE_ID = "" + mediaID;
        int res = context.getContentResolver().update(baseUri, values, MediaStore.Images.Media._ID + "= ?", new String[]{sIMAGE_ID });
        return res;
    }
    public int setFavourite(Context context, long mediaID, boolean isFavourite){
        int value = 0;
        if(isFavourite){
            value = 1;
        }
        return setIntField(context, mediaID, MediaStore.Images.Media.IS_FAVORITE, value);
    }
    public int setTrash(Context context, long mediaID, boolean isTrash){
        int value = 0;
        if(isTrash){
            value = 1;
        }
        return setIntField(context, mediaID, MediaStore.Images.Media.IS_TRASHED, value);
    }
    public int setDownload(Context context, long mediaID, boolean isDownload){
        int value = 0;
        if(isDownload){
            value = 1;
        }
        return setIntField(context, mediaID, MediaStore.Images.Media.IS_DOWNLOAD, value);
    }
    public int deleteMediaFromStorage(Context context, int mediaID){
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        baseUri = Uri.withAppendedPath(baseUri, ""+ mediaID);

        String sIMAGE_ID = "" + mediaID;
        int res = context.getContentResolver().delete(baseUri, MediaStore.Images.Media._ID + "= ?", new String[]{sIMAGE_ID });
        return res;
    }
}
