package com.example.penup.models;

import android.provider.MediaStore;

import androidx.annotation.NonNull;

public class RawImage {
    public RawImage(){
    }
    private long dateModified;
    private String title;
    private String data;
    private int id;
    private String bucketDisplayName;
    private int widtd;
    private int height;
    private long size;
    private String mineType;
    private int oriantation;
    private int isFavourite;
    private int isTrash;
    private int isDownload;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public int getWidtd() {
        return widtd;
    }

    public void setWidtd(int widtd) {
        this.widtd = widtd;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public int getOriantation() {
        return oriantation;
    }

    public void setOriantation(int oriantation) {
        this.oriantation = oriantation;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getIsTrash() {
        return isTrash;
    }

    public void setIsTrash(int isTrash) {
        this.isTrash = isTrash;
    }

    public int getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(int isDownload) {
        this.isDownload = isDownload;
    }
}
