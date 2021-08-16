package com.example.penup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.penup.R;
import com.example.penup.adapters.ViewMediaPagerAdapter;
import com.example.penup.menu.ViewMediaActionBarHandler;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;

import java.util.List;

import hb.xvideoplayer.MxVideoPlayer;

public class ViewMediaActivity extends AppCompatActivity {
    ViewPager viewPager;
    ViewMediaPagerAdapter adapter;
    List<MediaModel> listMedia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media);
        int index = getIntent().getIntExtra("index",0);
        int pathId = getIntent().getIntExtra("pathId",0);
        int albumId = getIntent().getIntExtra("albumId",Integer.MAX_VALUE);
        viewPager = findViewById(R.id.vp_view_media);
        if(albumId != Integer.MAX_VALUE) {
            listMedia = MediaConverter.getInstance().getListMediaFromAlbum(this,albumId).getListMedia();
        }else if(pathId != 0){
            listMedia = MediaConverter.getInstance().getListMediaFromPath(this,pathId).getListMedia();
        }else {
            listMedia = MediaResponsitory.getInstance().getListMedia(this);
        }
        adapter = new ViewMediaPagerAdapter(this);
        adapter.setData(listMedia);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MediaModel media = listMedia.get(viewPager.getCurrentItem());
                String title = media.getTitle();
                getSupportActionBar().setTitle(title);
                ViewMediaActivity.this.invalidateOptionsMenu();
                MxVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initActionBar(index);
    }
    public void deleteMedia(int index){
        listMedia.remove(index);
        adapter.setData(listMedia);
    }
    private void initActionBar(int index) {
        getSupportActionBar().setTitle(listMedia.get(index).getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_media_actionbar_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_favourite);
        if(listMedia.get(viewPager.getCurrentItem()).getFavourite()==1){
            item.setIcon(R.drawable.heart_icon);
        }else{
            item.setIcon(R.drawable.unheart_icon);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MxVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MediaModel media = listMedia.get(viewPager.getCurrentItem());

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_share:
                ViewMediaActionBarHandler.getInstance().share(this,media);
                return true;
            case R.id.menu_remove:
                ViewMediaActionBarHandler.getInstance().delete(this,media,viewPager.getCurrentItem());
                return true;
            case R.id.menu_detail:
                ViewMediaActionBarHandler.getInstance().detail(this,media);
                return true;
            case R.id.menu_favourite:
                ViewMediaActionBarHandler.getInstance().favourite(this,media);
                ViewMediaActivity.this.invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}