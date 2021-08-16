package com.example.penup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.penup.R;
import com.example.penup.adapters.AlbumMediaAdapter;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.menu.AlbumActionBarHandler;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.responsitory.MediaConverter;

public class AlbumMediaActivity extends AppCompatActivity {
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    int albumId;

    RecyclerView rvAlbumMedia;
    AlbumMediaAdapter adapter;
    ListMediaByAlbum mediaByAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_media);

        rvAlbumMedia = findViewById(R.id.rv_album_media);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rvAlbumMedia.setLayoutManager(layoutManager);
        rvAlbumMedia.setFocusable(false);

        albumId = getIntent().getIntExtra("albumId",-1);
        mediaByAlbum = MediaConverter.getInstance().getListMediaFromAlbum(this,albumId);

        adapter = new AlbumMediaAdapter(this);
        adapter.setData(mediaByAlbum.getListMedia());
        adapter.setCallBack(new RecycleViewClickItem() {
            @Override
            public void onClick(View view, int index) {
                if (actionMode != null) {
                    toggleSelection(index);
                }else {
                    Intent intent = new Intent(AlbumMediaActivity.this, ViewMediaActivity.class);
                    intent.putExtra("albumId", mediaByAlbum.getAlbumId());
                    intent.putExtra("index", index);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int index) {
                if (actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                }

                toggleSelection(index);
            }
        });
        rvAlbumMedia.setAdapter(adapter);

        initActionBar();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Album "+mediaByAlbum.getAlbumName());
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }
    public void reloadData(){
        mediaByAlbum = MediaConverter.getInstance().getListMediaFromAlbum(this,albumId);
        adapter.setData(mediaByAlbum.getListMedia());
    }
    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.album_media_selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            AlbumActionBarHandler actionBarHandler = AlbumActionBarHandler.getInstance();
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    actionBarHandler.removeFromAlbum(AlbumMediaActivity.this, mediaByAlbum, adapter.getSelectedItems());
                    Log.d(TAG, "menu_remove");
                    mode.finish();
                    return true;
                case R.id.menu_share:
                    actionBarHandler.shareFromAlbum(AlbumMediaActivity.this, mediaByAlbum, adapter.getSelectedItems());
                    Log.d(TAG, "menu_share");
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
        }
    }
}