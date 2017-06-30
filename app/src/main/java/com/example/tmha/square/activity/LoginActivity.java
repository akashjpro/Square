package com.example.tmha.square.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.tmha.square.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

    Button btnLogin, btnGoogle, btnFacebook;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //add cotrols
        addControls();
        addEvents();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").setValue("OK chua");
    }

    private void addControls() {

        getSupportActionBar().setTitle("Login");

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnGoogle = (Button) findViewById(R.id.buttonGoogle);
        btnFacebook = (Button) findViewById(R.id.buttonFacebook);
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
