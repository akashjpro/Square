package com.example.tmha.square.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.tmha.square.R;
import com.squareup.picasso.Picasso;

public class FullPhotoActivity extends AppCompatActivity {
    ImageView mImgPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);
        getSupportActionBar().setTitle("Photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String path = getIntent().getStringExtra("path");
        mImgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        if (path != null){
            Picasso.with(this).load(path)
                    .error(android.R.drawable.stat_notify_error)
                    .into(mImgPhoto);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
