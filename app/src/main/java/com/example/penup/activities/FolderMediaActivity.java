package com.example.penup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.penup.adapters.FolderMediaAdapter;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.menu.AlbumActionBarHandler;
import com.example.penup.menu.FolderMenuHandler;
import com.example.penup.menu.MediaActionBarHandler;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.ListMediaByFolder;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.MediaConverter;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.utils.CopyPasteUtil;
import com.example.penup.viewmodel.FolderMediaViewModel;
import com.example.penup.viewmodel.ListAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

public class FolderMediaActivity extends AppCompatActivity {
    private FolderMediaActivity.ActionModeCallback actionModeCallback = new FolderMediaActivity.ActionModeCallback();
    private ActionMode actionMode;
    int pathId;

    RecyclerView rvFolderMedia;
    FolderMediaAdapter adapter;
    ListMediaByFolder listMedia;
    FolderMediaViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_media);

        rvFolderMedia = findViewById(R.id.rv_folder_media);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rvFolderMedia.setLayoutManager(layoutManager);
        rvFolderMedia.setFocusable(false);

        pathId = getIntent().getIntExtra("pathId",-1);
        viewModel = new ViewModelProvider(this).get(FolderMediaViewModel.class);
        viewModel.requestData(this,pathId);
        viewModel.getListMedia().observe(this, new Observer<ListMediaByFolder>() {
            @Override
            public void onChanged(ListMediaByFolder listMediaByFolder) {
                listMedia = listMediaByFolder;
                adapter.setData(listMediaByFolder.getListMedia());
                initActionBar();
            }
        });
        adapter = new FolderMediaAdapter(this);

        adapter.setCallBack(new RecycleViewClickItem() {
            @Override
            public void onClick(View view, int index) {
                if (actionMode != null) {
                    toggleSelection(index);
                }else {
                    Intent intent = new Intent(FolderMediaActivity.this, ViewMediaActivity.class);
                    intent.putExtra("pathId", pathId);
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
        rvFolderMedia.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.folder_media_actionbar_menu, menu);
        MenuItem itemPaste = menu.findItem(R.id.menu_paste);
        MenuItem itemDetail = menu.findItem(R.id.menu_detail);
        itemDetail.setVisible(false);
        if(!CopyPasteUtil.isCopy){
            itemPaste.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlbumActionBarHandler actionBarHandler = AlbumActionBarHandler.getInstance();
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_paste:
                FolderMenuHandler.getInstance().paste(this,listMedia.getPath());
                invalidateOptionsMenu();
                return true;
            case R.id.menu_detail:
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Folder "+listMedia.getName());
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
        MediaResponsitory.getInstance().clearCache();
        viewModel.requestData(this,pathId);
    }
    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = FolderMediaActivity.ActionModeCallback.class.getSimpleName();
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
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    FolderMenuHandler.getInstance().delete(FolderMediaActivity.this, viewModel, adapter.getSelectedItems(), pathId);
                    Log.d(TAG, "menu_remove");
                    mode.finish();
                    return true;
                case R.id.menu_share:
                    FolderMenuHandler.getInstance().shareFromFolder(FolderMediaActivity.this, listMedia, adapter.getSelectedItems());
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