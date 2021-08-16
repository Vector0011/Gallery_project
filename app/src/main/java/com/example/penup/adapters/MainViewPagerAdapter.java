package com.example.penup.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.penup.fragments.AlbumFragment;
import com.example.penup.fragments.MediaFragment;
import com.example.penup.fragments.ShareFragment;
import com.example.penup.fragments.FolderFragment;

import java.util.HashMap;

public class MainViewPagerAdapter extends FragmentStateAdapter {
    HashMap<Integer, Fragment> mPageReferenceMap = new HashMap<>();

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment f0 = new MediaFragment();
                mPageReferenceMap.put(position, f0);
                return f0;
            case 1:
                Fragment f1 = new AlbumFragment();
                mPageReferenceMap.put(position, f1);
                return f1;
            case 2:
                Fragment f2 = new FolderFragment();
                mPageReferenceMap.put(position, f2);
                return f2;
            default:
                return new MediaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
