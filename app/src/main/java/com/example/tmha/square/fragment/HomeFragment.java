package com.example.tmha.square.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.MainActivity;
import com.example.tmha.square.adapter.MapInforAdapter;
import com.example.tmha.square.adapter.WorkHeadAdapter;
import com.example.tmha.square.model.Project;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.tmha.square.activity.MainActivity.database;

/**
 * Created by tmha on 6/7/2017.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback{
    View mView;
    RecyclerView mRecyclerView;
    List<Project> mListReport;
    WorkHeadAdapter mWorkHeadAdapter;
    MapFragment mapFragment;
    GoogleMap mMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        addControls();
        return mView;
    }

    /**
     * add controls and initalization them
     *
     */
    private void addControls() {

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragMap);
        mapFragment.getMapAsync(this);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerViewList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mListReport = new ArrayList<>();

        //get list all report from sqlite
        mListReport.addAll(database.getLimitproject(10, 0));

        mWorkHeadAdapter = new WorkHeadAdapter(getActivity(), mListReport);
        mRecyclerView.setAdapter(mWorkHeadAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        mWorkHeadAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng toi = new LatLng(10.862357, 106.619517);
        mMap.addMarker(new MarkerOptions().position(toi)
                .title("Cho tao dang o ne may")
                .snippet("To hop anh hung vo lam"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toi, 16));
        List<Project> project = MainActivity.database.getLimitproject(1, 0);

        mMap.setInfoWindowAdapter(new MapInforAdapter(getActivity(), project.get(0)));


    }


}
