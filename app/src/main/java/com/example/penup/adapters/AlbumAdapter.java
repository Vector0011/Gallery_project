package com.example.penup.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.penup.R;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.utils.DateTimeHelper;

import java.io.File;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AblumViewHolder> {
    private Context mContext;
    private List<ListMediaByAlbum> mListMedia;
    private RecycleViewClickItem clickItem;

    public AlbumAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ListMediaByAlbum> mListMedia){
        this.mListMedia = mListMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AblumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_album_item,parent,false);
        return new AblumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AblumViewHolder holder, int p) {
        ListMediaByAlbum album = mListMedia.get(p);
        if(album == null){
            return;
        }
        holder.tvTitle.setText(album.getAlbumName());
        holder.tvCount.setText(album.getListMedia().size()+" media");
        holder.rlMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view,album.getAlbumId());
            }
        });
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view,album.getAlbumId());
            }
        });
        if(album.getListMedia().size()==0){
            Glide.with(mContext).load(R.drawable.image_icon).into(holder.ivIcon);
        }else{
            Uri uri = Uri.fromFile(new File(album.getListMedia().get(0).getPath()));
            Glide.with(mContext).load(uri).into(holder.ivIcon);
        }
    }

    @Override
    public int getItemCount() {
        if(mListMedia != null){
            return mListMedia.size();
        }
        return 0;
    }

    public void setCallBack(RecycleViewClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public class AblumViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvCount;
        ImageView ivIcon;
        RelativeLayout rlMiddle;
        ImageView ivMore;
        public AblumViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_first_line);
            tvCount = itemView.findViewById(R.id.tv_second_line);
            ivIcon = itemView.findViewById(R.id.iv_picture_icon);
            rlMiddle = itemView.findViewById(R.id.rl_middle);
            ivMore = itemView.findViewById(R.id.iv_properties);
        }
    }
}
