package com.example.penup.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.penup.R;
import com.example.penup.databinding.LayoutAlbumMediaItemBinding;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.models.MediaModel;
import com.example.penup.utils.DateTimeHelper;

import java.io.File;
import java.util.List;

public class AlbumMediaAdapter extends SelectableAdapter<AlbumMediaAdapter.MediaViewHolder> {
    private Context mContext;
    private List<MediaModel> mListMedia;
    private RecycleViewClickItem clickItem;

    public AlbumMediaAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(List<MediaModel> mListMedia){
        this.mListMedia = mListMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumMediaAdapter.MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutAlbumMediaItemBinding mediaItemBinding = LayoutAlbumMediaItemBinding.inflate(layoutInflater,parent,false);
        return new MediaViewHolder(mediaItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumMediaAdapter.MediaViewHolder holder, int position) {
        MediaModel media = mListMedia.get(position);
        if(media == null){
            return;
        }
        int index = position;
        holder.mediaItemBinding.setMedia(media);
        holder.mediaItemBinding.setAdapter(this);
        holder.mediaItemBinding.setPosition(index);
        holder.mediaItemBinding.executePendingBindings();
        holder.mediaItemBinding.selectedOverlay.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               clickItem.onLongClick(view, index);
               Log.d("Vector", "long invisible");
               return true;
           }
        });
        holder.mediaItemBinding.selectedOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view, index);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(mListMedia!=null){
            return mListMedia.size();
        }
        return 0;
    }

    public void setCallBack(RecycleViewClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        LayoutAlbumMediaItemBinding mediaItemBinding;
        public MediaViewHolder(@NonNull LayoutAlbumMediaItemBinding mediaItemBinding) {
            super(mediaItemBinding.getRoot());
            this.mediaItemBinding = mediaItemBinding;
        }
    }
}
