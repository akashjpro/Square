package com.example.tmha.square.model;

import java.io.Serializable;

/**
 * Created by tmha on 6/8/2017.
 */

public class Project implements Serializable {
    private  int    mID;
    private  String mProjectName;
    private  String mProjectPhoto;
    private  int    mProgess;
    private  String mStartTime;
    private  String mEndTime;
    private  String mProjectContent;
    private  String mAddress;
    private  String mLocation;
    private  String mCreateBy;
    private  String mTimeCreate;

    public Project() {
    }

    public Project(String mProjectName, String mProjectPhoto,
                   String mAddress, String mLocation) {
        this.mProjectName = mProjectName;
        this.mProjectPhoto = mProjectPhoto;
        this.mAddress = mAddress;
        this.mLocation = mLocation;
    }

    public Project(String mProjectName, String mProjectPhoto,
                   int mProgess, String mStartTime,
                   String mEndTime, String mProjectContent,
                   String mAddress, String mCreateBy,
                   String mTimeCreate) {
        this.mProjectName = mProjectName;
        this.mProjectPhoto = mProjectPhoto;
        this.mProgess = mProgess;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mProjectContent = mProjectContent;
        this.mAddress = mAddress;
        this.mCreateBy = mCreateBy;
        this.mTimeCreate = mTimeCreate;
    }

    public Project(int mID, String mProjectName,
                   String mProjectPhoto, int mProgess,
                   String mStartTime, String mEndTime,
                   String mProjectContent, String mAddress,
                   String mLocation, String mCreateBy,
                   String mTimeCreate) {
        this.mID = mID;
        this.mProjectName = mProjectName;
        this.mProjectPhoto = mProjectPhoto;
        this.mProgess = mProgess;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mProjectContent = mProjectContent;
        this.mAddress = mAddress;
        this.mCreateBy = mCreateBy;
        this.mTimeCreate = mTimeCreate;
        this.mLocation = mLocation;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmProjectName() {
        return mProjectName;
    }

    public void setmProjectName(String mProjectName) {
        this.mProjectName = mProjectName;
    }

    public String getmProjectPhoto() {
        return mProjectPhoto;
    }

    public void setmProjectPhoto(String mProjectPhoto) {
        this.mProjectPhoto = mProjectPhoto;
    }

    public int getmProgess() {
        return mProgess;
    }

    public void setmProgess(int mProgess) {
        this.mProgess = mProgess;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmProjectContent() {
        return mProjectContent;
    }

    public void setmProjectContent(String mProjectContent) {
        this.mProjectContent = mProjectContent;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmCreateBy() {
        return mCreateBy;
    }

    public void setmCreateBy(String mCreateBy) {
        this.mCreateBy = mCreateBy;
    }

    public String getmTimeCreate() {
        return mTimeCreate;
    }

    public void setmTimeCreate(String mTimeCreate) {
        this.mTimeCreate = mTimeCreate;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
