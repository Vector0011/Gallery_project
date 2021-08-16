package com.example.penup.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.penup.BuildConfig;
import com.example.penup.activities.AlbumMediaActivity;
import com.example.penup.activities.FolderMediaActivity;
import com.example.penup.database.Album;
import com.example.penup.fragments.AlbumFragment;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.ListMediaByFolder;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.example.penup.responsitory.RoomResponsitory;
import com.example.penup.utils.InputUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumActionBarHandler {
    private static AlbumActionBarHandler INSTANCE = null;
    private AlbumActionBarHandler(){};
    public static synchronized AlbumActionBarHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AlbumActionBarHandler();
        }
        return INSTANCE;
    }
    public void addAlbum(AlbumFragment fragment){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

        builder.setTitle("Create a new album");
        builder.setMessage("Input album name:");
        final EditText input = new EditText(fragment.getActivity());
        builder.setView(input);
        input.requestFocus();
        InputUtil.requestInputForDialog(fragment.getActivity());
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                InputUtil.hideInputForDialog(fragment.getActivity());
            }
        });
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Album album = new Album();
                album.setName(input.getText().toString().trim());
                boolean result = fragment.addAlbum(album);
                if(result){
                    Toast.makeText(fragment.getActivity(),"Create album successfully",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(fragment.getActivity(),"Error! Make sure album name unique.",Toast.LENGTH_SHORT).show();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setEnabled(true);
                }else{
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
    public void removeFromAlbum(AlbumMediaActivity activity, ListMediaByAlbum mediaByAlbum, List<Integer> listSelected){
        new AlertDialog.Builder(activity)
                .setTitle("Delete media from "+mediaByAlbum.getAlbumName())
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        List<Integer> listId = new ArrayList<>();
                        List<MediaModel> listMedia = mediaByAlbum.getListMedia();
                        for(int i = 0; i< listSelected.size(); i++){
                            listId.add(listMedia.get(listSelected.get(i)).getId());
                        }
                        boolean result = RoomResponsitory.getInstance().deleteAlbumdata(activity, mediaByAlbum.getAlbumId(), listId);
                        if(result){
                            Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show();
                            activity.reloadData();
                        }else{
                            Toast.makeText(activity, "Delete fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void shareFromAlbum(AlbumMediaActivity activity, ListMediaByAlbum mediaByAlbum, List<Integer> listSelected){
        List<String> listPath = new ArrayList<>();
        List<MediaModel> listMedia = mediaByAlbum.getListMedia();
        int typeImage = 0;
        int typeVideo = 0;
        for(int i = 0; i< listSelected.size(); i++){
            listPath.add(listMedia.get(listSelected.get(i)).getPath());
            if(listMedia.get(listSelected.get(i)).getType() == MediaModel.TYPE_IMAGE){
                typeImage = 1;
            }
            if(listMedia.get(listSelected.get(i)).getType() == MediaModel.TYPE_VIDEO){
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
}
