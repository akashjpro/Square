package com.example.tmha.square.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tmha.square.R;
import com.example.tmha.square.database.DBManager;
import com.example.tmha.square.fragment.HomeFragment;
import com.example.tmha.square.fragment.ReportFragment;
import com.example.tmha.square.fragment.SettingFragment;
import com.example.tmha.square.model.Project;
import com.example.tmha.square.utils.RecyclerViewUtils;

/*
 * Classname: MainActivity
 *
 * Version information
 *
 * Date:06/07/2017
 *
 * Copyright
 *
 * Created by tmha on 06/7/2017
 */


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mfragManager;
    private HomeFragment mHomeFragment;
    private ReportFragment mReportFragment;
    private SettingFragment mSettingFragment;

    private NavigationView mNavigationView;

    private int mIndexFragment = 0;
    public static Toolbar mToolbar;
    public static RecyclerViewUtils.ShowHideToolbarOnScrollingListener showHideToolbarListener;
    public static DBManager database;
    public static  final int REQUEST_CODE_UPDATE = 123;
    public static  final int REQUEST_CODE_ADD    = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvent();

        mfragManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, new HomeFragment(), "fragHome");
        fragmentTransaction.commit();

        //set background toolbar
        mToolbar.setBackgroundColor(Color.alpha(R.color.colorFloatingBackground));
        mToolbar.setTitle("Home");
        setSupportActionBar(mToolbar); // set toolbar

        //set menu default
        mNavigationView.setCheckedItem(R.id.nav_home);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, CreateProjectAcivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * events
     */
    private void addEvent() {
    }

    /**
     * add controls
     */
    private void addControls() {

        database = new DBManager(this);
        database.getWritableDatabase();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
        mHomeFragment    = (HomeFragment) mfragManager.findFragmentByTag("fragHome");
        mReportFragment  = (ReportFragment) mfragManager.findFragmentByTag("fragReport");
        mSettingFragment = (SettingFragment) mfragManager.findFragmentByTag("fragSetting");

        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.nav_home:
                mToolbar.setTitle("Home");
                if (mIndexFragment != 0 ){
                    hideFragment(mIndexFragment, fragmentTransaction);
                    if(mHomeFragment != null){
                        fragmentTransaction.show(mHomeFragment);
                    }else {
                        fragment = new HomeFragment();
                        fragmentTransaction.add(R.id.contentLayout, fragment, "fragHome");
                    }
                }
                mIndexFragment = 0;
                break;

            case R.id.nav_report:
                mToolbar.setTitle("Project");
                if (mIndexFragment != 1 ){
                    hideFragment(mIndexFragment, fragmentTransaction);
                    if(mReportFragment != null){
                        fragmentTransaction.show(mReportFragment);
                    }else {
                        fragment = new ReportFragment();
                        fragmentTransaction.add(R.id.contentLayout, fragment, "fragReport");
                    }
                }
                mIndexFragment = 1;
                break;

            case R.id.nav_setting:
                mToolbar.setTitle("Setting");
                if (mIndexFragment != 2){
                    hideFragment(mIndexFragment, fragmentTransaction);
                    if(mSettingFragment != null){
                        fragmentTransaction.show(mSettingFragment);
                    }else {
                        fragment = new SettingFragment();
                        fragmentTransaction.add(R.id.contentLayout, fragment, "fragSetting");
                    }
                }
                mIndexFragment = 2;
                break;

            case R.id.nav_contact:
                break;

            case R.id.nav_about:
                break;

        }
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     * @param i
     * @param fragmentTransaction
     */
    public void hideFragment(int i, FragmentTransaction fragmentTransaction){
        switch (i){
            case 0:
                fragmentTransaction.hide(mHomeFragment); // if case = 0 then hide home fragment
                break;
            case 1:
                fragmentTransaction.hide(mReportFragment);  // if case = 1 then hide report fragment
                break;
            case 2:
                fragmentTransaction.hide(mSettingFragment);  // if case = 2 then hide setting fragment
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get report fragment
        ReportFragment reportFragment = (ReportFragment) getFragmentManager().findFragmentByTag("fragReport");
        //add report in list
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == RESULT_OK ){
            Bundle bundle = data.getBundleExtra("bundle");
            if((bundle != null) && (reportFragment != null)){
                Project project = (Project) bundle.getSerializable("project");
                reportFragment.addReport(project);
            }

        }
        //update report
        if (requestCode == REQUEST_CODE_UPDATE
                && resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getBundleExtra("bundle");
            if(bundle != null){
                Project project = (Project) bundle.getSerializable("project");
                int position = bundle.getInt("position");
                reportFragment.updateReport(position, project);
            }
        }
    }
}
