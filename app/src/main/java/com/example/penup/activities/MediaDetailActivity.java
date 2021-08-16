package com.example.penup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.penup.R;
import com.example.penup.databinding.ActivityMediaDetailBinding;
import com.example.penup.menu.ViewMediaActionBarHandler;
import com.example.penup.models.MediaModel;
import com.example.penup.utils.DateTimeHelper;
import com.example.penup.utils.FileHelper;

public class MediaDetailActivity extends AppCompatActivity {
    TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        MediaModel mediaModel = (MediaModel) getIntent().getSerializableExtra("media");

        initActionBar();

        BindingToLayout(mediaModel);
    }

    private void BindingToLayout(MediaModel mediaModel) {
        ActivityMediaDetailBinding activityMediaDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_media_detail);
        activityMediaDetailBinding.setMediaModel(mediaModel);
        activityMediaDetailBinding.setDateTimeHelper(DateTimeHelper.getInstance());
        activityMediaDetailBinding.setFileHelper(FileHelper.getInstance());
    }
    private void initActionBar() {
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}