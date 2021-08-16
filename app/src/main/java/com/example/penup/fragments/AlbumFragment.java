package com.example.penup.fragments;

import android.app.Activity;
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
import com.example.penup.activities.ViewMediaActivity;
import com.example.penup.adapters.AlbumAdapter;
import com.example.penup.adapters.ListMediaAdapter;
import com.example.penup.callback.RecyclerTouchListener;
import com.example.penup.database.Album;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.menu.AlbumMenuHandler;
import com.example.penup.menu.FolderMenuHandler;
import com.example.penup.menu.PopupMenuMaker;
import com.example.penup.models.GridMediaByDate;
import com.example.penup.models.ListMediaByAlbum;
import com.example.penup.responsitory.RoomResponsitory;
import com.example.penup.viewmodel.ListAlbumViewModel;
import com.example.penup.viewmodel.ListMediaViewModel;

import java.util.List;

public class AlbumFragment extends Fragment {
    View rootView;
    RecyclerView rvAblum;
    ListAlbumViewModel listAlbumViewModel;
    AlbumAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_album_fragment,container,false);
        initRecycleView();
        initViewModel();
        return rootView;
    }

    private void initRecycleView() {
        rvAblum = rootView.findViewById(R.id.rv_media_by_album);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration line = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        rvAblum.setLayoutManager(linearLayoutManager);
        rvAblum.addItemDecoration(line);
        adapter = new AlbumAdapter(getActivity());
        adapter.setCallBack(new RecycleViewClickItem() {
            @Override
            public void onClick(View view, int index) {
                switch (view.getId()){
                    case R.id.rl_middle:
                        Intent intent = new Intent(getActivity(), AlbumMediaActivity.class);
                        intent.putExtra("albumId",index);
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
                return AlbumMenuHandler.getInstance().deleteAlbum(getActivity(),listAlbumViewModel,index);
            case R.id.menu_rename:
                return AlbumMenuHandler.getInstance().renameAlbum(getActivity(),listAlbumViewModel,index);
            default:
                return false;
        }
    }

    private void initViewModel() {
        listAlbumViewModel = new ViewModelProvider(this).get(ListAlbumViewModel.class);
        listAlbumViewModel.requestData(getContext());
        listAlbumViewModel.getListMedia().observe(getViewLifecycleOwner(), new Observer<List<ListMediaByAlbum>>() {
            @Override
            public void onChanged(List<ListMediaByAlbum> listMediaByAlbums) {
                adapter.setData(listMediaByAlbums);
            }
        });
    }
    public boolean addAlbum(Album album){
        return listAlbumViewModel.addAlbum(getActivity(), album);
    }
    public ListAlbumViewModel getViewModel(){
        return listAlbumViewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        listAlbumViewModel.requestData(getActivity());
    }
}
