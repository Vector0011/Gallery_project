package com.example.penup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penup.R;
import com.example.penup.activities.MainActivity;
import com.example.penup.activities.ViewMediaActivity;
import com.example.penup.adapters.ListMediaAdapter;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.menu.AlbumActionBarHandler;
import com.example.penup.menu.MediaActionBarHandler;
import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.MediaModel;
import com.example.penup.responsitory.MediaResponsitory;
import com.example.penup.viewmodel.ListMediaViewModel;

import java.util.ArrayList;
import java.util.List;

public class MediaFragment extends Fragment {
    private MediaFragment.ActionModeCallback actionModeCallback = new MediaFragment.ActionModeCallback();
    private ActionMode actionMode;

    View rootView;
    RecyclerView rvMediaByDate;
    ListMediaViewModel listMediaViewModel;
    ListMediaAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_media_fragment,container,false);
        initRecycleView();
        initViewModel();
        return rootView;
    }

    private void initViewModel() {
        listMediaViewModel = new ViewModelProvider(this).get(ListMediaViewModel.class);
        listMediaViewModel.requestData(getContext());
        listMediaViewModel.getListMedia().observe(getViewLifecycleOwner(), new Observer<List<GridMediaByDate>>() {
            @Override
            public void onChanged(List<GridMediaByDate> gridMediaByDates) {
                adapter.setData(gridMediaByDates);

            }
        });
    }

    private void initRecycleView() {
        rvMediaByDate = rootView.findViewById(R.id.rv_media_by_date);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvMediaByDate.setLayoutManager(linearLayoutManager);
        adapter = new ListMediaAdapter(getActivity());
        adapter.setCallBack(new RecycleViewClickItem() {
            @Override
            public void onClick(View view, int index) {
                Log.d("Vector","click");
                if (actionMode != null) {
                    toggleSelection(index);
                }else {
                    Intent intent = new Intent(getActivity(), ViewMediaActivity.class);
                    intent.putExtra("index", index);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int index) {
                Log.d("Vector","long click");
                if (actionMode == null) {
                    actionMode = ((MainActivity)getActivity()).startSupportActionMode(actionModeCallback);
                }

                toggleSelection(index);
            }
        });
        rvMediaByDate.setAdapter(adapter);
    }
    public void updateData(){
        listMediaViewModel.requestData(getContext());
    }
    @Override
    public void onResume() {
        super.onResume();
        listMediaViewModel.requestData(getContext());
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
    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.all_media_selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<MediaModel> listMedia = MediaResponsitory.getInstance().getListMedia(getActivity());
            List<MediaModel> selectedMedia = new ArrayList<>();
            for(int i = 0; i<listMedia.size(); i++){
                if(adapter.isSelected(listMedia.get(i).getIndex())){
                    selectedMedia.add(listMedia.get(i));
                }
            }
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    Log.d("Vector", "menu_remove");
                    MediaActionBarHandler.getInstance().delete(MediaFragment.this,selectedMedia);
                    mode.finish();
                    return true;
                case R.id.menu_share:
                    MediaActionBarHandler.getInstance().share(getActivity(), selectedMedia);
                    Log.d("Vector", "menu_share");
                    mode.finish();
                    return true;
                case R.id.add_to_album:
                    MediaActionBarHandler.getInstance().addToAlbum((MainActivity)getActivity(), selectedMedia);
                    Log.d("Vector", "menu_add_album");
                    mode.finish();
                    return true;
                case R.id.menu_copy:
                    MediaActionBarHandler.getInstance().copy(selectedMedia);
                    Log.d("Vector", "copy");
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
