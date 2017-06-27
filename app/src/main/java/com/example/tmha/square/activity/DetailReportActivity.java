package com.example.tmha.square.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.adapter.CardPhotoAdapter;
import com.example.tmha.square.handler.ShadowTransformer;
import com.example.tmha.square.model.Report;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class DetailReportActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ArrayList<String> mListPath;
    private CardPhotoAdapter mCardPhotoAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private TextView mTxtTitle, mTxtContent,
            mTxtCreateBy, mTxtTimeCreate;
    private Report mReport;

    private final int  REQUEST_CODE_EDIT = 111;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        addControls();
        addEvents();
    }

    private void addEvents() {
        getData();

        findViewById(R.id.floatingButtonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailReportActivity.this, CreateReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("report", mReport);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        findViewById(R.id.floatingButtonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.floatingButtonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DetailReportActivity.this);
                dialog.setTitle("Thong bao");
                dialog.setContentView(R.layout.dialog_message);
                dialog.setCanceledOnTouchOutside(false);

                dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean result =
                                MainActivity.database
                                        .deleteReport(String.valueOf(mReport.getmID()), null);
                        if (result){
                            Toast.makeText(DetailReportActivity.this,
                                    "Delete success",
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            dialog.dismiss();
                            finish();
                        }else {
                            Toast.makeText(DetailReportActivity.this,
                                    "Delete fail",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

    }

    private void getData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null){
            mReport = (Report) bundle.getSerializable("report");
            setData(mReport);

        }
        mCardPhotoAdapter = new CardPhotoAdapter(DetailReportActivity.this);
        mCardPhotoAdapter.addCardItem(mListPath);
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardPhotoAdapter);
        mViewPager.setAdapter(mCardPhotoAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    /**
     * set data for all controls
     * @param report
     */
    public void setData(Report report){
        mTxtTitle.setText(report.getmReportName());
        mTxtContent.setText(report.getmContent());
        mTxtCreateBy.setText(report.getmCreateBy());
        mTxtTimeCreate.setText(report.getmTimeReport());
        try {
            JSONArray jsonArray = new JSONArray(report.getmAlbum());
            for (int i = 0 ; i< jsonArray.length(); i++ ){
                mListPath.add(jsonArray.get(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addControls() {
        getSupportActionBar().setTitle("Chi tiết report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mListPath = new ArrayList<>();

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtContent = (TextView) findViewById(R.id.txtContent);
        mTxtCreateBy = (TextView) findViewById(R.id.txtCreateBy);
        mTxtTimeCreate = (TextView) findViewById(R.id.txtTimeCreate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT
                && resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getBundleExtra("bundle");
            if(bundle != null){
                mListPath.clear();
                Report report = (Report) bundle.getSerializable("report");
                setData(report);
                mCardPhotoAdapter.notifyDataSetChanged();
            }
        }
    }
}