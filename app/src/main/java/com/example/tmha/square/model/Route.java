package com.example.tmha.square.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Aka on 6/27/2017.
 */

public class Route {
    private String mStartAddress;
    private String mEndAddress;
    private LatLng mStartLocation;
    private LatLng mEndLocation;
    private Distance mDistance;
    private Duration mDuration;

    private List<LatLng> mLatLngs;

    public Route() {
    }

    public Route(String mStartAddress, String mEndAddress,
                 LatLng mStartLocation, LatLng mEndLocation,
                 Distance mDistance, Duration mDuration,
                 List<LatLng> mLatLngs) {
        this.mStartAddress = mStartAddress;
        this.mEndAddress = mEndAddress;
        this.mStartLocation = mStartLocation;
        this.mEndLocation = mEndLocation;
        this.mDistance = mDistance;
        this.mDuration = mDuration;
        this.mLatLngs = mLatLngs;
    }

    public String getmStartAddress() {
        return mStartAddress;
    }

    public void setmStartAddress(String mStartAddress) {
        this.mStartAddress = mStartAddress;
    }

    public String getmEndAddress() {
        return mEndAddress;
    }

    public void setmEndAddress(String mEndAddress) {
        this.mEndAddress = mEndAddress;
    }

    public LatLng getmStartLocation() {
        return mStartLocation;
    }

    public void setmStartLocation(LatLng mStartLocation) {
        this.mStartLocation = mStartLocation;
    }

    public LatLng getmEndLocation() {
        return mEndLocation;
    }

    public void setmEndLocation(LatLng mEndLocation) {
        this.mEndLocation = mEndLocation;
    }

    public Distance getmDistance() {
        return mDistance;
    }

    public void setmDistance(Distance mDistance) {
        this.mDistance = mDistance;
    }

    public Duration getmDuration() {
        return mDuration;
    }

    public void setmDuration(Duration mDuration) {
        this.mDuration = mDuration;
    }

    public List<LatLng> getmLatLngs() {
        return mLatLngs;
    }

    public void setmLatLngs(List<LatLng> mLatLngs) {
        this.mLatLngs = mLatLngs;
    }
}
