package com.example.penup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penup.R;
import com.example.penup.activities.AlbumMediaActivity;
import com.example.penup.activities.FolderMediaActivity;
import com.example.penup.adapters.AlbumAdapter;
import com.example.penup.adapters.FolderAdapter;
import com.example.penup.database.Album;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.menu.FolderMenuHandler;
import com.example.penup.menu.PopupMenuMaker;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.models.ListMediaByFolder;
import com.example.penup.viewmodel.ListAlbumViewModel;
import com.example.penup.viewmodel.ListFolderViewModel;

import java.util.List;

public class FolderFragment extends Fragment {
    View rootView;
    RecyclerView rvAblum;
    ListFolderViewModel listFolderViewModel;
    FolderAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_folder_fragment,container,false);
        initRecycleView();
        initViewModel();
        return rootView;
    }

    private void initRecycleView() {
        rvAblum = rootView.findViewById(R.id.rv_media_by_folder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration line = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        rvAblum.setLayoutManager(linearLayoutManager);
        rvAblum.addItemDecoration(line);
        adapter = new FolderAdapter(getActivity());
        adapter.setCallBack(new RecycleViewClickItem() {
            @Override
            public void onClick(View view, int index) {
                switch (view.getId()){
                    case R.id.rl_middle:
                        Intent intent = new Intent(getActivity(), FolderMediaActivity.class);
                        intent.putExtra("pathId",index);
                        startActivity(intent);
                        break;
                    case R.id.iv_properties:
                        PopupMenu popup = PopupMenuMaker.makeFolderMenuItem(getActivity(),view);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                return itemMenuClick(menuItem, index);
                            }
                        });
                        popup.show();
                        break;
                }
            }
            @Override
            public void onLongClick(View view, int index) {

            }
        });
        rvAblum.setAdapter(adapter);
    }

    private boolean itemMenuClick(MenuItem menuItem, int index) {
        switch (menuItem.getItemId()){
            case R.id.menu_delete:
                return FolderMenuHandler.getInstance().deleteFolder(getActivity(),listFolderViewModel,index);
            case R.id.menu_rename:
                return FolderMenuHandler.getInstance().renameFolder(getActivity(),listFolderViewModel,index);
            default:
                return false;
        }
    }

    private void initViewModel() {
        listFolderViewModel = new ViewModelProvider(this).get(ListFolderViewModel.class);
        listFolderViewModel.requestData(getContext());
        listFolderViewModel.getListMedia().observe(getViewLifecycleOwner(), new Observer<List<ListMediaByFolder>>() {
            @Override
            public void onChanged(List<ListMediaByFolder> list) {
                adapter.setData(list);
                Log.d("vector","data change " +list.size());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        listFolderViewModel.requestData(getActivity());
    }
}
