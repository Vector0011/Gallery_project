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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.penup.R;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.ListMediaByFolder;

import java.io.File;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder>{
    private Context mContext;
    private List<ListMediaByFolder> mListMedia;
    private RecycleViewClickItem clickItem;

    public FolderAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ListMediaByFolder> mListMedia){
        this.mListMedia = mListMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_album_item,parent,false);
        return new FolderAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.FolderViewHolder holder, int p) {
        ListMediaByFolder folder = mListMedia.get(p);
        if(folder == null){
            return;
        }
        holder.tvTitle.setText(folder.getName());
        holder.tvCount.setText(folder.getListMedia().size()+" media");
        holder.rlMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view,folder.getPath().hashCode());
            }
        });
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view, folder.getPath().hashCode());
            }
        });
        if(folder.getListMedia().size()!=0){
            Uri uri = Uri.fromFile(new File(folder.getListMedia().get(0).getPath()));
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

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvCount;
        ImageView ivIcon;
        RelativeLayout rlMiddle;
        ImageView ivMore;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_first_line);
            tvCount = itemView.findViewById(R.id.tv_second_line);
            ivIcon = itemView.findViewById(R.id.iv_picture_icon);
            rlMiddle = itemView.findViewById(R.id.rl_middle);
            ivMore = itemView.findViewById(R.id.iv_properties);
        }
    }
}
