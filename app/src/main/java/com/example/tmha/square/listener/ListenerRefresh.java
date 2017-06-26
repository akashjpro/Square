package com.example.tmha.square.listener;

import com.example.tmha.square.model.Project;

/**
 * Created by tmha on 6/19/2017.
 */

public interface ListenerRefresh {
    void updateReport(int position, Project report);
    void addReport(Project report);
}
