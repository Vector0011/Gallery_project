package com.example.penup.responsitory;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.example.penup.database.Album;
import com.example.penup.database.AlbumData;
import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.ListMediaByFolder;
import com.example.penup.models.MediaModel;
import com.example.penup.models.RawImage;
import com.example.penup.models.RawVideo;
import com.example.penup.utils.DateTimeHelper;
import com.example.penup.utils.PathUtil;

import org.joda.time.DateTimeComparator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class MediaConverter {
    private static MediaConverter INSTANCE = null;
    private MediaConverter(){};
    public static synchronized MediaConverter getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MediaConverter();
        }
        return INSTANCE;
    }
    public MediaModel rawImageToMedia(RawImage rawImage){
        MediaModel mediaModel = new MediaModel();
        mediaModel.setId(rawImage.getId());
        mediaModel.setType(MediaModel.TYPE_IMAGE);
        mediaModel.setTitle(rawImage.getTitle());
        mediaModel.setDate(rawImage.getDateModified());
        mediaModel.setPath(rawImage.getData());
        mediaModel.setFolder(PathUtil.getInstance().getFolderFromPath(rawImage.getData()));
        mediaModel.setBucket(rawImage.getBucketDisplayName());
        mediaModel.setWidth(rawImage.getWidtd());
        mediaModel.setHeight(rawImage.getHeight());
        mediaModel.setSize(rawImage.getSize());
        mediaModel.setMineType(rawImage.getMineType());
        mediaModel.setOriantation(rawImage.getOriantation());
        mediaModel.setFavourite(rawImage.getIsFavourite());
        mediaModel.setTrash(rawImage.getIsTrash());
        mediaModel.setDownload(rawImage.getIsDownload());
        return mediaModel;
    }
    public MediaModel rawVideoToMedia(RawVideo rawVideo){
        MediaModel mediaModel = new MediaModel();
        mediaModel.setId(rawVideo.getId());
        mediaModel.setType(MediaModel.TYPE_VIDEO);
        mediaModel.setTitle(rawVideo.getTitle());
        mediaModel.setDate(rawVideo.getDateModified());
        mediaModel.setPath(rawVideo.getData());
        mediaModel.setFolder(PathUtil.getInstance().getFolderFromPath(rawVideo.getData()));
        mediaModel.setBucket(rawVideo.getBucketDisplayName());
        mediaModel.setWidth(rawVideo.getWidtd());
        mediaModel.setHeight(rawVideo.getHeight());
        mediaModel.setSize(rawVideo.getSize());
        mediaModel.setMineType(rawVideo.getMineType());
        mediaModel.setDuration(rawVideo.getDuration());
        mediaModel.setFavourite(rawVideo.getIsFavourite());
        mediaModel.setTrash(rawVideo.getIsTrash());
        mediaModel.setDownload(rawVideo.getIsDownload());
        return mediaModel;
    }
    public ArrayList<MediaModel> mergeVideoAndImage(ArrayList<RawImage> listImage, ArrayList<RawVideo> listVideo){
        ArrayList<MediaModel> list = new ArrayList<>();
        for(int i = 0; i<listImage.size(); i++){
            list.add(rawImageToMedia(listImage.get(i)));
        }
        for(int i = 0; i<listVideo.size(); i++){
            list.add(rawVideoToMedia(listVideo.get(i)));
        }
        return list;
    }
    public List<GridMediaByDate> getListMediaByDate(Context context){
        List<MediaModel> sourceMedia = MediaResponsitory.getInstance().getListMedia(context);
        List<GridMediaByDate> listResult = new ArrayList<>();

        for(int i = 0; i< sourceMedia.size(); i++){
            sourceMedia.get(i).setIndex(i);
            Date d = new Date(sourceMedia.get(i).getDate()*1000);
            if(listResult.size() == 0 || DateTimeHelper.getInstance().compareDateWithoutTime(d,listResult.get(listResult.size()-1).getDate())<0){
                GridMediaByDate media = new GridMediaByDate();
                media.setDate(d);
                media.getmListMedia().add(sourceMedia.get(i));
                listResult.add(media);
            }else{
                listResult.get(listResult.size()-1).getmListMedia().add(sourceMedia.get(i));
            }
        }
        return listResult;
    }
    public ListMediaByFolder getListMediaFromPath(Context context, int pathId){
        List<ListMediaByFolder> list = getListMediaByFolder(context);
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getPath().hashCode() == pathId){
                return list.get(i);
            }
        }
        return new ListMediaByFolder();
    }
    public ListMediaByAlbum getListMediaFromAlbum(Context context, int albumId){
        List<ListMediaByAlbum> list = getListMediaByAlbum(context);
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getAlbumId() == albumId){
                return list.get(i);
            }
        }
        return new ListMediaByAlbum();
    }
    public List<ListMediaByFolder> getListMediaByFolder(Context context){
        List<MediaModel> listMedia = MediaResponsitory.getInstance().getListMedia(context);
        List<ListMediaByFolder> result = getListFolder(listMedia);
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i< result.size(); i++){
            map.put(result.get(i).getPath(),i);
        }
        for(int i = 0; i<listMedia.size(); i++){
            int index = map.get(listMedia.get(i).getFolder());
            result.get(index).getListMedia().add(listMedia.get(i));
        }
        return result;
    }

    private List<ListMediaByFolder> getListFolder(List<MediaModel> listMedia) {
        List<ListMediaByFolder> result = new ArrayList<>();
        List<Pair<String,String>> listPath = new ArrayList<>();
        for(int i = 0; i<listMedia.size(); i++){
            listPath.add(new Pair(listMedia.get(i).getFolder(),listMedia.get(i).getBucket()));
        }
        Set<Pair<String,String>> uniPath = new HashSet<Pair<String, String>>(listPath);
        for(Pair<String,String> item : uniPath){
            result.add(new ListMediaByFolder(item.first,item.second));
        }
        return result;
    }

    public List<ListMediaByAlbum> getListMediaByAlbum(Context context){
        List<MediaModel> listMedia = MediaResponsitory.getInstance().getListMedia(context);
        List<Album> albums = RoomResponsitory.getInstance().getListAlbum(context);
        List<AlbumData> data = RoomResponsitory.getInstance().getListAlbumData(context);
        List<ListMediaByAlbum> result = new ArrayList<>();
        result.add(getFavouriteMediaAlbum(listMedia));
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i = 0; i< listMedia.size(); i++){
            map.put(listMedia.get(i).getId(), i);
        }
        for(int i = 0; i<albums.size(); i++){
            Album a = albums.get(i);
            ListMediaByAlbum mediaByAlbum = new ListMediaByAlbum();
            mediaByAlbum.setAlbumId(a.getId());
            mediaByAlbum.setAlbumName(a.getName());
            for(int j = 0; j<data.size(); j++){
                AlbumData d = data.get(j);
                if(d.getAlbum_id() == a.getId() && map.get(d.getMedia_id())!= null){
                    mediaByAlbum.getListMedia().add(listMedia.get(map.get(d.getMedia_id())));
                }
            }
            result.add(mediaByAlbum);
        }

        return result;
    }
    public ListMediaByAlbum getFavouriteMediaAlbum(List<MediaModel> listMedia){
        ListMediaByAlbum favourite = new ListMediaByAlbum();
        favourite.setAlbumId(-1);
        favourite.setAlbumName("Favourite");
        for(int i = 0; i< listMedia.size(); i++){
            if(listMedia.get(i).getFavourite()==1) {
                favourite.getListMedia().add(listMedia.get(i));
            }
        }
        return favourite;
    }
    public Album getFavouriteAlbum(){
        Album album = new Album();
        album.setName("Favourite");
        album.setId(-1);
        return album;
    }
}
