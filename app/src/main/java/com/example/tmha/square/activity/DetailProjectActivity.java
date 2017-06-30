package com.example.tmha.square.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.adapter.ReportAdapter;
import com.example.tmha.square.model.Project;
import com.example.tmha.square.model.Report;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.tmha.square.R.id.txtAddress;
import static com.example.tmha.square.R.id.txtCreateBy;
import static com.example.tmha.square.R.id.txtEndTime;
import static com.example.tmha.square.R.id.txtNameProject;
import static com.example.tmha.square.R.id.txtStartTime;
import static com.example.tmha.square.R.id.txtTimeCreate;

public class DetailProjectActivity extends AppCompatActivity {

    TextView mTxtNameProject, mTxtStartTime, mTxtEndTime, mTxtContent,
             mTxtCreateBy, mTxtTimeCreate, mTxtAddress, mTextProgress;
    ImageView mImgPhoto;
    ProgressBar mProgress;

    LinearLayout mLayoutLocation;

    ArrayList<Report> mListReport;
    RecyclerView mRecyclerView;
    ReportAdapter mReportAdapter;
    private Project mProject;
    private int mRow = 10;
    private int mIndex = 0;
    private int REQUEST_CODE_INSERT = 123;
    public int  REQUEST_CODE_UPDATE = 124;
    public int REQUEST_CODE_DELETE = 125;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);
        addControls();
        addEvents();

    }

    private void addEvents() {
        mTextProgress.setText(mProject.getmProgess() + "%");
        mProgress.setProgress(mProject.getmProgess());
        mLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("project", mProject);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        getSupportActionBar().setTitle("Chi tiết project");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTxtNameProject = (TextView) findViewById(txtNameProject);
        mTxtStartTime   = (TextView) findViewById(txtStartTime);
        mTxtEndTime     = (TextView) findViewById(txtEndTime);
        mTxtContent     = (TextView) findViewById(R.id.txtContentProject);
        mTxtCreateBy    = (TextView) findViewById(txtCreateBy);
        mTxtTimeCreate  = (TextView) findViewById(txtTimeCreate);
        mTxtAddress     = (TextView) findViewById(txtAddress);
        mImgPhoto       = (ImageView) findViewById(R.id.imgProject);
        mTextProgress   = (TextView) findViewById(R.id.txtProgress);
        mProgress       = (ProgressBar) findViewById(R.id.progress);
        mLayoutLocation = (LinearLayout) findViewById(R.id.layoutViewLocation);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewListReport);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mListReport = new ArrayList<>();

        setData();

        mReportAdapter = new ReportAdapter(this, mListReport);
        mRecyclerView.setAdapter(mReportAdapter);

    }

    private void setData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            mProject = (Project) bundle.getSerializable("project");
            if (mProject != null) {
                mTxtNameProject.setText(mProject.getmProjectName());
                mTxtStartTime.setText(mProject.getmStartTime());
                mTxtEndTime.setText(mProject.getmEndTime());
                mTxtContent.setText(mProject.getmProjectContent());
                mTxtAddress.setText(mProject.getmAddress());
                mTxtCreateBy.setText(mProject.getmCreateBy());
                mTxtTimeCreate.setText(mProject.getmTimeCreate());

                String picPath = mProject.getmProjectPhoto();
                Picasso.with(this).load(picPath)
                        .error(android.R.drawable.stat_notify_error)
                        .into(mImgPhoto);

                mImgPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(DetailProjectActivity.this,
                                MapsActivity.class));
                    }
                });
            }

            mListReport = MainActivity.database.getLimitReport(mRow, mIndex, mProject.getmID());

            findViewById(R.id.layoutCreateReport)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailProjectActivity.this, CreateReportActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", mProject.getmID());
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, REQUEST_CODE_INSERT);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get new report
        if (requestCode == REQUEST_CODE_INSERT &&
                resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getBundleExtra("bundle");
            if(bundle != null){
                Report report = (Report) bundle.getSerializable("report");
                mListReport.add(0, report);
                mReportAdapter.notifyItemInserted(0);
                mRecyclerView.scrollToPosition(0);
                mIndex++; // index increase after add new report
            }

        }

        //delete report with position
        if (requestCode == REQUEST_CODE_DELETE &&
                resultCode == RESULT_OK & data != null ){
            Bundle bundle = data.getBundleExtra("bundle");
            if(bundle != null){
               if (bundle != null){
                   int position = bundle.getInt("position");
                   mListReport.remove(position);
                   mReportAdapter.notifyDataSetChanged();
                   mIndex--; //decrease index after delete report
               }
            }

        }

        if (requestCode == REQUEST_CODE_UPDATE &&
                resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getBundleExtra("bundle");
            if(bundle != null){
                if (bundle != null){
                    int position = bundle.getInt("position");
                    Report report = (Report) bundle.getSerializable("report");
                    mListReport.set(position, report);
                    mReportAdapter.notifyItemChanged(position);
                }
            }
        }
    }

    public void deleteReport(int position ){
        mListReport.remove(position);
        mReportAdapter.notifyDataSetChanged();
        mIndex--; //decrease index after delete report
    }



}
