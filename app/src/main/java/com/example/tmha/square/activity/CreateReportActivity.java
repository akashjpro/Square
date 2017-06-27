package com.example.tmha.square.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.adapter.PhotoAdapter;
import com.example.tmha.square.listener.ListenerItem;
import com.example.tmha.square.model.Report;
import com.example.tmha.square.utils.TimeUtils;
import com.example.tmha.square.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateReportActivity extends AppCompatActivity implements ListenerItem {
    private EditText mEdtTitle, mEdtContent;
    private RecyclerView mRecyclerViewPhoto;
    private ArrayList<String> mListPath;
    private PhotoAdapter mPhotoAdapter;
    private Button mBtnSelect, mBtnCapture;
    private final int REQUEST_CODE_FOLDER = 111;
    private final int REQUEST_CODE_CAPTURE = 112;
    private String TAG = "log";
    private String mCurrentPhotoPath;
    private ImageView mImgSave, mImgCancel;
    private int mIdProjet;
    private Report mReport;
    private int mPosition;
    private int mIdReport = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        addControls();
        addEvents();
    }

    private void addEvents() {
        mBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });

        mBtnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent
                        = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i(TAG, "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent,
                                REQUEST_CODE_CAPTURE);
                    }
                }

            }
        });

        mImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i =0 ; i< mListPath.size(); i++){
                    if (mListPath.get(i) != null){
                        Utils.deleteFiles(mListPath.get(i));
                    }
                }
                onBackPressed();
            }
        });

        mImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEdtTitle.getText().toString().trim();
                if(TextUtils.isEmpty(title)){
                    Toast.makeText(CreateReportActivity.this,
                            "Xin vui long nhap tieu de",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = mEdtContent.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(CreateReportActivity.this,
                            "Xin vui long nhap noi dung",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mCurrentPhotoPath) && mListPath.get(0).equals("000")){
                    Toast.makeText(CreateReportActivity.this,
                            "Xin vui long chon hinh anh",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String createBy = "User";
                String timeCreate = TimeUtils.getCurrentTime();

                JSONArray jsonArray = new JSONArray(mListPath);
                String album = jsonArray.toString();

                Report report = new Report(mIdReport, mIdProjet, title,
                        content, album, createBy, timeCreate);
                insertUpdateReport(report);


            }
        });

    }

    private void setDataReport() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            mReport = (Report) bundle.getSerializable("report");
            mPosition = bundle.getInt("position", -1);

            if(mReport != null) {
                mIdProjet = mReport.getmIdProject();
                mIdReport = mReport.getmID();
                //report = (Project) getIntent().getSerializableExtra("report");
                mEdtTitle.setText(mReport.getmReportName());
                try {
                    JSONArray jsonArray = new JSONArray(mReport.getmAlbum());
                    for (int i = 0; i<jsonArray.length() ; i++){
                        String photo = jsonArray.get(i).toString();
                        mListPath.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mEdtContent.setText(mReport.getmContent());
            }else {

                mIdProjet = bundle.getInt("id");
                mIdReport = -1;
            }


        }

    }

    // Report(int mID, int mIdProject, String mReportName,
    //String mAlbumPhoto, String mContent,
    // String mCreateBy, String mTimeReport)

    private void insertUpdateReport(Report report) {
        if(mIdReport != -1){
            boolean result
                    = MainActivity.database.updateReport(report);
            if(result) {
                Toast.makeText(CreateReportActivity.this,
                        "Update success",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("report", report);
                bundle.putInt("position", mPosition);
                intent.putExtra("bundle", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Toast.makeText(CreateReportActivity.this,
                        "Update fail",
                        Toast.LENGTH_SHORT).show();
            }

        }else {
            //get result insert
            boolean resultReport =  MainActivity.database
                    .insertReport(report);

            if(resultReport) {
                Toast.makeText(CreateReportActivity.this,
                        "Add success",
                        Toast.LENGTH_SHORT).show();
                //get new report
                ArrayList<Report> listReport = MainActivity.database.getLimitReport(1, 0, mIdProjet);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("report", listReport.get(0));
                intent.putExtra("bundle", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Toast.makeText(CreateReportActivity.this,
                        "Add fail!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        Calendar calendar = Calendar.getInstance();
        String timeStamp
                = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(calendar.getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void addControls() {
        mImgSave = (ImageView) findViewById(R.id.imgSave);
        mImgCancel = (ImageView) findViewById(R.id.imgCancel);
        mEdtTitle = (EditText) findViewById(R.id.edtTitle);
        mEdtContent = (EditText) findViewById(R.id.edtContentReport);
        mBtnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        mBtnCapture = (Button) findViewById(R.id.btnCapture);
        mRecyclerViewPhoto
                = (RecyclerView) findViewById(R.id.recyclerViewListReport);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPhoto.setLayoutManager(layoutManager);
        mListPath = new ArrayList<>();

        setDataReport();

        mPhotoAdapter = new PhotoAdapter(CreateReportActivity.this, mListPath, this);
        mRecyclerViewPhoto.setAdapter(mPhotoAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create report");


    }

    @Override
    public void onClick(int position) {
        if (!mListPath.get(position).equals("000")) {
            Utils.deleteFiles(mListPath.get(position));
        }
        mListPath.remove(position);
        mPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClick(int position) {

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
        if(requestCode == REQUEST_CODE_FOLDER
                && resultCode == RESULT_OK
                && data != null){
            Uri uri = data.getData();
            //get real path from uri
            mCurrentPhotoPath = "file:" + getRealPathFromURI(uri);
            //check if size >0 & if element first = "000" then remove
            if(mListPath.size() > 0) {
                if (mListPath.get(0).equals("000")) {
                    mListPath.remove(0);
                    mPhotoAdapter.notifyItemRemoved(0);
                }
            }
            mListPath.add(mCurrentPhotoPath);
            mPhotoAdapter.notifyItemInserted(mListPath.size() - 1);

        }
        //result capture image
        if(requestCode == REQUEST_CODE_CAPTURE
                && resultCode == RESULT_OK){
            //check if size >0 & if element first = "000" then remove
            if(mListPath.size() > 0) {
                if (mListPath.get(0).equals("000")) {
                    mListPath.remove(0);
                    mPhotoAdapter.notifyItemRemoved(0);
                }
            }
            mListPath.add(mCurrentPhotoPath);
            mPhotoAdapter.notifyItemInserted(mListPath.size() - 1);
        }
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
