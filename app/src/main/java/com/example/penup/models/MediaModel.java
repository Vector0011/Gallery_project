package com.example.penup.models;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.Serializable;

public class MediaModel extends BaseObservable implements Serializable {
    public static int TYPE_IMAGE = 0;
    public static int TYPE_VIDEO = 1;
    private int index;
    private int id;
    private int type;
    private String title;
    private long date;
    private String path;
    private String folder;
    private String bucket;
    private int width;
    private int height;
    private long size;
    private String mineType;
    private int oriantation;
    private long duration;
    private int isFavourite;
    private int isTrash;
    private int isDownload;

    @Bindable
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyPropertyChanged(BR.index);
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        notifyPropertyChanged(BR.path);
    }

    @Bindable
    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
        notifyPropertyChanged(BR.folder);
    }

    @Bindable
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
        notifyPropertyChanged(BR.bucket);
    }

    @Bindable
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        notifyPropertyChanged(BR.width);
    }

    @Bindable
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        notifyPropertyChanged(BR.height);
    }

    @Bindable
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }

    @Bindable
    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
        notifyPropertyChanged(BR.mineType);
    }

    @Bindable
    public int getOriantation() {
        return oriantation;
    }

    public void setOriantation(int oriantation) {
        this.oriantation = oriantation;
        notifyPropertyChanged(BR.oriantation);
    }

    @Bindable
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public int getFavourite() {
        return isFavourite;
    }

    public void setFavourite(int favourite) {
        isFavourite = favourite;
        notifyPropertyChanged(BR.favourite);
    }

    @Bindable
    public int getTrash() {
        return isTrash;
    }

    public void setTrash(int trash) {
        isTrash = trash;
        notifyPropertyChanged(BR.trash);
    }

    @Bindable
    public int getDownload() {
        return isDownload;
    }

    public void setDownload(int download) {
        isDownload = download;
        notifyPropertyChanged(BR.download);
    }

    @NonNull
    @Override
    public String toString() {
        return "date: "+date+ "|title: "+title+"minetype: "+mineType+"|data: "+path+"id: "+id+"|bucket: "+folder+"|w:"+width+"|H:"+height+"|size:"+size+"|Ori:"+oriantation+"|duration:"+duration+"|isTrash"+isTrash+"|isDownload"+isDownload;
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String imageUrl){
        Glide.with(imageView).load(Uri.fromFile(new File(imageUrl))).into(imageView);
    }
}
