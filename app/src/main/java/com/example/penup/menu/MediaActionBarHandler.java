package com.example.penup.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.example.penup.BuildConfig;
import com.example.penup.activities.AlbumMediaActivity;
import com.example.penup.activities.MainActivity;
import com.example.penup.database.Album;
import com.example.penup.dialog.SpinnerDialog;
import com.example.penup.fragments.AlbumFragment;
import com.example.penup.fragments.MediaFragment;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.responsitory.RoomResponsitory;
import com.example.penup.utils.CopyPasteUtil;
import com.example.penup.utils.InputUtil;
import com.example.penup.viewmodel.ListAlbumViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaActionBarHandler {
    private static MediaActionBarHandler INSTANCE = null;
    private MediaActionBarHandler(){};
    public static synchronized MediaActionBarHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MediaActionBarHandler();
        }
        return INSTANCE;
    }

    public void delete(MediaFragment fragment, List<MediaModel> selectedMedia) {
        new AlertDialog.Builder(fragment.getActivity())
                .setTitle("Delete media from your phone storage")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        try{
                            for(int i = 0; i<selectedMedia.size(); i++){
                                ExternalStorageResponsitory.getInstance().deleteMediaFromStorage(fragment.getActivity(), selectedMedia.get(i).getId());
                            }
                            MediaResponsitory.getInstance().clearCache();
                            fragment.updateData();
                            Toast.makeText(fragment.getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(fragment.getActivity(), "Delete fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void share(Activity activity, List<MediaModel> listSelected){
        List<String> listPath = new ArrayList<>();
        int typeImage = 0;
        int typeVideo = 0;
        for(int i = 0; i< listSelected.size(); i++){
            listPath.add(listSelected.get(i).getPath());
            if(listSelected.get(i).getType() == MediaModel.TYPE_IMAGE){
                typeImage = 1;
            }
            if(listSelected.get(i).getType() == MediaModel.TYPE_VIDEO){
                typeVideo = 1;
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        if(typeImage+typeVideo==2){
            intent.setType("*/*");
        }else if(typeImage == 1){
            intent.setType("image/*");
        }else{
            intent.setType("video/*");
        }

        ArrayList<Uri> files = new ArrayList<Uri>();

        for(String path : listPath /* List of the files you want to send */) {
            File file = new File(path);
            Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID+".provider",file);
            files.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent shareIntent = Intent.createChooser(intent, null);
        activity.startActivity(shareIntent);
    }
    public void addToAlbum(MainActivity activity, List<MediaModel> selected){
        List<Album> raw = RoomResponsitory.getInstance().getListAlbum(activity);
        List<Album> listAlbum = new ArrayList<>();
        listAlbum.add( MediaConverter.getInstance().getFavouriteAlbum());
        for(int i = 0; i<raw.size(); i++){
            listAlbum.add(raw.get(i));
        }
        ArrayList<String> albumName = new ArrayList<>();
        for(int i = 0; i< listAlbum.size(); i++){
            albumName.add(listAlbum.get(i).getName());
        }
        SpinnerDialog mSpinnerDialog = new SpinnerDialog(activity, albumName, new SpinnerDialog.DialogListener() {
            public void cancelled() {
            }
            public void ready(int n) {
                try {
                    Log.d("Vector", " readdy" + n);
                    int albumId = listAlbum.get(n).getId();
                    for (int i = 0; i < selected.size(); i++) {
                        if(albumId==-1) {
                            ExternalStorageResponsitory.getInstance().setFavourite(activity,selected.get(i).getId(),true);
                        }else{
                            RoomResponsitory.getInstance().addAlbumData(activity, albumId, selected.get(i).getId());
                        }
                    }
                    if(albumId!=-1){
                        RoomResponsitory.getInstance().clearCache();
                        AlbumFragment albumFragment = (AlbumFragment) activity.getPagerAdapter().getFragment(1);
                        if(albumFragment!=null && albumFragment.getViewModel()!=null){
                            albumFragment.getViewModel().requestData(activity);
                        }
                    }
                    Toast.makeText(activity, "add media to "+listAlbum.get(n).getName()+" successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSpinnerDialog.show();
    }

    public void copy( List<MediaModel> selectedMedia) {
        ArrayList<String> listPath = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        for(MediaModel media : selectedMedia){
            listPath.add(media.getFolder());
            listName.add(media.getPath().substring(media.getPath().lastIndexOf("/")+1));
        }
        CopyPasteUtil.getInstance().copy(listPath,listName);
    }
}
