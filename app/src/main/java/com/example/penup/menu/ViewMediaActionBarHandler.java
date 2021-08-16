package com.example.penup.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.penup.BuildConfig;
import com.example.penup.activities.MediaDetailActivity;
import com.example.penup.activities.ViewMediaActivity;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.ExternalStorageResponsitory;
import com.example.penup.responsitory.MediaResponsitory;

import java.io.File;
import java.util.ArrayList;

public class ViewMediaActionBarHandler {
    private static ViewMediaActionBarHandler INSTANCE = null;
    private ViewMediaActionBarHandler(){};
    public static synchronized ViewMediaActionBarHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ViewMediaActionBarHandler();
        }
        return INSTANCE;
    }
    public void share(Activity activity, MediaModel mediaModel){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        if(mediaModel.getType() == MediaModel.TYPE_IMAGE){
            intent.setType("image/*");
        }else{
            intent.setType("video/*");
        }
        ArrayList<Uri> files = new ArrayList<Uri>();

        File file = new File(mediaModel.getPath());
        Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID+".provider",file);
        files.add(uri);

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent shareIntent = Intent.createChooser(intent, null);
        activity.startActivity(shareIntent);
    }
    public void delete(ViewMediaActivity activity, MediaModel mediaModel, int index){
        new AlertDialog.Builder(activity)
                .setTitle("Delete media from your phone storage")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int result = ExternalStorageResponsitory.getInstance().deleteMediaFromStorage(activity, mediaModel.getId());
                        if(result == 1){
                            Toast.makeText(activity, "Delete successfully", Toast.LENGTH_SHORT).show();
                            activity.deleteMedia(index);
                            MediaResponsitory.getInstance().clearCache();
                        }else{
                            Toast.makeText(activity, "Delete fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void detail(ViewMediaActivity viewMediaActivity, MediaModel media) {
        Intent intent = new Intent(viewMediaActivity, MediaDetailActivity.class);
        intent.putExtra("media", media);
        viewMediaActivity.startActivity(intent);
    }

    public void favourite(ViewMediaActivity activity, MediaModel media){
        if(media.getFavourite() == 1){
            media.setFavourite(0);
        }else{
            media.setFavourite(1);
        }
        ExternalStorageResponsitory.getInstance().setFavourite(activity,media.getId(),media.getFavourite()==1);
    }
}
