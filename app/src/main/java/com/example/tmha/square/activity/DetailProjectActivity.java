package com.example.tmha.square.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.adapter.ReportAdapter;
import com.example.tmha.square.model.Project;
import com.example.tmha.square.model.Report;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailProjectActivity extends AppCompatActivity {

    TextView txtNameProject, txtStartTime, txtEndTime, txtContent,
            txtCreateBy, txtTimeCreate, txtAddress;
    ImageView imgPhoto;

    ArrayList<Report> mListReport;
    RecyclerView mRecyclerView;
    ReportAdapter mReportAdapter;
    private Project mProject;
    private int mRow = 10;
    private int mIndex = 0;
    private int REQUEST_CODE_INSERT = 123;
    private int REQUEST_CODE_UPDATE = 124;
    public int REQUEST_CODE_DELETE = 125;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);
        addControls();
        addEvents();

    }

    private void addEvents() {

    }

    private void addControls() {
        getSupportActionBar().setTitle("Chi tiết project");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNameProject = (TextView) findViewById(R.id.txtNameProject);
        txtStartTime   = (TextView) findViewById(R.id.txtStartTime);
        txtEndTime     = (TextView) findViewById(R.id.txtEndTime);
        txtContent     = (TextView) findViewById(R.id.txtContentProject);
        txtCreateBy    = (TextView) findViewById(R.id.txtCreateBy);
        txtTimeCreate  = (TextView) findViewById(R.id.txtTimeCreate);
        txtAddress     = (TextView) findViewById(R.id.txtAddress);
        imgPhoto       = (ImageView) findViewById(R.id.imgProject);

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
                txtNameProject.setText(mProject.getmProjectName());
                txtStartTime.setText(mProject.getmStartTime());
                txtEndTime.setText(mProject.getmEndTime());
                txtContent.setText(mProject.getmProjectContent());
                txtAddress.setText(mProject.getmAddress());
                txtCreateBy.setText(mProject.getmCreateBy());
                txtTimeCreate.setText(mProject.getmTimeCreate());

                String picPath = mProject.getmProjectPhoto();
                Picasso.with(this).load(picPath)
                        .error(android.R.drawable.stat_notify_error)
                        .into(imgPhoto);

                imgPhoto.setOnClickListener(new View.OnClickListener() {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSERT &&
                resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getBundleExtra("bundle");
            if(bundle != null){
                Report report = (Report) bundle.getSerializable("report");
                mListReport.add(0, report);
                mReportAdapter.notifyItemInserted(0);
                mRecyclerView.scrollToPosition(0);
                mIndex++;
            }

        }

        if (requestCode == REQUEST_CODE_DELETE &&
                resultCode == RESULT_OK ){
              refresh();

        }
    }

    public void refresh(){
        mListReport.clear();
        mRow = 10;
        mIndex = 0;
        mListReport = MainActivity.database.getLimitReport(mRow, mIndex, mProject.getmID());
        mReportAdapter.notifyDataSetChanged();
    }
}
