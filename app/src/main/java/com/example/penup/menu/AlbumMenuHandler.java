package com.example.penup.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.penup.viewmodel.ListAlbumViewModel;
import com.example.penup.viewmodel.ListFolderViewModel;

public class AlbumMenuHandler {
    private static AlbumMenuHandler INSTANCE = null;
    private AlbumMenuHandler(){};
    public static synchronized AlbumMenuHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AlbumMenuHandler();
        }
        return INSTANCE;
    }
    public boolean deleteAlbum(Activity activity, ListAlbumViewModel viewModel, int albumId){
        new AlertDialog.Builder(activity)
                .setTitle("Delete album? (No picture will be deleted)")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        boolean  result = viewModel.deleteAlbum(activity, albumId);
                        if(result){
                            Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(activity, "Delete fail, Favourite album is default", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }
    public boolean renameAlbum(Activity activity, ListAlbumViewModel viewModel, int position){
        return true;
    }
}
