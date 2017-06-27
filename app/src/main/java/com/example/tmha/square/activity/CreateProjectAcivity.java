package com.example.tmha.square.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.model.Project;
import com.example.tmha.square.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/*
 * Classname: CreateProjectAcivity
 *
 * Date:06/09/2017
 *
 * Copyright
 *
 * Created by tmha on 06/09/2017
 */


public class CreateProjectAcivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private TextView mTxtProgress;
    private EditText mEdtNameProject, mEdtContent;
    private EditText mEdtStartTime, mEdtEndTime, mEdtAddress;
    private ImageView mImgProject, mImgSave, mImgCancel;
    private Button mBtnSelectFile, mBtnCapture;
    private final int REQUEST_CODE_FOLDER = 111;
    private final int REQUEST_CODE_CAPTURE = 112;
    private int mId = -1;
    private int mPosition;
    private Bitmap mBitmap;
    private String mCurrentPhotoPath;
    private Project mProject;
    private String TAG = "log";
    private boolean isSaveImage = false;
    private SeekBar mSeekBarProgress;
    private int mProgress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        addControls();
        addEvents();
    }

    /**
     *add events
     */
    private void addEvents() {
        //set data report
        setDataReport();

        mSeekBarProgress.setOnSeekBarChangeListener(this);

        mBtnSelectFile.setOnClickListener(new View.OnClickListener() {
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

        mImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get name project
                String name = mEdtNameProject.getText().toString().trim();
                //check name
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(CreateProjectAcivity.this,
                            " Tên dự án không được để trống!!!"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                //get content project
                String content = mEdtContent.getText().toString().trim();
                //check content
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(CreateProjectAcivity.this,
                            " Nội dung dự án không được để trống!!!"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                String address = mEdtAddress.getText().toString().trim();
                if(TextUtils.isEmpty(address)){
                    Toast.makeText(CreateProjectAcivity.this,
                            "Xin vui long nhap dia chi",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check image
                if(mCurrentPhotoPath == null){
                    Toast.makeText(CreateProjectAcivity.this,
                            "Please, select picture",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String createBy = "User";
                String timeCreate = TimeUtils.getCurrentTime();
                String startTime = mEdtStartTime.getText().toString().trim();
                String endTime   = mEdtEndTime.getText().toString().trim();
                String location  = "10.802083, 106.639731";
                int progess = mProgress;
                Project project = new Project(mId, name, mCurrentPhotoPath,
                        progess, startTime, endTime, content,
                        address, location, createBy, timeCreate );
                //insert or update project
                insertUpdateReport(project);



            }
        });

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


    private void insertUpdateReport(Project project) {
        if(mId != -1){
            //get result update
            boolean result
                    = MainActivity.database.updateProject(project);
            if(result) {
                Toast.makeText(CreateProjectAcivity.this,
                        "Update success",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("project", project);
                bundle.putInt("position", mPosition);
                intent.putExtra("bundle", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Toast.makeText(this,
                        "Update fail!!!",
                        Toast.LENGTH_SHORT).show();
            }

        }else {
            //get result insert
            boolean result =  MainActivity.database
                    .insertProject(project);
            if(result) {
                Toast.makeText(CreateProjectAcivity.this,
                        "Add success",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                //get new project
                ArrayList<Project> listProject
                        = MainActivity.database.getLimitproject(1, 0);
                bundle.putSerializable("project", listProject.get(0));
                intent.putExtra("bundle", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Toast.makeText(CreateProjectAcivity.this,
                        "Add fail!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDataReport() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            mProject = (Project) bundle.getSerializable("project");
            mPosition = bundle.getInt("position");
            //report = (Project) getIntent().getSerializableExtra("report");
            if (mProject != null) {
//                mReport = MainActivity.database.getProject(String.valueOf(mId));
                mId = mProject.getmID();
                mEdtNameProject.setText(mProject.getmProjectName());
                mEdtNameProject.setSelection(mProject.getmProjectName().length());
                mCurrentPhotoPath = mProject.getmProjectPhoto();
//                try {
//                    mBitmap = MediaStore.Images.Media
//                            .getBitmap(getContentResolver(),
//                                    Uri.parse(mCurrentPhotoPath));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mImgProject.setImageBitmap(mBitmap);

                Picasso.with(this).load(mCurrentPhotoPath)
                        .error(android.R.drawable.stat_notify_error)
                        .into(mImgProject);

                mEdtStartTime.setText(mProject.getmStartTime());
                mEdtEndTime.setText(mProject.getmEndTime());
                mEdtContent.setText(mProject.getmProjectContent());
                mEdtAddress.setText(mProject.getmAddress());
                mTxtProgress.setText(mProject.getmProgess() + "%");
                mSeekBarProgress.setProgress(mProject.getmProgess());
            }

        }
    }


    private void addControls() {
        mEdtNameProject = (EditText) findViewById(R.id.edtNameProject);
        mEdtContent     = (EditText) findViewById(R.id.edtContentProject);
        mEdtStartTime   = (EditText) findViewById(R.id.edtStartTime);
        mEdtEndTime     = (EditText) findViewById(R.id.edtEndTime);
        mImgProject     = (ImageView) findViewById(R.id.imgProject);
        mBtnSelectFile  = (Button) findViewById(R.id.btnSelectPhoto);
        mBtnCapture     = (Button) findViewById(R.id.btnCapture);
        mImgSave        = (ImageView) findViewById(R.id.imgSave);
        mImgCancel      = (ImageView) findViewById(R.id.imgCancel);
        mEdtAddress     = (EditText) findViewById(R.id.edtAddress);
        mSeekBarProgress = (SeekBar) findViewById(R.id.seekBarProgress);
        mTxtProgress    = (TextView) findViewById(R.id.txtProgress);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create project");
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //result folder image
        if(requestCode == REQUEST_CODE_FOLDER
                && resultCode == RESULT_OK
                && data != null){
            Uri uri = data.getData();
            //get real path from uri
            mCurrentPhotoPath = "file:" + getRealPathFromURI(uri);
            try {
//                InputStream inputStream = getContentResolver()
//                        .openInputStream(uri);
//                mBitmap  = BitmapFactory.decodeStream(inputStream);
//                mImgProject.setImageBitmap(mBitmap);
                mBitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(),
                                Uri.parse(mCurrentPhotoPath));
                mImgProject.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //result capture image
        if(requestCode == REQUEST_CODE_CAPTURE
                && resultCode == RESULT_OK){
            try {
                mBitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(),
                                Uri.parse(mCurrentPhotoPath));
                mImgProject.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    @Override
    protected void onStop() {
        String path = mCurrentPhotoPath;
        super.onStop();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTxtProgress.setText(progress + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mProgress = seekBar.getProgress();
    }
}
