package com.example.tmha.square.activity;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.google.firebase.database.DatabaseReference;


/*
 * Classname: LoginActivity
 *
 * Version information
 *
 * Date:06/07/2017
 *
 * Copyright
 *
 * Created by tmha on 06/7/2017
 */

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnGoogle, btnFacebook;
    private ImageView mImgIcon;
    private TextView mTxtTitle;
    private DatabaseReference mDatabase;
    private LinearLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //add cotrols
        addControls();
        addEvents();
    }

    private void addControls() {
        btnLogin    = (Button) findViewById(R.id.buttonLogin);
        btnGoogle   = (Button) findViewById(R.id.buttonGoogle);
        btnFacebook = (Button) findViewById(R.id.buttonFacebook);
        mImgIcon    = (ImageView) findViewById(R.id.imgIconApp);
        mTxtTitle   = (TextView) findViewById(R.id.txtTitle);
        mLayout     = (LinearLayout) findViewById(R.id.activity_login);

        getSupportActionBar().hide();
        final TransitionDrawable transitionDrawable = (TransitionDrawable) mImgIcon.getDrawable();
        transitionDrawable.startTransition(2000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transitionDrawable.reverseTransition(2000);
            }
        },2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImgIcon.setVisibility(View.GONE);
                mTxtTitle.setVisibility(View.GONE);
                mLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Login");
                getSupportActionBar().show();
            }
        }, 5000);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
