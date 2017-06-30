package com.example.tmha.square.handler;

import android.location.LocationManager;

/**
 * Created by Aka on 6/29/2017.
 */

public class GetLocation {
    private LocationManager mLocationManager;

//    public void click() {
//        if (!checkLocation()){
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLocationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListener);
//    }
//
//
//
//    private final LocationListener locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            mLocation = location;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mEdtOrigin.setText("("+ mLocation.getLatitude() + ", "
//                            + mLocation.getLongitude() + ")");
//                    Toast.makeText(MapsActivity.this,
//                            "Network Provider update",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//
//        }
//    };
//
//    private boolean checkLocation() {
//        if(!isLocationEnabled())
//            showAlert();
//        return isLocationEnabled();
//    }
//
//    private boolean isLocationEnabled() {
//        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//    private void showAlert() {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Enable Location")
//                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
//                        "use this app")
//                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    }
//                });
//        dialog.show();
//    }

}
