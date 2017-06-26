package com.example.tmha.square.model;

import java.io.Serializable;

/**
 * Created by tmha on 6/8/2017.
 */

public class Report implements Serializable {
    private  int    mID;
    private  int    mIdProject;
    private  String mReportName;
    private  String mContent;
    private  String mAlbum;
    private  String mCreateBy;
    private  String mTimeReport;

    public Report() {
    }

    public Report(int mIdProject, String mReportName,
                  String mContent, String mAlbum, String mCreateBy,
                  String mTimeReport) {
        this.mIdProject = mIdProject;
        this.mReportName = mReportName;
        this.mContent = mContent;
        this.mAlbum = mAlbum;
        this.mCreateBy = mCreateBy;
        this.mTimeReport = mTimeReport;
    }

    public Report(int mID, int mIdProject,
                  String mReportName, String mContent,
                  String mAlbum, String mCreateBy, String mTimeReport) {
        this.mID = mID;
        this.mIdProject = mIdProject;
        this.mReportName = mReportName;
        this.mContent = mContent;
        this.mAlbum = mAlbum;
        this.mCreateBy = mCreateBy;
        this.mTimeReport = mTimeReport;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getmIdProject() {
        return mIdProject;
    }

    public void setmIdProject(int mIdProject) {
        this.mIdProject = mIdProject;
    }

    public String getmReportName() {
        return mReportName;
    }

    public void setmReportName(String mReportName) {
        this.mReportName = mReportName;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getmCreateBy() {
        return mCreateBy;
    }

    public void setmCreateBy(String mCreateBy) {
        this.mCreateBy = mCreateBy;
    }

    public String getmTimeReport() {
        return mTimeReport;
    }

    public void setmTimeReport(String mTimeReport) {
        this.mTimeReport = mTimeReport;
    }
}
