package com.example.penup.activities;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.penup.R;
import com.example.penup.adapters.MainViewPagerAdapter;
import com.example.penup.fragments.AlbumFragment;
import com.example.penup.menu.AlbumActionBarHandler;
import com.example.penup.permission.PermissionUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gun0912.tedpermission.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager2 mViewPager;
    MainViewPagerAdapter mainViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        requestPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SDK_INT >= Build.VERSION_CODES.R && mainViewPagerAdapter == null) {
            if (Environment.isExternalStorageManager())
            {
                loadData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int position = mViewPager.getCurrentItem();
        switch (position) {
            case 0:
                break;
            case 1:
                getMenuInflater().inflate(R.menu.album_fragment_actionbar_menu, menu);
                break;
            case 2:
                break;
            default:
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_add_album:
                AlbumFragment fragment = (AlbumFragment)mainViewPagerAdapter.getFragment(1);
                AlbumActionBarHandler.getInstance().addAlbum(fragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findView(){
        mTabLayout = findViewById(R.id.tl_main);
        mViewPager = findViewById(R.id.vp_main);
    }
    void requestPermission(){
        if(SDK_INT>=30){
            PermissionUtil.getInstance().requestPermissionForSDK30(this);
        }else {
            PermissionListener permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    loadData();
                }
                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            PermissionUtil.getInstance().requestPermission(this, permissionListener);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("Vector", resultCode+" " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PermissionUtil.PERMISSION_CODE)
        {
            Log.d("Vector", resultCode+" oke " + requestCode);
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager())
                {
                    loadData();
                }
            }

        }
    }
    void loadData(){
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mViewPager.setAdapter(mainViewPagerAdapter);
        new TabLayoutMediator(mTabLayout,mViewPager,(tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Media");
                    break;
                case 1:
                    tab.setText("Albums");
                    break;
                case 2:
                    tab.setText("Folder");
                    break;
            }
        }).attach();
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                invalidateOptionsMenu();
            }
        });
    }
    public MainViewPagerAdapter getPagerAdapter(){
        return mainViewPagerAdapter;
    }
}