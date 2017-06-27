package com.example.tmha.square.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.adapter.CardPhotoAdapter;
import com.example.tmha.square.model.Report;

import java.util.ArrayList;

public class DetailReportActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ArrayList<String> mListPath;
    private CardPhotoAdapter mCardPhotoAdapter;


    private TextView mTxtTitle, mTxtContent,
            mTxtCreateBy, mTxtTimeCreate;
    private Report mReport;

    private final int  REQUEST_CODE_EDIT = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
    }
}
