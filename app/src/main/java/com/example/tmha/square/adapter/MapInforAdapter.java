package com.example.tmha.square.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.MapsActivity;
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
        Button btnViewLocation = (Button) row.findViewById(R.id.btnFindLocation);


        Picasso.with(mContext).load(R.drawable.bitexco)
                .error(android.R.drawable.stat_notify_error)
                .into(imgPhoto);

        txtName.setText(mProject.getmProjectName());
        txtAddress.setText(mProject.getmAddress());

        btnViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("project", mProject);
                intent.putExtra("bundle", bundle);
                mContext.startActivity(intent);
            }
        });


        return row;
    }
}
