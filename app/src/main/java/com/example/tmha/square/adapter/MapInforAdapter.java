package com.example.tmha.square.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.model.Project;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

/**
 * Created by Aka on 6/30/2017.
 */

public class MapInforAdapter implements GoogleMap.InfoWindowAdapter {
    Activity mContext;
    Project mProject;

    public MapInforAdapter(Activity mContext, Project mProject) {
        this.mContext = mContext;
        this.mProject = mProject;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = this.mContext.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_project_map, null);
        ImageView imgPhoto = (ImageView) row.findViewById(R.id.imgPhoto);
        TextView txtName = (TextView) row.findViewById(R.id.txtNameProject);
        TextView txtAddress = (TextView) row.findViewById(R.id.txtAddress);
        Button btnViewLocation = (Button) row.findViewById(R.id.btnViewLocation);


        Picasso.with(mContext).load(mProject.getmProjectPhoto())
                .error(android.R.drawable.stat_notify_error)
                .into(imgPhoto);

        txtName.setText(mProject.getmProjectName());
        txtAddress.setText(mProject.getmAddress());

        btnViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return row;
    }
}
