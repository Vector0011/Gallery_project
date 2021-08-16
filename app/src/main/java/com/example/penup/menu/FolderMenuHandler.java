package com.example.penup.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.penup.BuildConfig;
import com.example.penup.activities.FolderMediaActivity;
import com.example.penup.interfaces.TaskCompleteCallback;
import com.example.penup.models.ListMediaByFolder;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.utils.CopyPasteUtil;
import com.example.penup.viewmodel.FolderMediaViewModel;
import com.example.penup.viewmodel.ListFolderViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderMenuHandler {
    private static FolderMenuHandler INSTANCE = null;
    private FolderMenuHandler(){};
    public static synchronized FolderMenuHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FolderMenuHandler();
        }
        return INSTANCE;
    }
    public boolean deleteFolder(Activity activity, ListFolderViewModel viewModel, int position){
        new AlertDialog.Builder(activity)
                .setTitle("Delete media from your phone storage")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        boolean  result = viewModel.deleteFolder(activity, position);
                        if(result){
                            Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(activity, "Delete fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }
    public boolean renameFolder(Activity activity, ListFolderViewModel viewModel, int position){
        return true;
    }
    public boolean paste(FolderMediaActivity activity, String path){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable update = new Runnable() {
            @Override
            public void run() {
                activity.reloadData();
            }
        };
        CopyPasteUtil.getInstance().paste(activity,path, pathDone -> {
            // Create a handler that associated with Looper of the main thread
            String paths[] = {pathDone};
            MediaScannerConnection.scanFile(activity,
                    paths,
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path1, Uri uri) {
                            mainHandler.removeCallbacks(update);
                            mainHandler.postDelayed(update,500);
                        }
                    });
        });
        return true;
    }
    public void shareFromFolder(FolderMediaActivity activity, ListMediaByFolder mediaByAlbum, List<Integer> listSelected){
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

    public void delete(FolderMediaActivity activity, FolderMediaViewModel viewModel, List<Integer> selectedItems, int pathId) {
        new AlertDialog.Builder(activity)
                .setTitle("Delete media from your phone storage")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        boolean  result = viewModel.delete(activity, selectedItems, pathId);
                        if(result){
                            Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(activity, "Delete fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
