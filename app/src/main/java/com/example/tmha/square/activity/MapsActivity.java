package com.example.tmha.square.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.adapter.MapInforAdapter;
import com.example.tmha.square.handler.FindDirection;
import com.example.tmha.square.handler.GPSTracker;
import com.example.tmha.square.listener.FindDirectionListener;
import com.example.tmha.square.model.Project;
import com.example.tmha.square.model.Route;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.Locale;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private EditText mEdtOrigin, mEdtDestination;
    private Button mBtnFind;
    private List<Marker> mOriginMarkers = new ArrayList<>();
    private List<Marker> mDestinationMarkers = new ArrayList<>();
    private List<Polyline> mPolyline = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private LatLng mMyLatLng;
    private GPSTracker mGPS;
    private Location mLocation;
    private Geocoder mGeocoder;
    private String mAddress;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private Project mProject;
    private String mOrigin;
    private String mDestination;
    private String mTile;
    private static final String CURRENT_LOCATION = "Vị trí hiện tại";
    private String mCurrentLocation;


//    GoogleMap.OnMyLocationChangeListener listenerLocationChange = new GoogleMap.OnMyLocationChangeListener() {
//        @Override
//        public void onMyLocationChange(Location location) {
//            mMyLatLng = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//            if (mMap != null) {
//                mMap.clear();
//                mMap.addMarker(
//                        new MarkerOptions().position(mMyLatLng)
//                                .title("My location")
//                                .snippet("(" + mMyLatLng.latitude + mMyLatLng.longitude + ")"));
//                mMap.animateCamera(
//                        CameraUpdateFactory.newLatLngZoom(mMyLatLng, 16.0f));
//            }
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mEdtOrigin = (EditText) findViewById(R.id.edtOrigin);
        mEdtDestination = (EditText) findViewById(R.id.edtDestination);
        mBtnFind = (Button) findViewById(R.id.btnFind);

        mBtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null){
            mProject = (Project) bundle.getSerializable("project");
            mEdtDestination.setText(mProject.getmProjectName());
        }




    }


    private void checkInput() {
        mOrigin= mEdtOrigin.getText().toString();
        mDestination = mEdtDestination.getText().toString();
        if (mOrigin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (mDestination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (mDestination.equals(mProject.getmProjectName())){
            mDestination = mProject.getmLocation();
            mTile = mProject.getmProjectName();
        }else {
            mTile = " ";
        }

        if (mOrigin.equals(CURRENT_LOCATION)){
            mOrigin = mCurrentLocation;
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
                    }, mOrigin, mDestination).execute();
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

        if (mOriginMarkers != null) {
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
            for (Polyline polyline : mPolyline) {
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
                    .title(mEdtOrigin.getText().toString())
                    .snippet(route.getmStartAddress())
                    .position(route.getmStartLocation())));
            mDestinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_marker_b))
                    .title(mEdtDestination.getText().toString())
                    .snippet((route.getmEndAddress()))
                    .position(route.getmEndLocation())));

            //mMap.setInfoWindowAdapter(new MapInforAdapter(MapsActivity.this, mProject));


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
        if (mProject != null) {
            mOriginMarkers.add(mMap.addMarker(new MarkerOptions().position(choTCH)
                    .title(mProject.getmProjectName())
                    .snippet(mProject.getmAddress())
            ));
//            mMap.setInfoWindowAdapter(new MapInforAdapter(MapsActivity.this, mProject));
        }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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

//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                mLocation = location;
//            }
//        });

        mGeocoder = new Geocoder(this, Locale.getDefault());

        mMap.setOnMyLocationButtonClickListener(
                new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        getCurrentLocation();
//                if (checkLocation()){
//                    mLocation= gps.getLocation();
//                    if (mLocation != null){
//                        try {
//                            List<Address> addresses = mGeocoder
//                                    .getFromLocation(mMyLatLng.latitude,
//                                            mMyLatLng.longitude, 1);
//                            for (int i=0; i<5; i++){
//                                String address = addresses.get(0).getAddressLine(i);
//                                if (address != null){
//                                    if (mAddress != null){
//                                        mAddress +=  address + " ";
//                                    }else {
//                                        mAddress =  address + " ";
//                                    }
//                                }
//
//                            }
//                            if (mEdtOrigin.isFocused()){
//                                mEdtOrigin.setText(mAddress);
//                            }else {
//                                mEdtDestination.setText(mAddress);
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }else {
//                    showSettingsAlert();
//                }
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

//    public boolean checkLocation(){
//
//        LocationManager locationManager
//                = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        // getting GPS status
//        boolean isGPSEnabled = locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//        // getting network status
//        boolean isNetworkEnabled = locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if (isGPSEnabled ){
//            return true;
//        }
//        return false;
//    }



    public void getCurrentLocation() {
        if (!checkLocation()){
            return;
        }
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
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListener);
    }



    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            mLocation = location;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCurrentLocation = mLocation.getLatitude() + ", "
                            + mLocation.getLongitude();
                    mEdtOrigin.setText(CURRENT_LOCATION);
                    Toast.makeText(MapsActivity.this,
                            "Network Provider update",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String pathPic = mProject.getmProjectPhoto();
        String address = marker.getSnippet();
        if (!address.equals(mProject.getmAddress())){
            pathPic = "http://www.wallpaperup.com/uploads/wallpapers/2016/07/02/994343/big_thumb_5b1bc79dc86b691f7ef251d83168143f.jpg";
        }
        Project project = new Project(marker.getTitle(),
                pathPic, address, mProject.getmLocation());
        mProject.setmAddress(marker.getSnippet());
        mProject.setmProjectName(marker.getTitle());
        mMap.setInfoWindowAdapter(new MapInforAdapter(MapsActivity.this, project));
    }
}
