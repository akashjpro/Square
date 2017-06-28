package com.example.tmha.square.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.handler.FindDirection;
import com.example.tmha.square.listener.FindDirectionListener;
import com.example.tmha.square.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback{

    private GoogleMap mMap;
    private EditText mEdtOrigin, mEdtDestination;
    private Button mBtnFind;
    private List<Marker> mOriginMarkers = new ArrayList<>();
    private List<Marker> mDestinationMarkers = new ArrayList<>();
    private List<Polyline> mPolyline = new ArrayList<>();
    private  ProgressDialog mProgressDialog;
    private LatLng mMyLatLng;

    GoogleMap.OnMyLocationChangeListener listenerLocationChange = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mMyLatLng = new LatLng(location.getLatitude(),
                    location.getLongitude());
            if(mMap != null){
                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions().position(mMyLatLng)
                                .title("My location")
                                .snippet("(" + mMyLatLng.latitude + mMyLatLng.longitude + ")"));
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(mMyLatLng, 16.0f));
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mEdtOrigin      = (EditText) findViewById(R.id.edtOrigin);
        mEdtDestination = (EditText) findViewById(R.id.edtDestination);
        mBtnFind        = (Button) findViewById(R.id.btnFind);

        mBtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

    }

    private void checkInput() {
        final String origin = mEdtOrigin.getText().toString();
        final String destination = mEdtDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new FindDirection(new FindDirectionListener() {
                            @Override
                            public void onDirectionFinderStart() {
                                hadlerBeforeDraw();
                            }

                            @Override
                            public void onDirectionFinderSuccess(
                                    List<Route> routes) {
                                drawPolyline(routes);
                            }
                        }, origin, destination).execute();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });


    }

    private void hadlerBeforeDraw() {
        mProgressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding...", false);
        mProgressDialog.setCanceledOnTouchOutside(true);

        if ( mOriginMarkers!= null) {
            for (Marker marker : mOriginMarkers) {
                marker.remove();
            }
        }

        if (mDestinationMarkers != null) {
            for (Marker marker : mDestinationMarkers) {
                marker.remove();
            }
        }

        if (mPolyline != null) {
            for (Polyline polyline:mPolyline ) {
                polyline.remove();
            }
        }
    }

    private void drawPolyline(List<Route> routes) {
        mProgressDialog.dismiss();
        mPolyline = new ArrayList<>();
        mOriginMarkers = new ArrayList<>();
        mDestinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(route.getmStartLocation(), 14));
            ((TextView) findViewById(R.id.tvDuration))
                    .setText(route.getmDuration().getmText());
            ((TextView) findViewById(R.id.tvDistance))
                    .setText(route.getmDistance().getmText());

            mOriginMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_marker_a))
                    .title(route.getmStartAddress())
                    .position(route.getmStartLocation())));
            mDestinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_marker_b))
                    .title(route.getmEndAddress())
                    .position(route.getmEndLocation())));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);

            for (int i = 0; i < route.getmLatLngs().size(); i++)
                polylineOptions.add(route.getmLatLngs().get(i));

            mPolyline.add(mMap.addPolyline(polylineOptions));
        }

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng choTCH = new LatLng(10.859828, 106.618125);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(choTCH, 16));
        mOriginMarkers.add(  mMap.addMarker(new MarkerOptions().position(choTCH)
                .title("Chợ Tân Chánh Hiệp")
                .snippet("Nơi tập chung mua bán")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_a))));
        LatLng benXeAnSuong = new LatLng(10.844015, 106.613571);
//        mMap.addPolyline(new PolylineOptions().add(
//                choTCH,
//                new LatLng(10.850921, 106.628621),
//                new LatLng(10.850921, 106.628621),
//                benXeAnSuong
//             )
//                .width(10)
//                .color(Color.RED)
//        );

//        mMap.addMarker(new MarkerOptions().position(benXeAnSuong)
//                .title("Bến xe An Sương")
//                .snippet("Tập chung xe vận chuyển")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_b)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mEdtOrigin.isFocused()){
                    mEdtOrigin.setText(mMyLatLng.toString());
                }else if (mEdtDestination.isFocused()){
                    mEdtDestination.setText(mMyLatLng.toString());
                }else {
                    mEdtOrigin.setText(mMyLatLng.toString());
                }
                return false;
            }
        });

//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
//        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
//                mMap.setOnMyLocationChangeListener(listenerLocationChange);
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
