package com.example.tmha.square.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.example.tmha.square.R;

/**
 * Created by Aka on 6/28/2017.
 */

public class ShowHideProgessDialog {

    public static ProgressDialog progressDialog;

    public static void showHideProgessDialog(Context context){
        progressDialog = new ProgressDialog(context, R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
            }
        }, 3000);
    }

    public static void hideProgessDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
