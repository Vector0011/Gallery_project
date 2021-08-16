package com.example.penup.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penup.R;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.MediaModel;
import com.example.penup.utils.DateTimeHelper;

import java.util.HashMap;
import java.util.List;

public class ListMediaAdapter extends SelectableAdapter<ListMediaAdapter.GridMediaViewHolder> {
    private Context mContext;
    private List<GridMediaByDate> mListMedia;
    private RecycleViewClickItem clickItem;
    HashMap<Integer, GridMediaAdapter> mAdapterReferenceMap = new HashMap<>();

    public ListMediaAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<GridMediaByDate> mListMedia){
        mAdapterReferenceMap.clear();
        this.mListMedia = mListMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_media_item,parent,false);
        return new GridMediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridMediaViewHolder holder, int p) {
        GridMediaByDate gridMedia = mListMedia.get(p);
        if(gridMedia == null){
            return;
        }
        holder.tvDate.setText(DateTimeHelper.getInstance().getStringWithoutTime(gridMedia.getDate()));
        int tempP = p;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3);
        holder.rvGridMedia.setLayoutManager(gridLayoutManager);
        holder.rvGridMedia.setFocusable(false);

        GridMediaAdapter gridMediaAdapter = new GridMediaAdapter(mContext);
        createAdapterMap(gridMedia.getmListMedia(),gridMediaAdapter);
        gridMediaAdapter.setData(gridMedia.getmListMedia());
        gridMediaAdapter.setCallBack(clickItem);
        gridMediaAdapter.setMainAdapter(this);

        holder.rvGridMedia.setAdapter(gridMediaAdapter);
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

    public class GridMediaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        RecyclerView rvGridMedia;
        public GridMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            rvGridMedia = itemView.findViewById(R.id.rv_grid_media);
        }
    }
    private void createAdapterMap(List<MediaModel> l, GridMediaAdapter adapter){
        for(int i = 0; i< l.size(); i++) {
            mAdapterReferenceMap.put(l.get(i).getIndex(),adapter);
        }
    }
    @Override
    public void updateItem(int position) {
        GridMediaAdapter adapter = mAdapterReferenceMap.get(position);
        adapter.notifyDataSetChanged();
    }
}
