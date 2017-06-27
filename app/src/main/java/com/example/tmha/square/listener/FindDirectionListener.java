package com.example.tmha.square.listener;

import com.example.tmha.square.model.Route;

import java.util.List;

/**
 * Created by Aka on 6/27/2017.
 */

public interface FindDirectionListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
