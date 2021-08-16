package com.example.penup.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.penup.R;
import com.example.penup.models.MediaModel;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class ViewMediaPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<MediaModel> listMedia;

    public ViewMediaPagerAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<MediaModel> listMedia){
        this.listMedia = listMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_view_media_item,container,false);

        PhotoView photoView = view.findViewById(R.id.pv_view_media);
        MxVideoPlayerWidget videoPlayerWidget = (MxVideoPlayerWidget) view.findViewById(R.id.mpw_video_player);
        MediaModel media = listMedia.get(position);
        if(media!=null){
            if(media.getType() == MediaModel.TYPE_IMAGE) {
                videoPlayerWidget.setVisibility(View.GONE);
                photoView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Uri.fromFile(new File(media.getPath()))).into(photoView);
            }else{
                photoView.setVisibility(View.GONE);
                videoPlayerWidget.setVisibility(View.VISIBLE);
                videoPlayerWidget.startPlay(media.getPath(), MxVideoPlayer.CURRENT_STATE_NORMAL, media.getTitle());
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(listMedia!=null){
            return listMedia.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        MxVideoPlayer.releaseAllVideos();
        container.removeView((View) object);
    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
